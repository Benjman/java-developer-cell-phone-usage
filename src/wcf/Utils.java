package wcf;

import wcf.entity.Employee;
import wcf.entity.PhoneSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class Utils {

	public static String getFileSource(final String pathStr) throws IOException {
		return new String(Files.readAllBytes(Paths.get(pathStr)));
	}

	/**
	 * @param lines Collection to search.
	 * @param index Index to start at, and increment.
	 * @return Next non-blank line, or null.
	 */
	private static String getNextNonBlankLine(final String[] lines, final AtomicInteger index) {
		do {
			if (lines.length <= index.get()) {
				return null;
			}
		} while (lines[index.getAndIncrement()].isBlank());
		return lines[index.get() - 1];
	}

	public static final class EmployeesCsvDeserializer {

		public static List<Employee> deserialize(final String path, final String dateFormat) throws IOException {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			final String source = getFileSource(path);
			String[] lines = source.split("\r");
			AtomicInteger index = new AtomicInteger();
			String line;
			// Do we have file source, and does the source have any contents:
			if (source.isBlank()
					|| (line = getNextNonBlankLine(lines, index)) == null) return Collections.emptyList();
			List<Employee> employees = new ArrayList<>();
			String[] fieldOrder = line.split(",");
			while ((line = getNextNonBlankLine(lines, index)) != null) {
				int id = -1;
				String name = null, model = null;
				Date purchase = null;
				final String[] values = line.split(",");
				if (values.length != fieldOrder.length) throw new IllegalStateException("Invalid number of values");
				for (int i = 0; i < fieldOrder.length; i++) {
					final String field = fieldOrder[i],
							value = values[i];
					switch (field.toLowerCase()) {
						case "employeeid":
							id = Integer.parseInt(value.replaceAll("[^0-9]", ""));
							break;
						case "employeename":
							name = value;
							break;
						case "purchasedate":
							try {
								// TODO convert to utc
								purchase = formatter.parse(value);
							} catch (ParseException e) {
								throw new InvalidParameterException(String.format("Purchase date format unexpected. \nExpected: %s\nWas: %s", formatter.toPattern(), field));
							}
							break;
						case "model":
							model = value;
							break;
						default:
							// TODO logging
							System.err.printf("Unexpected field: %s\n", value);
					}
				}
				employees.add(new Employee(id, name, purchase, model));
			}
			return employees;
		}

	}

	public static final class PhoneUsageDeserializer {

		public static List<PhoneSession> deserialize(final String path, final String dateFormat) throws IOException {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			final String source = getFileSource(path);
			String[] lines = source.split("\n");
			AtomicInteger index = new AtomicInteger();
			String line;
			// Do we have file source, and does the source have any contents:
			if (source.isBlank()
					|| (line = getNextNonBlankLine(lines, index)) == null) return Collections.emptyList();
			List<PhoneSession> sessions = new ArrayList<>();
			String[] fieldOrder = line.split(",");
			while ((line = getNextNonBlankLine(lines, index)) != null) {
				int id = -1;
				Date date = null;
				int minutes = 0;
				float data = 0f;
				final String[] values = line.split(",");
				if (values.length != fieldOrder.length) throw new IllegalStateException("Invalid number of values");
				for (int i = 0; i < fieldOrder.length; i++) {
					final String field = fieldOrder[i],
							value = values[i];
					switch (field.toLowerCase()) {
						case "emplyeeid":
							try {
								id = Integer.parseInt(value.replaceAll("[^0-9]", ""));
							} catch (NumberFormatException e) {
								throw new NumberFormatException(String.format("employeeId format was unexpected. Expected integer, received: %s", value));
							}
							break;
						case "date":
							try {
								date = formatter.parse(value);
							} catch (ParseException e) {
								throw new InvalidParameterException(String.format("Date format unexpected. \nExpected: %s\nWas: %s", formatter.toPattern(), field));
							}
							break;
						case "totalminutes":
							try {
								minutes = Integer.parseInt(value.replaceAll("[^0-9]", ""));
							} catch (NumberFormatException e) {
								throw new NumberFormatException(String.format("totalMinutes format was unexpected. Expected integer, received: %s", value));
							}
							break;
						case "totaldata":
							try {
								data = Float.parseFloat(value.replaceAll("[^0-9.]", ""));
							} catch (NumberFormatException e) {
								throw new NumberFormatException(String.format("totalData format was unexpected. Expected float, received: %s", value));
							}
					}
				}
				sessions.add(new PhoneSession(id, date, minutes, data));
			}
			return sessions;
		}

	}

}
