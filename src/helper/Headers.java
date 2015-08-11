package helper;

public abstract class Headers {
	private int headerRow;
	private int headerCount;
	private int headerPadding;
	private int databaseFields;
	private String tableName;
	private String[] headerNames;
	private DataTypes[] dataTypes;
	private int[] duplicateCheckColumns;
	private String[] duplicateCheckNames;
	
	public Headers(int headerRow, int headerCount, int headerPadding, int databaseFields, String tableName) {
		this.headerRow = headerRow;
		this.headerCount = headerCount;
		this.headerPadding = headerPadding;
		this.databaseFields = databaseFields;
		this.tableName = tableName;
	}
	
	public abstract void initialize();
	
	protected void setHeaderNames(String[] headerNames) {
		this.headerNames = headerNames;
	}
	
	protected void setDataTypes(DataTypes[] dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	protected void setDuplicateCheckColumns(int[] duplicateCheckColumns) {
		this.duplicateCheckColumns = duplicateCheckColumns;
	}
	
	protected void setDuplicateCheckNames(String[] duplicateCheckNames) {
		this.duplicateCheckNames = duplicateCheckNames;
	}
	
	public int getHeaderRow() {
		return headerRow;
	}
	
	public int getHeaderCount() {
		return headerCount;
	}
	
	public int getHeaderPadding() {
		return headerPadding;
	}
	
	public int getDatabaseFields() {
		return databaseFields;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public String[] getHeaderNames() {
		return headerNames;
	}
	
	public DataTypes[] getDataTypes() {
		return dataTypes;
	}
	
	public int[] getDuplicateCheckColumns() {
		return duplicateCheckColumns;
	}

	public String[] getDuplicateCheckNames() {
		return duplicateCheckNames;
	}
}
