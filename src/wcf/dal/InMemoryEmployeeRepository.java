package wcf.dal;

import java.util.HashMap;
import java.util.Map;

import wcf.entity.Employee;
import wcf.entity.EntityNotFoundException;

public class InMemoryEmployeeRepository implements EmployeeRepository {

	private static InMemoryEmployeeRepository instance;

	public static InMemoryEmployeeRepository getInstance() {
		if (instance == null) {
			instance = new InMemoryEmployeeRepository();
		}
		return instance;
	}

	private final Map<Integer, Employee> storage = new HashMap<>();

	private InMemoryEmployeeRepository() {
	}

	public void save(Employee employee) {
		storage.put(employee.id, employee);
	}

	public Employee findById(int id) throws EntityNotFoundException {
		if (!storage.containsKey(id)) {
			throw new EntityNotFoundException();
		}
		return storage.get(id);
	}

	public void delete(Employee employee) {
		storage.remove(employee.id);
	}

}
