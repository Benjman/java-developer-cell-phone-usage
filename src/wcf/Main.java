package wcf;

import wcf.dal.InMemoryEmployeeRepository;
import wcf.dal.InMemoryPhoneHistoryRepository;
import wcf.entity.Employee;
import wcf.entity.PhoneSession;
import wcf.service.EmployeeService;
import wcf.service.PhoneService;
import wcf.service.ReportService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

class Main {
	private static ReportService reportService;

	private static void initEmployeeData() throws IOException {
		final EmployeeService employeeService = EmployeeService.initInstance(InMemoryEmployeeRepository.getInstance());
		final List<Employee> employees = Utils.EmployeesCsvDeserializer.deserialize("res/CellPhone.csv", "yyyyMMdd");
		for (final Employee employee : employees) {
			employeeService.update(employee);
		}
	}

	private static void initPhoneData() throws IOException {
		final PhoneService phoneService = PhoneService.initInstance(InMemoryPhoneHistoryRepository.getInstance());
		final List<PhoneSession> phoneSessions = Utils.PhoneUsageDeserializer.deserialize("res/CellPhoneUsageByMonth.csv", "MM/dd/yyyy");
		for (final PhoneSession session : phoneSessions) {
			phoneService.addSession(session);
		}
	}

	public static void main(String[] args) throws Exception {
		initEmployeeData();
		initPhoneData();

		reportService = new ReportService();
		final Date start = new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime();
		final Date end = new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();
		final String reportData = reportService.generatePhoneUsageReport(start, end);
		System.out.println(reportData);
		wcf.service.PrintService.print(reportData.getBytes());
	}

}
