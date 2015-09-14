package payroll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import helper.Helper;
import helper.PayrollHalfTypes;
import helper.PayrollPeriodTypes;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import variable.Constants;
import deduction.Deduction;

public class Payroll {
	private Workbook workbook;
	private Connection connection;
	private PayrollPeriodTypes period;
	private PayrollHalfTypes half;
	private Deduction deduction;
	private Map<String, Integer> positions;
	
	public Payroll(Workbook workbook, Connection connection, PayrollPeriodTypes period,
			PayrollHalfTypes half)
			throws SQLException {
		this.workbook = workbook;
		this.connection = connection;
		this.period = period;
		this.half = half;
		this.deduction = new Deduction(connection);
		positions = new HashMap<String, Integer>();
		
		findPositions();
	}
	
	public void findPositions() {
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);
		
		for (int i = 0; i < row.getLastCellNum(); i++) {
			String entry = row.getCell(i).getStringCellValue().trim().toLowerCase();
			positions.put(entry, i);
		}
	}
	
	public void calculatePayroll() throws SQLException {
		Map<String, Object[]> rows = new TreeMap<String, Object[]>();
		HashSet<Integer> filledColumns = new HashSet<Integer>();
		Sheet sheet = workbook.getSheetAt(0);
		rows.put("1", Constants.HEADERS);
		
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			double rate = getNumericValue(row, "rate");
			double days = getNumericValue(row, "days");
			double basic = rate * days;
			double cola = getNumericValue(row, "cola");
			double sea = getNumericValue(row, "sea");
			double ctpa = getNumericValue(row, "ctpa");
			double divisor = period.getDivisor();
			double allRate = rate + cola + sea + ctpa;
			double dividedAllRate = allRate / divisor;
			double dividedRate = rate / divisor;
			double dividedCSC = (cola + sea + ctpa) / divisor;

			Object[] data1 = calculateData(row, basic, days, dividedCSC, 
					dividedRate, dividedAllRate, allRate);
			double sum = 0;
			
			for (int j = 4; j < data1.length; j += 2) {
				sum += (Double) data1[j];
			}
			
			double sss = getSSS(sum);
			double philhealth = getPhilHealth(sum);
			double pagibig = getPagibig();
			double leftoverSum = sum - sss - philhealth - pagibig;
			double deductions = deduction.applyDeductions(row.getCell(positions.get("empno")).
					getStringCellValue().trim(), leftoverSum);
			
			Object[] data2 = {
					sum,
					sss,
					philhealth,
					pagibig,
					deductions,
					sum - sss - philhealth - pagibig - deductions 
			};
			
			Object[] data = concatenate(data1, data2);
			rows.put(Integer.toString(i + 1), data);
			
			for (int j = 0; j < data.length; j++) {
				if (data[j] instanceof String ||
						(data[j] instanceof Double && !data[j].equals(0.0))) {
					filledColumns.add(j);
				}
			}
		}
		
		rows = Helper.removeEmptyColumns(rows, filledColumns);
		Helper.writeWorkbook(rows, "out/payroll.xlsx");
		deduction.writeDeduction();
	}
	
	private Object[] calculateData(Row row, double basic, double days, 
			double dividedCSC, double dividedRate, double dividedAllRate,
			double allRate) {
		double legalHolidaySum = (days * 8.0 - getNumericValue(row, "hours1") + 
				getNumericValue(row, "dod3") + getNumericValue(row, "splhol3") + 
				getNumericValue(row, "lglhol4")) / 8.0;
		double allowanceSum = (days * 8.0 - getNumericValue(row, "hours1") + 
				getNumericValue(row, "dod3") + getNumericValue(row, "splhol3") + 
				getNumericValue(row, "lglhol4")) / period.getDivisor();
		double shortageSum = (days * 8.0 - getNumericValue(row, "hours1") + 
				getNumericValue(row, "dod2") + getNumericValue(row, "splholrd2") + 
				getNumericValue(row, "lglhol4") + getNumericValue(row, "lglholrd4"))
				/ 8.0;
		
		if (period == PayrollPeriodTypes.MONTHLY || 
				period == PayrollPeriodTypes.SPECIAL_MONTHLY) {
			basic = allRate / 2.0; // -absent, but where is that, also needed for monthlyallow
		}
		
		if (period == PayrollPeriodTypes.DAILY) {
			legalHolidaySum = (getNumericValue(row, "lglhol3") + 
					getNumericValue(row, "lglhol4") + getNumericValue(row, "lglholrd3") + 
					getNumericValue(row, "lglholrd4")) / 8.0;
			allowanceSum = (days * 8.0) - getNumericValue(row, "hours1") + 
					getNumericValue(row, "dod2") + getNumericValue(row, "splhol2") + 
					getNumericValue(row, "splholrd2") / 8.0 ;
			shortageSum = (days * 8.0 - getNumericValue(row, "hours1") + 
					getNumericValue(row, "dod2") + getNumericValue(row, "splhol2") + 
					getNumericValue(row, "splholrd2") + getNumericValue(row, "lglhol4") + 
					getNumericValue(row, "lglholrd4")) / 8.0;
		}
		
		Object[] data = {
				row.getCell(positions.get("empno")).getStringCellValue(),
				row.getCell(positions.get("name")).getStringCellValue(),
				row.getCell(positions.get("rate")).getNumericCellValue(),
				getNumericValue(row, "days"),
				basic,
				getNumericValue(row, "cola"),
				getNumericValue(row, "cola") * days,
				getNumericValue(row, "sea"),
				getNumericValue(row, "sea") * days,
				getNumericValue(row, "ctpa"),
				getNumericValue(row, "ctpa") * days,
				getNumericValue(row, "cola1"),
				getNumericValue(row, "cola1") * allowanceSum,
				getNumericValue(row, "sea1"),
				getNumericValue(row, "sea1") * allowanceSum,
				getNumericValue(row, "ctpa1"),
				getNumericValue(row, "ctpa1") * allowanceSum,
				getNumericValue(row, "lglholcola"),
				getNumericValue(row, "cola1") * legalHolidaySum,
				getNumericValue(row, "lglholsea"),
				getNumericValue(row, "sea1") * legalHolidaySum,
				getNumericValue(row, "lglholctpa"),
				getNumericValue(row, "ctpa1") * legalHolidaySum,
				getNumericValue(row, "minutes"),
				getNumericValue(row, "minutes") * dividedAllRate / 60.0 * -1.0,
				getNumericValue(row, "hours"),
				getNumericValue(row, "hours") * dividedAllRate * -1.0,
				getNumericValue(row, "hours1"),
				getNumericValue(row, "hours1") * dividedRate * -1.0,
				getNumericValue(row, "excess"),
				getNumericValue(row, "excess") * dividedRate * 1.25,
				getNumericValue(row, "ndreg"),
				getNumericValue(row, "ndreg") * dividedRate * 0.1,
				getNumericValue(row, "ndot"),
				getNumericValue(row, "ndot") * dividedRate * 0.1 * 1.25,
				getNumericValue(row, "dod1"),
				getNumericValue(row, "dod1") * dividedRate * 0.3,
				getNumericValue(row, "dod2"),
				getNumericValue(row, "dod2") * dividedRate * 1.3,
				getNumericValue(row, "dod3"),
				getNumericValue(row, "dod3") * dividedRate * 1.3 + 
					getNumericValue(row, "dod3") * dividedCSC,
				getNumericValue(row, "dodexcess"),
				getNumericValue(row, "dodexcess") * dividedRate * 1.3 * 1.3,
				getNumericValue(row, "dodndreg"),
				getNumericValue(row, "dodndreg") * dividedRate * 0.1 * 1.3,
				getNumericValue(row, "dodndot"),
				getNumericValue(row, "dodndot") * dividedRate * 0.1 * 1.3 * 1.3,
				getNumericValue(row, "splhol1"),
				getNumericValue(row, "splhol1") * dividedRate * 0.3,
				getNumericValue(row, "splhol2"),
				getNumericValue(row, "splhol2") * dividedRate * 1.3,
				getNumericValue(row, "splhol3"),
				getNumericValue(row, "splhol3") * dividedRate * 1.3 + 
					getNumericValue(row, "splhol3") * dividedCSC,
				getNumericValue(row, "splholexcess"),
				getNumericValue(row, "splholexcess") * dividedRate * 1.3 * 1.3,
				getNumericValue(row, "splholndreg"),
				getNumericValue(row, "splholndreg") * dividedRate * 0.1 * 1.3,
				getNumericValue(row, "splholndot"),
				getNumericValue(row, "splholndot") * dividedRate * 0.1 * 1.3 * 1.3,
				getNumericValue(row, "splholrd1"),
				getNumericValue(row, "splholrd1") * dividedRate * 0.5,
				getNumericValue(row, "splholrd2"),
				getNumericValue(row, "splholrd2") * dividedRate * 1.5,
				getNumericValue(row, "splholrd3"),
				getNumericValue(row, "splholrd3") * dividedRate * 1.5 + 
					getNumericValue(row, "splholrd3") * dividedCSC,
				getNumericValue(row, "splholrdexcess"),
				getNumericValue(row, "splholrdexcess") * dividedRate * 1.5 * 1.3,
				getNumericValue(row, "splholrdndreg"),
				getNumericValue(row, "splholrdndreg") * dividedRate * 0.1 * 1.5,
				getNumericValue(row, "splholrdndot"),
				getNumericValue(row, "splholrdndot") * dividedRate * 0.1 * 1.5 * 1.3,
				getNumericValue(row, "lglhol1"),
				getNumericValue(row, "lglhol1") * dividedAllRate,
				getNumericValue(row, "lglhol2"),
				getNumericValue(row, "lglhol2") * dividedAllRate * 2.0,
				getNumericValue(row, "lglhol3"),
				getNumericValue(row, "lglhol3") * dividedRate,
				getNumericValue(row, "lglhol4"),
				getNumericValue(row, "lglhol4") * dividedRate,
				getNumericValue(row, "lglholexcess"),
				getNumericValue(row, "lglholexcess") * dividedRate * 2.6,
				getNumericValue(row, "lglholndreg"),
				getNumericValue(row, "lglholndreg") * dividedRate * 0.1 * 2.0,
				getNumericValue(row, "lglholndot"),
				getNumericValue(row, "lglholndot") * dividedRate * 0.1 * 2.6,
				getNumericValue(row, "lglholrd1"),
				getNumericValue(row, "lglholrd1") * dividedAllRate * 1.6,
				getNumericValue(row, "lglholrd2"),
				getNumericValue(row, "lglholrd2") * dividedAllRate * 2.6,
				getNumericValue(row, "lglholrd3"),
				getNumericValue(row, "lglholrd3") * dividedRate * 1.6,
				getNumericValue(row, "lglholrd4"),
				getNumericValue(row, "lglholrd4") * dividedRate * 2.6,
				getNumericValue(row, "lglholrdexcess"),
				getNumericValue(row, "lglholrdexcess") * dividedRate * 2.6 * 1.3,
				getNumericValue(row, "lglholrdndreg"),
				getNumericValue(row, "lglholrdndreg") * dividedRate * 0.1 * 2.6,
				getNumericValue(row, "lglholrdndot"),
				getNumericValue(row, "lglholrdndot") * dividedRate * 0.1 * 2.6 * 1.3,
				getNumericValue(row, "shortallow"),
				getNumericValue(row, "shortallow") * shortageSum,
				getNumericValue(row, "monthlyallow"),
				getNumericValue(row, "monthlyallow")
		};
		
		return data;
	}
	
	private double getNumericValue(Row row, String header) {
		if (positions.get(header) != null) {
			return row.getCell(positions.get(header)).getNumericCellValue();
		}
		
		return 0;
	}
	
	private double getSSS(double salary) {
		double[][] salaryRanges = {
				{ 1249.99, 36.3, 83.7 },
				{ 1749.99, 54.5, 120.5 },
				{ 2249.99, 72.7, 157.3 },
				{ 2749.99, 90.8, 194.2 },
				{ 3249.99, 109, 231 },
				{ 3749.99, 127.2, 267.8 },
				{ 4249.99, 145.3, 304.7 },
				{ 4749.99, 163.5, 341.5 },
				{ 5249.99, 181.7, 378.3 },
				{ 5749.99, 199.8, 415.2 },
				{ 6249.99, 218, 452 },
				{ 6749.99, 236.2, 488.8 },
				{ 7249.99, 254.3, 525.7 },
				{ 7749.99, 272.5, 562.5 },
				{ 8249.99, 290.7, 599.3 },
				{ 8749.99, 308.8, 636.2 },
				{ 9249.99, 327, 673 },
				{ 9749.99, 345.2, 709.8 },
				{ 10249.99, 363.3, 746.7 },
				{ 10749.99, 381.5, 783.5 },
				{ 11249.99, 399.7, 820.3 },
				{ 11749.99, 417.8, 857.2 },
				{ 12249.99, 436, 894 },
				{ 12749.99, 454.2, 930.8 },
				{ 13249.99, 472.3, 967.7 },
				{ 13749.99, 490.5, 1004.5 },
				{ 14249.99, 508.7, 1041.3 },
				{ 14749.99, 526.8, 1078.2 },
				{ 15249.99, 545, 1135 },
				{ 15749.99, 563.2, 1171.8 },
				{ Double.MAX_VALUE, 581.3, 1208.7 }
		};
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 581.3;
	}
	
	private double getPhilHealth(double salary) {
		double[][] salaryRanges = {
				{ 8999.99, 100.00 },
				{ 9999.99, 112.50 },
				{ 10999.99, 125.00 },
				{ 11999.99, 137.50 },
				{ 12999.99, 150.00 },
				{ 13999.99, 162.50 },
				{ 14999.99, 175.00 },
				{ 15999.99, 187.50 },
				{ 16999.99, 200.00 },
				{ 17999.99, 212.50 },
				{ 18999.99, 225.00 },
				{ 19999.99, 237.50 },
				{ 20999.99, 250.00 },
				{ 21999.99, 262.50 },
				{ 22999.99, 275.00 },
				{ 23999.99, 287.50 },
				{ 24999.99, 300.00 },
				{ 25999.99, 312.50 },
				{ 26999.99, 325.00 },
				{ 27999.99, 337.50 },
				{ 28999.99, 350.00 },
				{ 29999.99, 362.50 },
				{ 30999.99, 375.00 },
				{ 31999.99, 387.50 },
				{ 32999.99, 400.00 },
				{ 33999.99, 412.50 },
				{ 34999.99, 425.00 },
				{ Double.MAX_VALUE, 437.50 }
		};
		
		for (int i = 0; i < salaryRanges.length; i++) {
			if (salary < salaryRanges[i][0]) {
				return salaryRanges[i][1];
			}
		}
		
		return 437.50;
	}
	
	private double getPagibig() {
		if (half == PayrollHalfTypes.FIRST) {
			return 100.0;
		}
		else return 0; // TODO: add a check to see if they're in first half, if not, make 100
	}
	
	private Object[] concatenate(Object[] a, Object[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   Object[] c= new Object[aLen + bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}
}
