package wcf.dal;

import wcf.entity.PhoneSession;

import java.util.Date;
import java.util.List;

public interface PhoneHistoryRepository {

	List<PhoneSession> findAll();

	void save(PhoneSession session);

	List<PhoneSession> findByEmployeeId(int employeeId);

	List<PhoneSession> findBetweenDates(Date start, Date end);

}
