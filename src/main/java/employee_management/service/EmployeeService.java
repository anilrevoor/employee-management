package employee_management.service;

import employee_management.entity.Employee;
import employee_management.exception.EmployeeNotFoundException;
import employee_management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {

        logger.info("Creating employee with email: {}", employee.getEmail());

        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee created successfully with id: {}", savedEmployee.getId());

        return savedEmployee;
    }

    public List<Employee> getAllEmployees() {

        logger.info("Fetching all employees");

        List<Employee> employees = employeeRepository.findAll();

        logger.info("Found {} employees", employees.size());

        return employees;
    }

    public Optional<Employee> getEmployeeById(Long id) {

        logger.info("Fetching employee with id: {}", id);

        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {

        logger.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(
                            "Employee not found with id: " + id);
                });

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());

        Employee updatedEmployee = employeeRepository.save(employee);

        logger.info("Employee updated successfully with id: {}", id);

        return updatedEmployee;
    }

    public void deleteEmployee(Long id) {

        logger.info("Deleting employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(
                            "Employee not found with id: " + id);
                });

        employeeRepository.delete(employee);

        logger.info("Employee deleted successfully with id: {}", id);
    }
}