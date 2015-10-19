package dataEntry;

import helper.DataTypes;
import helper.Headers;
import helper.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import variable.Constants;
import variable.Modules;

public class DataEntry {
	private Connection connection;
	private Workbook workbook;
	private Modules module;
	private Set<List<Integer>> duplicateRows;
	private Headers headers;
	
	public DataEntry(Connection connection, Workbook workbook, 
			Modules module, Set<List<Integer>> duplicateRows) {
		this.connection = connection;
		this.workbook = workbook;
		this.module = module;
		this.duplicateRows = duplicateRows;
		headers = module.getHeaders();
		
	}
	
	public void addAll() throws SQLException {
		DataTypes[] dataTypes = headers.getDataTypes();
		String tableName = headers.getTableName();
		tableName = Character.toString(tableName.charAt(0)).toUpperCase() + 
				tableName.substring(1);
		String command = headers.getAddStatement();
		
		PreparedStatement statement = connection.prepareStatement(command);
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			
			if (module == Modules.EMPLOYEE) {
				statement.setString(17, "");
				statement.setString(18, "");
				statement.setString(19, sheet.getRow(0).getCell(1).
						getStringCellValue().trim());
				statement.setString(20, sheet.getRow(1).getCell(1).
						getStringCellValue().trim());
			}
			
			for (int j = headers.getHeaderRow() + 1; j < sheet.getLastRowNum() - 
					headers.getHeaderPadding(); j++) {
				if (!duplicateRows.contains(Arrays.asList(i, j))) {
					Row row = sheet.getRow(j);
					
					for (int l = 0; l < headers.getHeaderCount(); l++) {
						Cell cell = row.getCell(l);
						
						if (dataTypes[l] == DataTypes.DATE) {
							Date date = cell.getDateCellValue();
							statement.setDate(l + 1, new java.sql.Date(date.getTime()));
						} else {
							statement.setString(l + 1, Helper.getCellContents(cell));
						}
					}
					
					statement.execute();
				}
			}
		}
	}
}
