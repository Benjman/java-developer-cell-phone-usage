package wcf.service;

import wcf.dal.PhoneHistoryRepository;
import wcf.entity.Employee;
import wcf.entity.PhoneSession;

import java.util.Date;
import java.util.List;

public class PhoneService {

	private static PhoneService instance;

	public static PhoneService getInstance() {
		return instance;
	}

	public static PhoneService initInstance(PhoneHistoryRepository repository) {
		if (instance == null) {
			instance = new PhoneService(repository);
		}
		return instance;
	}

	private final PhoneHistoryRepository repository;

	private PhoneService(final PhoneHistoryRepository repository) {
		this.repository = repository;
	}

	public void addSession(PhoneSession session) {
		repository.save(session);
	}

	public List<PhoneSession> getAllSessions() {
		return repository.findAll();
	}

	public List<PhoneSession> getAllByEmployee(Employee employee) {
		return repository.findByEmployeeId(employee.id);
	}

	public List<PhoneSession> getForRange(final Date start, final Date end) {
		return repository.findBetweenDates(start, end);
	}

}
