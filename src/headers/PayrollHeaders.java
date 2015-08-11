package headers;

import helper.DataTypes;
import helper.Headers;

public class PayrollHeaders extends Headers {
	private double[] multipliers;
	private short type;
	
	public PayrollHeaders(short type) {
		super (0, 29, 0, 5, "employee");
		this.type = type;
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"empno",
				"name"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.EMPLOYEE_NUMBER,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = {
				0,
		};
		
		double dod = 0.3 / 8.0;
		double splhol = 0.3 / 8.0;
		double splholrd = 0.5 / 8.0;
		double lglholrd = 1.6 / 8.0;
		
		if (type == 2) {
			dod = 1.3 / 8.0;
			splhol = 1.3 / 8.0;
			splholrd = 1.5 / 8.0;
			lglholrd = 2.6 / 8.0;
		}
		
		double[] multipliers = {
				1.25 / 8.0,
				0.1 / 8.0,
				0.1 * 1.25 / 8.0,
				dod,
				1.3 * 1.3 / 8.0,
				0.1 * 1.3 / 8.0,
				0.1 * 1.3 * 1.3 / 8.0,
				splhol,
				1.3 * 1.3 / 8.0,
				0.1 * 1.3 / 8.0,
				0.1 * 1.3 * 1.3 / 8.0,
				splholrd,
				1.5 * 1.3 / 8.0,
				0.1 * 1.5 / 8.0,
				0.1 * 1.5 * 1.3 / 8.0,
				1.0 / 8.0,
				2.0 / 8.0,
				2.6 / 8.0,
				0.1 * 2.0 / 8.0,
				0.1 * 2.6 / 8.0,
				lglholrd,
				2.6 * 1.3 / 8.0,
				0.1 * 2.6 / 8.0
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.multipliers = multipliers;
	}
	
	public double[] getMultipliers() {
		return multipliers;
	}
}
