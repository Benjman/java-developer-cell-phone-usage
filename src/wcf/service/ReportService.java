package wcf.service;

import wcf.entity.Employee;
import wcf.entity.EntityNotFoundException;
import wcf.entity.PhoneSession;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

	private final EmployeeService employeeService;
	private final PhoneService phoneService;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	private final Calendar calendar = Calendar.getInstance();

	public ReportService() {
		employeeService = EmployeeService.getInstance();
		phoneService = PhoneService.getInstance();
	}

	/**
	 * Generates an ASCII string to represent employee phone usage through a given period.
	 *
	 * @param start Start date of report
	 * @param end   End date of report
	 * @return ASCII data for report
	 */
	public String generatePhoneUsageReport(Date start, Date end) {
		final StringBuilder stringBuilder = new StringBuilder();
		final Map<Employee, float[]> employeeUsages = new HashMap<>(); // float buffer: 0-11 is the minutes used, 12-23 is the data used.
		final List<PhoneSession> sessions = phoneService.getForRange(start, end);
		sessions.sort(Comparator.comparing(a -> a.date)); // sort ascending by date
		generateHeader(stringBuilder, sessions, employeeUsages);
		populateEmployeeMonthlyStatistics(sessions, employeeUsages);
		printTable(stringBuilder, employeeUsages);
		return stringBuilder.toString();
	}

	private void printTable(final StringBuilder stringBuilder, final Map<Employee, float[]> employeeUsages) {
		final int columnCount = 16;
		String[] columns = new String[columnCount];
		columns[0] = "Employee Id";
		columns[1] = "Employee Name";
		columns[2] = "Model";
		columns[3] = "Purchase Date";
		final SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM yyyy");
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 0; i < 12; i++) {
			calendar.set(Calendar.MONTH, i);
			final String monthStr = monthFormatter.format(calendar.getTime());
			columns[4 + i] = monthStr/* + " Minutes"*/;
//			columns[4 + i + 12] = monthStr + " Data";
		}
		int rightPad = 3;
		int[] columnWidths = new int[columns.length];
		String[] values = new String[columnCount * employeeUsages.size()];
		int index = 0;
		for (final Employee employee : employeeUsages.keySet()) {
			values[index * columnCount] = String.valueOf(employee.id);
			values[index * columnCount + 1] = employee.name;
			values[index * columnCount + 2] = employee.phoneModel;
			values[index * columnCount + 3] = dateFormat.format(employee.phonePurchaseDate);
			columnWidths[0] = Math.max(columnWidths[0], values[index * columnCount].length());
			columnWidths[1] = Math.max(columnWidths[1], values[index * columnCount + 1].length());
			columnWidths[2] = Math.max(columnWidths[2], values[index * columnCount + 2].length());
			columnWidths[3] = Math.max(columnWidths[3], values[index * columnCount + 3].length());
			for (int i = 0; i < 12; i++) {
				final float[] monthlyUsage = employeeUsages.get(employee);
				final int usageIndex = index * columnCount + 4 + i;
				values[usageIndex] = (int) monthlyUsage[i] + " / " + String.format("%.2f", monthlyUsage[12 + i]);
				columnWidths[4 + i] = Math.max(columnWidths[4 + i], values[usageIndex].length());
			}
			index++;
		}
		addHr(stringBuilder);

		// get column widths
		for (int i = 0; i < columns.length; i++) {
			columnWidths[i] = Math.max(columnWidths[i], columns[i].length()) + rightPad;
		}

		// print usage header
		int usageXOffset = 0;
		for (int i = 0; i < 4; i++) {
			usageXOffset += columnWidths[i];
		}
		final String usageLabel = "Usage (Minutes / Data)";
		stringBuilder.append(String.format("%1$" + (usageXOffset + usageLabel.length()) + "s", usageLabel)).append("\n");

		for (int i = 0; i < columns.length; i++) {
			stringBuilder.append(String.format("%1$-" + columnWidths[i] + "s", columns[i]));
		}
		stringBuilder.append("\n");
		index = 0;
		for (int i = 0; i < values.length; i++) {
			stringBuilder.append(String.format("%1$-" + columnWidths[i % columnCount] + "s", values[i]));
			if (++index % columnCount == 0) {
				stringBuilder.append("\n");
			}
		}
		stringBuilder.append("\n");
	}

	private void populateEmployeeMonthlyStatistics(final List<PhoneSession> sessions, final Map<Employee, float[]> employeeUsages) {
		for (final PhoneSession session : sessions) {
			calendar.setTime(session.date);
			final int month = calendar.get(Calendar.MONTH);
			final float[] values = employeeUsages.get(employeeUsages.keySet().stream().filter(employee -> employee.id == session.employeeId).findFirst().get());
			values[month] += session.totalMinutes;
			values[month + 12] += session.totalDate;
		}
	}

	private void addHr(final StringBuilder stringBuilder) {
		int lineLength = 0;
		for (final String s : stringBuilder.toString().split("\n")) {
			lineLength = Math.max(lineLength, s.length());
		}
		stringBuilder.append(new String(new char[lineLength]).replace("\0", "="))
				.append("\n\n");
	}

	private void generateHeader(final StringBuilder stringBuilder, final List<PhoneSession> sessions, final Map<Employee, float[]> employees) {
		int totalMinutes = 0;
		float totalData = 0f;
		for (final PhoneSession session : sessions) {
			try {
				totalMinutes += session.totalMinutes;
				totalData += session.totalDate;
				boolean employeeFound = false;
				for (final Employee employee : employees.keySet()) {
					if (employeeFound = employee.id == session.employeeId) break;
				}
				if (!employeeFound) {
					employees.put(employeeService.get(session.employeeId), new float[24]);
				}
			} catch (EntityNotFoundException e) {
				System.err.println("Failed to find employee by id " + session.employeeId);
			}
		}

		float averageMinutes = (float) totalMinutes / (float) sessions.size(),
				averageData = totalData / (float) sessions.size();

		int rightPad = 3;
		String[] columns = {"Report Run Date", "Number of Phones", "Total Minutes", "Total Data", "Average Minutes", "Average Data"};
		String[] values = {
				dateFormat.format(new Date()),
				String.valueOf(employees.size()),
				String.valueOf(totalMinutes),
				String.valueOf(String.format("%.2f", totalData)),
				String.valueOf(String.format("%.2f", averageMinutes)),
				String.valueOf(String.format("%.4f", averageData))
		};
		int[] columnWidths = new int[columns.length];

		for (int i = 0; i < columns.length; i++) {
			columnWidths[i] = Math.max(columns[i].length(), values[i].length()) + rightPad;
			stringBuilder.append(String.format("%1$-" + columnWidths[i] + "s", columns[i]));
		}
		stringBuilder.append("\n");
		for (int i = 0; i < values.length; i++) {
			stringBuilder.append(String.format("%1$-" + columnWidths[i] + "s", values[i]));
		}
		stringBuilder.append("\n");
	}

}
