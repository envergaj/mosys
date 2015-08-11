package payroll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import helper.Helper;
import helper.PayrollTypes;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import deduction.Deduction;

public class Payroll {
	private Workbook workbook;
	private Connection connection;
	private PayrollTypes type;
	private Deduction deduction;
	private Map<String, Integer> positions;
	
	public Payroll(Workbook workbook, Connection connection, PayrollTypes type) throws SQLException {
		this.workbook = workbook;
		this.connection = connection;
		this.type = type;
		this.deduction = new Deduction(connection);
		positions = new HashMap<String, Integer>();
		
		findPositions();
	}
	
	public void findPositions() {
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);
		
		for (int i = 0; i < row.getLastCellNum(); i++) {
			String entry = row.getCell(i).getStringCellValue().trim().toLowerCase();
			positions.put(entry, i);
		}
	}
	
	public void calculatePayroll() throws SQLException {
		Map<String, Object[]> rows = new TreeMap<String, Object[]>();
		Sheet sheet = workbook.getSheetAt(0);
		
		Object[] headers = {
				"empno",
				"name",
				"rate",
				"cola",
				"tcola",
				"days",
				"tdays",
				"hours",
				"thours",
				"regular",
				"tregular",
				"odwork",
				"todwork",
				"regwork",
				"tregwork",
				"dayoff" + type.getMark(),
				"tdayoff",
				"d08",
				"td08",
				"dowork",
				"tdowork",
				"dowork8",
				"tdowork8",
				"splhol" + type.getMark(),
				"tsplhol",
				"sh8",
				"tsh8",
				"shwork",
				"tshwork",
				"shwork8",
				"tshwork8",
				"splhol",
				"tsplhol",
				"sho8",
				"tsho8",
				"showork",
				"tshowork",
				"showork8",
				"tshowork8",
				"holiday1",
				"tholiday1",
				"holiday2",
				"tholiday2",
				"h8",
				"th8",
				"hwork",
				"thwork",
				"howork",
				"thowork",
				"holidayoff",
				"tholidayoff",
				"ho8",
				"tho8",
				"hwork8",
				"thwork8",
				"grosspay",
				"sss",
				"philhealth",
				"pagibig",
				"deductions",
				"total"
		};
		
		rows.put("1", headers);
		
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			double rate = row.getCell(positions.get("rate")).getNumericCellValue();
			double days = row.getCell(positions.get("days")).getNumericCellValue();
			double total8 = (rate + row.getCell(positions.get("ecola")).getNumericCellValue()) / 8.0;
			double rate8 = rate / 8.0; 
			
			Object[] data1 = {
					row.getCell(positions.get("empno")).getStringCellValue(),
					row.getCell(positions.get("name")).getStringCellValue(),
					row.getCell(positions.get("rate")).getNumericCellValue(),
					row.getCell(positions.get("ecola")).getNumericCellValue(),
					row.getCell(positions.get("ecola")).getNumericCellValue() * days,
					row.getCell(positions.get("days")).getNumericCellValue(),
					row.getCell(positions.get("days")).getNumericCellValue() * rate,
					row.getCell(positions.get("hours")).getNumericCellValue(),
					row.getCell(positions.get("hours")).getNumericCellValue() * total8 / 60.0 * -1.0,
					row.getCell(positions.get("regular")).getNumericCellValue(),
					row.getCell(positions.get("regular")).getNumericCellValue() * rate8 * 1.25,
					row.getCell(positions.get("odwork")).getNumericCellValue(),
					row.getCell(positions.get("odwork")).getNumericCellValue() * rate8 * 0.1,
					row.getCell(positions.get("regwork")).getNumericCellValue(),
					row.getCell(positions.get("regwork")).getNumericCellValue() * rate8 * 0.1 * 1.25,
					row.getCell(positions.get("dayoff" + type.getMark())).getNumericCellValue(),
					row.getCell(positions.get("dayoff" + type.getMark())).getNumericCellValue() * rate8 * (0.3 + type.getIncrease()),
					row.getCell(positions.get("do8")).getNumericCellValue(),
					row.getCell(positions.get("do8")).getNumericCellValue() * rate8 * 1.3 * 1.3,
					row.getCell(positions.get("dowork")).getNumericCellValue(),
					row.getCell(positions.get("dowork")).getNumericCellValue() * rate8 * 0.1 * 1.3,
					row.getCell(positions.get("dowork8")).getNumericCellValue(),
					row.getCell(positions.get("dowork8")).getNumericCellValue() * rate8 * 0.1 * 1.3 * 1.3,
					row.getCell(positions.get("splhol" + type.getMark())).getNumericCellValue(),
					row.getCell(positions.get("splhol" + type.getMark())).getNumericCellValue() * rate8 * (0.3 + type.getIncrease()),
					row.getCell(positions.get("sh8")).getNumericCellValue(),
					row.getCell(positions.get("sh8")).getNumericCellValue() * rate8 * 1.3 * 1.3,
					row.getCell(positions.get("shwork")).getNumericCellValue(),
					row.getCell(positions.get("shwork")).getNumericCellValue() * rate8 * 0.1 * 1.3,
					row.getCell(positions.get("shwork8")).getNumericCellValue(),
					row.getCell(positions.get("shwork8")).getNumericCellValue() * rate8 * 0.1 * 1.3 * 1.3,
					row.getCell(positions.get("splholdo")).getNumericCellValue(),
					row.getCell(positions.get("splholdo")).getNumericCellValue() * rate8 * (0.5 + type.getIncrease()),
					row.getCell(positions.get("sho8")).getNumericCellValue(),
					row.getCell(positions.get("sho8")).getNumericCellValue() * rate8 * 1.5 * 1.3,
					row.getCell(positions.get("showork")).getNumericCellValue(),
					row.getCell(positions.get("showork")).getNumericCellValue() * rate8 * 0.1 * 1.5,
					row.getCell(positions.get("showork8")).getNumericCellValue(),
					row.getCell(positions.get("showork8")).getNumericCellValue() * rate8 * 0.1 * 1.5 * 1.3,
					row.getCell(positions.get("holiday1")).getNumericCellValue(),
					row.getCell(positions.get("holiday1")).getNumericCellValue() * total8,
					row.getCell(positions.get("holiday2")).getNumericCellValue(),
					row.getCell(positions.get("holiday2")).getNumericCellValue() * total8 * 2.0,
					row.getCell(positions.get("h8")).getNumericCellValue(),
					row.getCell(positions.get("h8")).getNumericCellValue() * rate8 * 2.6,
					row.getCell(positions.get("hwork")).getNumericCellValue(),
					row.getCell(positions.get("hwork")).getNumericCellValue() * rate8 * 0.1 * 2.0,
					row.getCell(positions.get("howork")).getNumericCellValue(),
					row.getCell(positions.get("howork")).getNumericCellValue() * rate8 * 0.1 * 2.6,
					row.getCell(positions.get("holidayoff")).getNumericCellValue(),
					row.getCell(positions.get("holidayoff")).getNumericCellValue() * rate8 * (1.6 + type.getIncrease()),
					row.getCell(positions.get("ho8")).getNumericCellValue(),
					row.getCell(positions.get("ho8")).getNumericCellValue() * rate8 * 2.6 * 1.3,
					row.getCell(positions.get("hwork8")).getNumericCellValue(),
					row.getCell(positions.get("hwork8")).getNumericCellValue() * rate8 * 0.1 * 2.6
			};
			
			double sum = 0;
			
			for (int j = 4; j < data1.length; j += 2) {
				sum += (Double) data1[j];
			}
			
			double sss = getSSS(sum);
			double philhealth = getPhilHealth(sum);
			double pagibig = getPagibig(sum);
			double deductions = deduction.applyDeductions(row.getCell(positions.get("empno")).getStringCellValue().trim(), sum);
			
			Object[] data2 = {
					sum,
					sss,
					philhealth,
					pagibig,
					deductions,
					sum - sss - philhealth - pagibig - deductions 
			};
			
			Object[] data = concatenate(data1, data2);
			rows.put(Integer.toString(i + 1), data);
		}
		
		Helper.writeWorkbook(rows, "out/payroll.xlsx");
		deduction.writeDeduction();
	}
	
	private double getSSS(double salary) {
		double[][] salaryRanges = {
				{ 1249.99, 36.3, 83.7 },
				{ 1749.99, 54.5, 120.5 },
				{ 2249.99, 72.7, 157.3 },
				{ 2749.99, 90.8, 194.2 },
				{ 3249.99, 109, 231 },
				{ 3749.99, 127.2, 267.8 },
				{ 4249.99, 145.3, 304.7 },
				{ 4749.99, 163.5, 341.5 },
				{ 5249.99, 181.7, 378.3 },
				{ 5749.99, 199.8, 415.2 },
				{ 6249.99, 218, 452 },
				{ 6749.99, 236.2, 488.8 },
				{ 7249.99, 254.3, 525.7 },
				{ 7749.99, 272.5, 562.5 },
				{ 8249.99, 290.7, 599.3 },
				{ 8749.99, 308.8, 636.2 },
				{ 9249.99, 327, 673 },
				{ 9749.99, 345.2, 709.8 },
				{ 10249.99, 363.3, 746.7 },
				{ 10749.99, 381.5, 783.5 },
				{ 11249.99, 399.7, 820.3 },
				{ 11749.99, 417.8, 857.2 },
				{ 12249.99, 436, 894 },
				{ 12749.99, 454.2, 930.8 },
				{ 13249.99, 472.3, 967.7 },
				{ 13749.99, 490.5, 1004.5 },
				{ 14249.99, 508.7, 1041.3 },
				{ 14749.99, 526.8, 1078.2 },
				{ 15249.99, 545, 1135 },
				{ 15749.99, 563.2, 1171.8 },
				{ Double.MAX_VALUE, 581.3, 1208.7 }
		};
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 581.3;
	}
	
	private double getPhilHealth(double salary) {
		double[][] salaryRanges = {
				{ 8999.99, 100.00 },
				{ 9999.99, 112.50 },
				{ 10999.99, 125.00 },
				{ 11999.99, 137.50 },
				{ 12999.99, 150.00 },
				{ 13999.99, 162.50 },
				{ 14999.99, 175.00 },
				{ 15999.99, 187.50 },
				{ 16999.99, 200.00 },
				{ 17999.99, 212.50 },
				{ 18999.99, 225.00 },
				{ 19999.99, 237.50 },
				{ 20999.99, 250.00 },
				{ 21999.99, 262.50 },
				{ 22999.99, 275.00 },
				{ 23999.99, 287.50 },
				{ 24999.99, 300.00 },
				{ 25999.99, 312.50 },
				{ 26999.99, 325.00 },
				{ 27999.99, 337.50 },
				{ 28999.99, 350.00 },
				{ 29999.99, 362.50 },
				{ 30999.99, 375.00 },
				{ 31999.99, 387.50 },
				{ 32999.99, 400.00 },
				{ 33999.99, 412.50 },
				{ 34999.99, 425.00 },
				{ Double.MAX_VALUE, 437.50 }
		};
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 437.50;
	}
	
	private double getPagibig(double salary) {
		double[][] salaryRanges = {
				{ Double.MAX_VALUE, 100.0, 100.0 }
		};
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 100.0;
	}
	
	public Object[] concatenate(Object[] a, Object[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   Object[] c= new Object[aLen + bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}
}
