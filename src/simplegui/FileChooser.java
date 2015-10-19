package simplegui;

import helper.PayrollHalfTypes;
import helper.PayrollPeriodTypes;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import mosys.Test;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import payroll.Payroll;
import variable.Modules;
import billing.Benefits;

import java.io.File; 
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class FileChooser {
	public static void main(final String[] args) throws Exception {
		TestDatabase tdb = new TestDatabase();
		Connection connection = tdb.loadDatabase();
		final JFrame parent = new JFrame();
        Object[] options = {"Calculate Benefits",
                "Calculate Payroll",
                "Insert Deductions"};
		int n = JOptionPane.showOptionDialog(
				parent,
				"What do you want to do?",
				"Simple Interface",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
		
		if (n == 1) {
			options = new Object[] {"Second", "First"};
			int halfOption = JOptionPane.showOptionDialog(
					parent,
					"Is this first half or second half?",
					"Payroll Options",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			PayrollHalfTypes half;
			if (halfOption == 0) {
				half = PayrollHalfTypes.SECOND;
			}
			else half = PayrollHalfTypes.FIRST;
			
			options = new Object[] {"Hourly", "Special Monthly", "Monthly", "Daily"};
			int periodOption = JOptionPane.showOptionDialog(
					parent,
					"What is the time period?",
					"Payroll Options",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[3]);
			PayrollPeriodTypes period;
			if (periodOption == 0) {
				period = PayrollPeriodTypes.HOURLY;
			}
			else if (periodOption == 1) {
				period = PayrollPeriodTypes.SPECIAL_MONTHLY;
			}
			else if (periodOption == 2) {
				period = PayrollPeriodTypes.MONTHLY;
			}
			else period = PayrollPeriodTypes.DAILY;
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File file = fileChooser.getSelectedFile();
			    payroll(WorkbookFactory.create(file), connection, period, half);
			}
		}
		else if (n == 0) {
			JOptionPane.showMessageDialog(parent, "Select first half payroll.");
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(parent);
			File firstHalf = null;
			if (result == JFileChooser.APPROVE_OPTION) {
			    firstHalf = fileChooser.getSelectedFile();
			}
			
			JOptionPane.showMessageDialog(parent, "Select second half payroll.");
			
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			result = fileChooser.showOpenDialog(parent);
			File secondHalf = null;
			if (result == JFileChooser.APPROVE_OPTION) {
				secondHalf = fileChooser.getSelectedFile();
			}
			
			double sssAmount = -1;
			double philhealthAmount = -1;
			double pagibigAmount = -1;
			
			options = new Object[] {"Fixed", "Bracket"};
			int bracketType = JOptionPane.showOptionDialog(
					parent,
					"Is SSS fixed or based on the employee's salary bracket?",
					"Benefits Options",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			if (bracketType == 0) {
				sssAmount = Double.parseDouble(JOptionPane.showInputDialog(
						parent,
						"What is the daily SSS rate paid by the employer?"));
			}
			
			bracketType = JOptionPane.showOptionDialog(
					parent,
					"Is Philhealth fixed or based on the employee's salary bracket?",
					"Benefits Options",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			if (bracketType == 0) {
				philhealthAmount = Double.parseDouble(JOptionPane.showInputDialog(
						parent,
						"What is the daily Philhealth rate paid by the employer?"));
			}
			
			bracketType = JOptionPane.showOptionDialog(
					parent,
					"Is Pagibig fixed or based on the employee's salary bracket?",
					"Benefits Options",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			if (bracketType == 0) {
				pagibigAmount = Double.parseDouble(JOptionPane.showInputDialog(
						parent,
						"What is the daily Pagibig rate paid by the employer?"));
			}
			
			if (firstHalf != null & secondHalf != null) {
				benefits(WorkbookFactory.create(firstHalf), WorkbookFactory.create(secondHalf), connection,
						sssAmount, philhealthAmount, pagibigAmount);
			}
		}
		else if (n == 2) {
			JOptionPane.showMessageDialog(parent, "Select deductions input.");
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(parent);
			File file = null;
			if (result == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
			
			if (file != null) {
				Workbook workbook = WorkbookFactory.create(file);
				Test.dataEntry(workbook, connection, Modules.EMPLOYEE_DEDUCTION);
			}
		}
		parent.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		System.exit(0);
    }
	
	public static void payroll(Workbook workbook, Connection connection, PayrollPeriodTypes period,
			PayrollHalfTypes half) throws SQLException {
		Payroll payroll = new Payroll(workbook, connection, period, half);
		payroll.calculatePayroll();
	}
	
	public static void benefits(Workbook firstHalf, Workbook secondHalf, Connection connection,
			double sssAmount, double philhealthAmount, double pagibigAmount) 
			throws SQLException {
		Benefits benefits = new Benefits(firstHalf, secondHalf, connection);
		
		if (sssAmount > 0) {
			benefits.setFixedSSS(sssAmount);
		}
		
		if (philhealthAmount > 0) {
			benefits.setFixedPhilhealth(philhealthAmount);
		}
		
		if (pagibigAmount > 0) {
			benefits.setFixedPagibig(pagibigAmount);
		}
		
		benefits.getInfo();
		benefits.calculateBenefits();
	}
}
