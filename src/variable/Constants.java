package variable;

public final class Constants {
	public static final String EXISTS_DB = "select 1 from %s where %s = ?";
	public static final String ADD_DB = "call add";
	public static final String GET_DEDUCTIONS = "select * from employeeDeduction where employeeNumber = ?";
	public static final String UPDATE_DEDUCTIONS = "call updateEmployeeDeduction (?, ?, ?, ?, ?, ?)";
	public static final double MINIMUM_SALARY = 1500.00;
	//public static final int SHEET_INDEX = 0;
	//public static final int ROW_INDEX = 1;
	//public static final int COLUMN_INDEX = 2;
}
