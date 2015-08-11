package validation;

import variable.Constants;
import variable.Modules;
import helper.DataTypes;
import helper.Headers;
import helper.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Validator {
	private Workbook workbook;
	private Connection connection;
	private Headers headers;
	private int headerIndex;
	
	public Validator(Workbook workbook, Connection connection, Modules module) {
		this.workbook = workbook;
		this.connection = connection;
		this.headers = module.getHeaders();
		headerIndex = headers.getHeaderRow();
	}
	
	public List<Integer> findInvalidHeaderCounts() {
		List<Integer> invalidHeaderCounts = new ArrayList<Integer>();
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			int headerCount = sheet.getRow(headerIndex).getLastCellNum();
			
			if (headerCount != headers.getHeaderCount()) {
				invalidHeaderCounts.add(i);
			}
		}
		
		return invalidHeaderCounts;
	}
	
	public Set<int[]> findInvalidHeaders() {
		String[] headerNames = headers.getHeaderNames();
		Set<int[]> invalidHeaders = new HashSet<int[]>();
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			Row row = sheet.getRow(headerIndex);
			
			for (int j = 0; j < headers.getHeaderCount(); j++) {
				String header = row.getCell(j).getStringCellValue().trim().toLowerCase();
				
				if (!headerNames[j].equals(header)) {
					invalidHeaders.add(new int[] {i, headerIndex, j});
				}
			}
		}
		
		return invalidHeaders;
	}

	public Set<int[]> findInvalidDataTypes() {
		DataTypes[] dataTypes = headers.getDataTypes();
		Set<int[]> invalidDataTypes = new HashSet<int[]>();
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			
			for (int j = headerIndex + 1; j < sheet.getLastRowNum() - headers.getHeaderPadding(); j++) {
				Row row = sheet.getRow(j);
				
				for (int l = 0; l < headers.getHeaderCount(); l++) {
					Cell cell = row.getCell(l);

					if (!DataTypes.isValid(dataTypes[l], cell)) {
						invalidDataTypes.add(new int[] {i, headerIndex, j});
					}
				}
			}
		}
		
		return invalidDataTypes;
	}
	
	public Set<int[]> findDuplicatesWorkbook() {
		int[] columnsToCheck = headers.getDuplicateCheckColumns();
		Set<int[]> duplicateEntries = new HashSet<int[]>();
		
		for (int i = 0; i < columnsToCheck.length; i++) {
			Map<String, int[]> headersSeen = new HashMap<String, int[]>();
			int index = columnsToCheck[i];
			
			for (int j = 0; j < workbook.getNumberOfSheets(); j++) {
				Sheet sheet = workbook.getSheetAt(j);
				
				for (int l = headerIndex + 1; l < sheet.getLastRowNum() - headers.getHeaderPadding(); l++) {
					Cell cell = sheet.getRow(l).getCell(index);
					String entry = Helper.getCellContents(cell);
					
					if (entry != null && entry.length() != 0 && headersSeen.containsKey(entry)) {
						duplicateEntries.add(new int[] {j, l, index});
						duplicateEntries.add(headersSeen.get(entry));
					} else {
						headersSeen.put(entry, new int[] {j, l, index});
					}
				}
			}
		}
		
		return duplicateEntries;
	}
	
	public Set<int[]> findDuplicatesDatabase() throws SQLException {
		int[] indicesToCheck = headers.getDuplicateCheckColumns();
		String[] namesToCheck = headers.getDuplicateCheckNames();
		Set<int[]> duplicateEntries = new HashSet<int[]>();
		
		for (int i = 0; i < indicesToCheck.length; i++) {
			int index = indicesToCheck[i];
			PreparedStatement statement = connection.prepareStatement
					(String.format(Constants.EXISTS_DB, headers.getTableName(), namesToCheck[i]));
			
			for (int j = 0; j < workbook.getNumberOfSheets(); j++) {
				Sheet sheet = workbook.getSheetAt(j);
				
				for (int l = headerIndex + 1; l < sheet.getLastRowNum() - headers.getHeaderPadding(); l++) {
					String entry = sheet.getRow(l).getCell(index).getStringCellValue().trim();
					
					if (entry.length() > 0) {
						statement.setString(1, entry);
						ResultSet result = statement.executeQuery();
						
						if (result.next()) {
							duplicateEntries.add(new int[] {j, l, index});
						}
					}
				}
			}
		}
		
		return duplicateEntries;
	}
}
