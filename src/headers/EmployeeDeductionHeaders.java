package headers;

import helper.DataTypes;
import helper.Headers;

public class EmployeeDeductionHeaders extends Headers {
	public EmployeeDeductionHeaders() {
		super (0, 5, 0, 5, "employeeDeduction");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"empno",
				"name",
				"principal",
				"amt",
				"code"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.EMPLOYEE_NUMBER,
				DataTypes.TEXT,
				DataTypes.NUMBER,
				DataTypes.NUMBER,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = { };
		
		String[] duplicateCheckNames = { };
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
		this.setAddStatement("insert into employeeDeduction (employeeNumber, "
				+ "employeeName, principal, amortization, deductionShortCode)"
				+ "values (?, ?, ?, ?, ?);");
	}
}
