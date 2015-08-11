package headers;

import helper.DataTypes;
import helper.Headers;

public class WorkLocationHeaders extends Headers {
	public WorkLocationHeaders() {
		super (0, 2, 0, 2, "workLocation");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"work location",
				"location short code"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.TEXT,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = {
				0
		};
		
		String[] duplicateCheckNames = {
				"workLocation"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
