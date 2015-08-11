package headers;

import helper.DataTypes;
import helper.Headers;

public class ClientHeaders extends Headers {
	public ClientHeaders() {
		super (0, 2, 0, 2, "client");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"client company",
				"client company short code"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.TEXT,
				DataTypes.TEXT
		};
		
		int[] duplicateCheckColumns = {
				0
		};
		
		String[] duplicateCheckNames = {
				"client"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
