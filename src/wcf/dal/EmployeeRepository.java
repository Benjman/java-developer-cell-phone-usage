package wcf.dal;

import wcf.entity.Employee;
import wcf.entity.EntityNotFoundException;

public interface EmployeeRepository {

	void save(Employee entity);

	Employee findById(int id) throws EntityNotFoundException;

	void delete(Employee entity);

}
