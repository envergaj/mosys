package mosys;

import helper.PayrollHalfTypes;
import helper.PayrollPeriodTypes;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dataEntry.DataEntry;
import payroll.Payroll;
import validation.Validator;
import variable.Modules;

public class Test {
	public static void main(String[] args)
	throws EncryptedDocumentException, InvalidFormatException, IOException, SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mosys", "root", "");
		String filename;
		Workbook workbook;
		
//		filename = "test/client.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.CLIENT);
//			
//		filename = "test/coop.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.COOP);
//		
//		filename = "test/position.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.POSITION);
//		
//		filename = "test/workLocation.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.WORK_LOCATION);
//		
//		filename = "test/carp.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.EMPLOYEE);
//		
//		filename = "test/deduction.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.DEDUCTION);
//	
//		filename = "test/employeeDeduction.xlsx";
//		workbook = WorkbookFactory.create(new File(filename));
//		dataEntry(workbook, connection, Modules.EMPLOYEE_DEDUCTION);
		
		filename = "test/timesummary.xlsx"; // you can use apples or timesummary
		workbook = WorkbookFactory.create(new File(filename));
		payroll(workbook, connection, PayrollPeriodTypes.DAILY, PayrollHalfTypes.FIRST);
	}
	
	public static Set<int[]> validate(Workbook workbook, Connection connection, Modules module) throws SQLException {
		Validator validator = new Validator(workbook, connection, module);
		
		List<Integer> invalidHeaderCounts = validator.findInvalidHeaderCounts();
		Set<int[]> invalidHeaders = validator.findInvalidHeaders();
		Set<int[]> invalidDataTypes = validator.findInvalidDataTypes();
		Set<int[]> duplicatesWorkbook = validator.findDuplicatesWorkbook();
		Set<int[]> duplicatesDatabase = validator.findDuplicatesDatabase();
		
		System.out.println("number of invalid header counts: " + invalidHeaderCounts.size());
		System.out.println("number of invalid headers: " + invalidHeaders.size());
		System.out.println("number of invalid data types: " + invalidDataTypes.size());
		System.out.println("number of duplicates in workbook: " + duplicatesWorkbook.size());
		System.out.println("number of duplicates in database: " + duplicatesDatabase.size());
		
		Set<int[]> duplicateRows = new HashSet<int[]>();
		Iterator<int[]> iterator = duplicatesWorkbook.iterator();
		
		while (iterator.hasNext()) {
			int[] position = iterator.next();
			duplicateRows.add(new int[] { position[0], position[1] });
		}
		
		iterator = duplicatesDatabase.iterator();
		
		while (iterator.hasNext()) {
			int[] position = iterator.next();
			duplicateRows.add(new int[] { position[0], position[1] });
		}
		
		return duplicateRows;
	}
	
	public static void dataEntry(Workbook workbook, Connection connection, Modules module) throws SQLException {
		Set<List<Integer>> skipRows = new HashSet<List<Integer>>();
		Set<int[]> duplicateRows = validate(workbook, connection, module);
		Iterator<int[]> iterator = duplicateRows.iterator();
		
		while (iterator.hasNext()) {
			int[] position = iterator.next();
			skipRows.add(Arrays.asList(position[0], position[1]));
		}
		
		DataEntry dataEntry = new DataEntry(connection, workbook, module, skipRows);
		dataEntry.addAll();
		
		System.out.println("\nnumber of duplicates not added: " + duplicateRows.size());
		System.out.println("all other entries added to database\n\n");
	}
	
	public static void payroll(Workbook workbook, Connection connection, PayrollPeriodTypes period,
			PayrollHalfTypes half) throws SQLException {
		Payroll payroll = new Payroll(workbook, connection, period, half);
		payroll.calculatePayroll();
		
		System.out.println("payroll calculated");
		System.out.println("deductions applied\n\n");
	}
}