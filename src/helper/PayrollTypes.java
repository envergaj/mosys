package helper;

public enum PayrollTypes {
	ONE ("1", 0),
	TWO ("2", 1.0);
	
	private final String mark;
	private final double increase;
	
	private PayrollTypes (String mark, double increase) {
		this.mark = mark;
		this.increase = increase;
	}
	
	public String getMark() {
		return mark;
	}
	
	public double getIncrease() {
		return increase;
	}
}
