package headers;

import helper.DataTypes;
import helper.Headers;

public class DeductionHeaders extends Headers {
	public DeductionHeaders() {
		super (0, 2, 0, 2, "deduction");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"deduction",
				"deduction short code"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.TEXT,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = {
				0
		};
		
		String[] duplicateCheckNames = {
				"deduction"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
