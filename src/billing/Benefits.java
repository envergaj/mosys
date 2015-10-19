package billing;

import helper.BenefitsRateTypes;
import helper.Helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import variable.Constants;

public class Benefits {
	private Workbook firstHalf;
	private Workbook secondHalf;
	private Connection connection;
	private Map<String, Integer> positions;
	private Map<String, double[]> info;
	private BenefitsRateTypes sssType;
	private BenefitsRateTypes philhealthType;
	private BenefitsRateTypes pagibigType;
	private double sssAmount;
	private double philhealthAmount;
	private double pagibigAmount;
	
	public Benefits(Workbook firstHalf, Workbook secondHalf, Connection 
			connection)
			throws SQLException {
		this.firstHalf = firstHalf;
		this.secondHalf = secondHalf;
		this.connection = connection;
		sssType = BenefitsRateTypes.BRACKET;
		philhealthType = BenefitsRateTypes.BRACKET;
		pagibigType = BenefitsRateTypes.BRACKET;
		positions = new HashMap<String, Integer>();
		
		findPositions();
	}
	
	public void findPositions() {
		Sheet sheet = firstHalf.getSheetAt(0);
		Row row = sheet.getRow(0);
		
		for (int i = 0; i < row.getLastCellNum(); i++) {
			String entry = row.getCell(i).getStringCellValue().trim().toLowerCase();
			positions.put(entry, i);
		}
	}
	
	public void getInfo() {
		info = new TreeMap<String, double[]>();
		Sheet sheet = firstHalf.getSheetAt(0);
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			String employeeNumber = row.getCell(positions.get("empno")).
					getStringCellValue();
			double[] employeeInfo = getEmployeeInfo(row);
			
			info.put(employeeNumber, employeeInfo);
		}
		
		sheet = secondHalf.getSheetAt(0);
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			String employeeNumber = row.getCell(positions.get("empno")).
					getStringCellValue();
			double[] employeeInfo = getEmployeeInfo(row);
			
			if (info.containsKey(employeeNumber)) {
				double[] firstHalfInfo = info.get(employeeNumber);
				employeeInfo[0] += firstHalfInfo[0];
				employeeInfo[1] += firstHalfInfo[1];
				employeeInfo[2] += firstHalfInfo[2] - 10;
				employeeInfo[3] += firstHalfInfo[3];
				employeeInfo[4] = firstHalfInfo[4];
				employeeInfo[5] += firstHalfInfo[5];
				info.remove(employeeNumber);
			}
			
			info.put(employeeNumber, employeeInfo);
		}
	}
	
	private double[] getEmployeeInfo(Row row) {
		double[] employeeInfo = {
				getNumericValue(row, "grosspay"),
				getNumericValue(row, "sss"),
				getSSS(getNumericValue(row, "grosspay"))[1],
				getNumericValue(row, "philhealth"),
				getNumericValue(row, "pagibig"),
				getNumericValue(row, "days")
		};
		
		return employeeInfo;
	}
	
	public void calculateBenefits() {
		Map<String, Object[]> rows = new TreeMap<String, Object[]>();
		int index = 2;
		
		rows.put("1", new Object[] {
			"empno",
			"days worked",
			"gross pay",
			"employee sss",
			"employer sss",
			"government sss",
			"employee philhealth",
			"employer philhealth",
			"government philhealth",
			"employee pagibig",
			"employer pagibig",
			"government pagibig",
		});
		
		for (String employeeNumber : info.keySet()) {
			double[] employeeInfo = info.get(employeeNumber);
			double employerSSS = employeeInfo[2];
			double employerPhilhealth = employeeInfo[3];
			double employerPagibig = employeeInfo[4];
			
			if (sssType == BenefitsRateTypes.FIXED) {
				employerSSS = employeeInfo[5] * sssAmount;
			}
			
			if (philhealthType == BenefitsRateTypes.FIXED) {
				employerPhilhealth = employeeInfo[5] * philhealthAmount;
				employerPhilhealth = Math.max(employerPhilhealth, 100.0);
			}

			if (pagibigType == BenefitsRateTypes.FIXED) {
				employerPagibig = employeeInfo[5] * pagibigAmount;
				employerPagibig = Math.max(employerPagibig, 100.0);
			}
			
			Object[] data = {
					employeeNumber,
					employeeInfo[5],
					employeeInfo[0],
					employeeInfo[1],
					employerSSS,
					getGovernmentSSS(employeeInfo[0], employeeInfo[1], 
							employerSSS),
					employeeInfo[3],
					employerPhilhealth,
					getGovernmentPhilhealth(employeeInfo[0], employeeInfo[3],
							employerPhilhealth),
					employeeInfo[4],
					employerPagibig,
					employeeInfo[4] + employerPagibig
			};
			
			rows.put(Integer.toString(index), data);
			index++;
		}

		Helper.writeWorkbook(rows, "out/benefits.xlsx");
	}
	
	private double getGovernmentSSS(double salary, double employeeSSS,
			double employerSSS) {
		double[] sss = getSSS(salary);
		double totalSSS = employeeSSS + employerSSS;
		double governmentSSS = sss[0] +sss[1];
		
		while (governmentSSS > totalSSS) {
			sss = getSSS(salary - 500.0);
			salary -= 500.0;
			governmentSSS = sss[0] + sss[1];
		}
		
		return governmentSSS;
	}
	
	private double getGovernmentPhilhealth(double salary, 
			double employeePhilhealth, double employerPhilhealth) {
		double philhealth = getPhilHealth(salary) * 2.0;
		double totalPhilhealth = employeePhilhealth + employerPhilhealth;
		
		while (philhealth > totalPhilhealth) {
			philhealth = getPhilHealth(salary - 1000.0) * 2.0;
			salary -= 1000.0;
		}
		
		return philhealth;
	}
	
	private double getNumericValue(Row row, String header) {
		if (positions.get(header) != null) {
			return row.getCell(positions.get(header)).getNumericCellValue();
		}
		
		return 0;
	}
	
	private double[] getSSS(double salary) {
		double[][] salaryRanges = Constants.SSS_RANGES;
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return new double[] {
						salaryRanges[i][1],
						salaryRanges[i][2]
				};
			}
		}
		
		return new double[] {
				581.3, 1208.7
		};
	}
	
	private double getPhilHealth(double salary) {
		double[][] salaryRanges = Constants.PHILHEALTH_RANGES;
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 437.50;
	}
	
	public void setFixedSSS(double amount) {
		sssType = BenefitsRateTypes.FIXED;
		sssAmount = amount;
	}
	
	public void setFixedPhilhealth(double amount) {
		philhealthType = BenefitsRateTypes.FIXED;
		philhealthAmount = amount;
	}
	
	public void setFixedPagibig(double amount) {
		pagibigType = BenefitsRateTypes.FIXED;
		pagibigAmount = amount;
	}
}
