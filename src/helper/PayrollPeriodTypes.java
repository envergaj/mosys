package helper;

public enum PayrollPeriodTypes {
	DAILY (8.0),
	MONTHLY (26.0 * 8.0),
	SPECIAL_MONTHLY (26.083 * 8.0),
	HOURLY (60.0);
	
	private final double divisor;
	
	private PayrollPeriodTypes(double divisor) {
		this.divisor = divisor;
	}
	
	public double getDivisor() {
		return divisor;
	}
}
