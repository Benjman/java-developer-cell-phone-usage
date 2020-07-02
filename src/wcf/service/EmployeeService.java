package wcf.service;

import wcf.dal.EmployeeRepository;
import wcf.entity.Employee;
import wcf.entity.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
	private static EmployeeService instance;

	public static EmployeeService getInstance() {
		return instance;
	}

	public static EmployeeService initInstance(EmployeeRepository repository) {
		if (instance == null) {
			instance = new EmployeeService(repository);
		}
		return getInstance();
	}

	private final EmployeeRepository repository;

	private EmployeeService(final EmployeeRepository repository) {
		this.repository = repository;
	}

	public void update(Employee employee) {
		repository.save(employee);
	}

	public Employee get(int id) throws EntityNotFoundException {
		return repository.findById(id);
	}

	public List<Employee> get(final List<Integer> employeeIds) throws EntityNotFoundException {
		List<Employee> result = new ArrayList<>();
		for (final Integer id : employeeIds) {
			result.add(get(id));
		}
		return result;
	}

	public void delete(Employee employee) {
		repository.delete(employee);
	}

}
