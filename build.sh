mkdir -p build
cp -r res build
javac \
	./src/wcf/Main.java \
	./src/wcf/entity/EntityNotFoundException.java \
	./src/wcf/entity/PhoneSession.java \
	./src/wcf/entity/Employee.java \
	./src/wcf/Utils.java \
	./src/wcf/dal/InMemoryEmployeeRepository.java \
	./src/wcf/dal/PhoneHistoryRepository.java \
	./src/wcf/dal/InMemoryPhoneHistoryRepository.java \
	./src/wcf/dal/EmployeeRepository.java \
	./src/wcf/service/PhoneService.java \
	./src/wcf/service/EmployeeService.java \
	./src/wcf/service/ReportService.java \
	./src/wcf/service/PrintService.java \
	-d build

