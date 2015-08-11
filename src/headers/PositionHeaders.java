package headers;

import helper.DataTypes;
import helper.Headers;

public class PositionHeaders extends Headers {
	public PositionHeaders() {
		super (0, 2, 0, 2, "position");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"position",
				"position short code"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.TEXT,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = {
				0
		};
		
		String[] duplicateCheckNames = {
				"position"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
