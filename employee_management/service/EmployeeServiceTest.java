package employee_management.service;

import employee_management.entity.Employee;
import employee_management.exception.EmployeeNotFoundException;
import employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void createEmployee_shouldSaveEmployee() {

        Employee employee = new Employee();
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.createEmployee(employee);

        assertEquals("Anil", result.getFirstName());
        assertEquals("Kumar", result.getLastName());
        assertEquals("anil@example.com", result.getEmail());
        assertEquals("IT", result.getDepartment());
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeNotFound() {

        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.empty());

        Employee employee = new Employee();
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(employeeId, employee)
        );

        assertEquals(
                "Employee not found with id: 999",
                exception.getMessage()
        );
    }

    @Test
    void getEmployeeById_shouldReturnEmployeeWhenFound() {

        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.of(employee));

        java.util.Optional<Employee> result =
                employeeService.getEmployeeById(employeeId);

        assertEquals(true, result.isPresent());
        assertEquals("Anil", result.get().getFirstName());
        assertEquals("Kumar", result.get().getLastName());
        assertEquals("anil@example.com", result.get().getEmail());
        assertEquals("IT", result.get().getDepartment());
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {

        Employee employee1 = new Employee();
        employee1.setFirstName("Anil");
        employee1.setLastName("Kumar");
        employee1.setEmail("anil@example.com");
        employee1.setDepartment("IT");

        Employee employee2 = new Employee();
        employee2.setFirstName("Rahul");
        employee2.setLastName("Sharma");
        employee2.setEmail("rahul@example.com");
        employee2.setDepartment("HR");

        when(employeeRepository.findAll())
                .thenReturn(java.util.List.of(employee1, employee2));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Anil", result.get(0).getFirstName());
        assertEquals("Rahul", result.get(1).getFirstName());
    }

    @Test
    void deleteEmployee_shouldDeleteEmployeeWhenFound() {

        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.of(employee));

        employeeService.deleteEmployee(employeeId);

        org.mockito.Mockito.verify(employeeRepository)
                .delete(employee);
    }

    @Test
    void deleteEmployee_shouldThrowExceptionWhenEmployeeNotFound() {

        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(employeeId)
        );

        assertEquals(
                "Employee not found with id: 999",
                exception.getMessage()
        );
    }

    @Test
    void updateEmployee_shouldUpdateEmployeeWhenFound() {

        Long employeeId = 1L;

        Employee existingEmployee = new Employee();
        existingEmployee.setFirstName("Old");
        existingEmployee.setLastName("Name");
        existingEmployee.setEmail("old@example.com");
        existingEmployee.setDepartment("IT");

        Employee employeeDetails = new Employee();
        employeeDetails.setFirstName("Anil");
        employeeDetails.setLastName("Kumar");
        employeeDetails.setEmail("anil@example.com");
        employeeDetails.setDepartment("Development");

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(existingEmployee);

        Employee result =
                employeeService.updateEmployee(employeeId, employeeDetails);

        assertEquals("Anil", result.getFirstName());
        assertEquals("Kumar", result.getLastName());
        assertEquals("anil@example.com", result.getEmail());
        assertEquals("Development", result.getDepartment());

        org.mockito.Mockito.verify(employeeRepository)
                .save(existingEmployee);
    }
}