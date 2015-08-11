package variable;

import headers.ClientHeaders;
import headers.CoopHeaders;
import headers.DeductionHeaders;
import headers.EmployeeDeductionHeaders;
import headers.EmployeeHeaders;
import headers.PositionHeaders;
import headers.WorkLocationHeaders;
import helper.Headers;

public enum Modules {
	EMPLOYEE (new EmployeeHeaders()),
	//EMPLOYMENT_HISTORY (2, 7),
	COOP (new CoopHeaders()),
	WORK_LOCATION (new WorkLocationHeaders()),
	CLIENT (new ClientHeaders()),
	POSITION (new PositionHeaders()),
	DEDUCTION (new DeductionHeaders()),
	EMPLOYEE_DEDUCTION (new EmployeeDeductionHeaders());
	
	private final Headers headers;
	
	private Modules(Headers headers) {
		this.headers = headers;
	}
	
	public Headers getHeaders() {
		return headers;
	}
}
