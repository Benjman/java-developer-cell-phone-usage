package wcf.dal;

import wcf.entity.PhoneSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryPhoneHistoryRepository implements PhoneHistoryRepository {

	private static InMemoryPhoneHistoryRepository instance;

	public static InMemoryPhoneHistoryRepository getInstance() {
		if (instance == null) {
			instance = new InMemoryPhoneHistoryRepository();
		}
		return instance;
	}

	private final List<PhoneSession> storage = new ArrayList<>();

	private InMemoryPhoneHistoryRepository() {
	}

	public List<PhoneSession> findAll() {
		return storage;
	}

	public void save(final PhoneSession session) {
		storage.add(session);
	}

	public List<PhoneSession> findByEmployeeId(final int employeeId) {
		return storage.stream()
				.filter(session -> session.employeeId == employeeId)
				.collect(Collectors.toList());
	}

	@Override
	public List<PhoneSession> findBetweenDates(final Date start, final Date end) {
		return storage.stream()
				.filter(session -> session.date.compareTo(start) >= 0
						&& session.date.compareTo(end) <= 0)
				.collect(Collectors.toList());
	}

}
