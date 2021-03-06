package deduction;

import helper.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import variable.Constants;

public class Deduction {
	private PreparedStatement getStatement;
	private PreparedStatement updateStatement;
	private Map<String, Object[]> rows;
	private int rowIndex;
	
	public Deduction(Connection connection) throws SQLException {
		getStatement = connection.prepareStatement(Constants.GET_DEDUCTIONS);
		updateStatement = connection.prepareStatement(Constants.UPDATE_DEDUCTIONS);
		rows = new TreeMap<String, Object[]>();
		
		Object[] headers = {
				"employee number",
				"deduction short code",
				"old principal",
				"amount deducted",
				"new principal"
		};
		
		rows.put("1", headers);
		rowIndex = 2;
	}
	
	public HashMap<String, Double> applyDeductions(String employeeNumber, double salary) 
			throws SQLException {
		HashMap<String, Double> deductionMap = new HashMap<String, Double>();
		getStatement.setString(1, employeeNumber);
		ResultSet deductions = getStatement.executeQuery();
		double deductionSum = 0;
		
		while (deductions.next()) {
			int id = deductions.getInt(1);
			String employeeName = deductions.getString(3);
			double principal = deductions.getDouble(4);
			double amortization  = deductions.getDouble(5);
			String deductionShortCode = deductions.getString(6);
			
			if (principal < amortization) {
				amortization = principal;
			}
			
			double extraSalary = salary - deductionSum - Constants.MINIMUM_SALARY;
			
			if (amortization > 0 && extraSalary > 0) {
				if (amortization >= extraSalary) {
					amortization = extraSalary;
				}
				
				double newPrincipal = principal - amortization;
				
				Object[] data = {
						employeeNumber,
						deductionShortCode,
						principal,
						amortization,
						newPrincipal
				};
				
				deductionMap.put(deductionShortCode, amortization);
				deductionSum += amortization;
				rows.put(Integer.toString(rowIndex), data);
				rowIndex++;
				
				updateStatement.setInt(6, id);
				updateStatement.setString(1, employeeNumber);
				updateStatement.setString(2, employeeName);
				updateStatement.setDouble(3, newPrincipal);
				updateStatement.setDouble(4, amortization);
				updateStatement.setString(5, deductionShortCode);
				updateStatement.execute();
			}
		}
		
		return deductionMap;
	}
	
	public void writeDeduction() {
		Helper.writeWorkbook(rows, "out/deductions.xlsx");
	}
}
