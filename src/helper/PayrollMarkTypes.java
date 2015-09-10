package helper;

public enum PayrollMarkTypes {
	ONE ("1", 0),
	TWO ("2", 1.0);
	
	private final String mark;
	private final double increase;
	
	private PayrollMarkTypes (String mark, double increase) {
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
