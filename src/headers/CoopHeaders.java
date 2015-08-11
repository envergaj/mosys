package headers;

import helper.DataTypes;
import helper.Headers;

public class CoopHeaders extends Headers {
	public CoopHeaders() {
		super (0, 7, 0, 7, "coop");
		initialize();
	}

	@Override
	public void initialize() {
		String[] headerNames = {
				"coop name",
				"coop short code",
				"address",
				"sss employer number",
				"phic employer number",
				"hdmf employer number",
				"tin employer number"
		};
		
		DataTypes[] dataTypes = {
				DataTypes.TEXT,
				DataTypes.TEXT,
				DataTypes.TEXT,
				DataTypes.SSS,
				DataTypes.PHILHEALTH,
				DataTypes.PAGIBIG,
				DataTypes.TIN
		};
		
		int[] duplicateCheckColumns = {
				0,
				3,
				4,
				5,
				6
		};
		
		String[] duplicateCheckNames = {
				"coop",
				"sssEmployerNumber",
				"phicEmployerNumber",
				"hdmfEmployerNumber",
				"tinEmployerNumber"
		};
		
		this.setHeaderNames(headerNames);
		this.setDataTypes(dataTypes);
		this.setDuplicateCheckColumns(duplicateCheckColumns);
		this.setDuplicateCheckNames(duplicateCheckNames);
	}
}
