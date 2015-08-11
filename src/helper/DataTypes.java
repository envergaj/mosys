package helper;

import org.apache.poi.ss.usermodel.Cell;

public enum DataTypes {
	DATE,
	TEXT,
	NUMBER,
	GENDER,
	CIVIL_STATUS,
	EMPLOYEE_NUMBER,
	SSS,
	PAGIBIG,
	PHILHEALTH,
	TIN;
	
	public static boolean isValid(DataTypes dataType, Cell cell) {
		boolean isText = cell.getCellType() == Cell.CELL_TYPE_STRING;
		boolean isNumber = cell.getCellType() == Cell.CELL_TYPE_NUMERIC;
		String contents = "";
		
		if (isText) {
			contents = cell.getStringCellValue().trim().toUpperCase();
		} else if (isNumber) {
			contents = Double.toString(cell.getNumericCellValue()).trim();
		}
		
		switch (dataType) {
		case DATE:
			try {
				cell.getDateCellValue();
				return true;
			} catch (IllegalStateException e) {
				return false;
			}
		case TEXT:
			return isText;
		case NUMBER:
			return isNumber;
		case GENDER:
			if (isText) {
				return contents.equals("M") || contents.equals("F");
			}
			return false;
		case CIVIL_STATUS:
			if (isText) {
				return contents.equals("S") || contents.equals("M") || contents.equals("W");
			}
			return false;
		case EMPLOYEE_NUMBER:
			return isText;
		case SSS:
			if (isNumber) {
				return contents.length() == 10;
			}
			return false;
		case PAGIBIG:
			if (isNumber) {
				return contents.length() == 12;
			}
			return false;
		case PHILHEALTH:
			if (isNumber) {
				return contents.length() == 12;
			}
			return false;
		case TIN:
			if (isNumber) {
				return contents.length() == 9;
			}
			return false;
		}
		
		return false;
	}
}