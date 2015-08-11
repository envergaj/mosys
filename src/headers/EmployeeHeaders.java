package headers;

import helper.DataTypes;
import helper.Headers;

public class EmployeeHeaders extends Headers {
	public EmployeeHeaders() {
		super (4, 16, 2, 20, "employee");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"emp. no.",
				"last name",
				"first name",
				"middle name",
				"sex",
				"birth date",
				"status",
				"position",
				"company",
				"start date",
				"address",
				"sss no.",
				"pagibig no.",
				"philhealth",
				"tin",
				"atm no."
		};
		
		DataTypes[] dataTypes = {
				DataTypes.EMPLOYEE_NUMBER,
				DataTypes.TEXT,
				DataTypes.TEXT,
				DataTypes.TEXT,
				DataTypes.GENDER,
				DataTypes.DATE,
				DataTypes.CIVIL_STATUS,
				DataTypes.TEXT,
				DataTypes.TEXT,
				DataTypes.DATE,
				DataTypes.TEXT,
				DataTypes.SSS,
				DataTypes.PAGIBIG,
				DataTypes.PHILHEALTH,
				DataTypes.TIN,
				DataTypes.NUMBER
		};
		
		int[] duplicateCheckColumns = {
				0,
				11,
				12,
				13,
				14,
				15
		};
		
		String[] duplicateCheckNames = {
				"employeeNumber",
				"sssNumber",
				"pagibigNumber",
				"philhealthNumber",
				"tinNumber",
				"cashcardNumber"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
