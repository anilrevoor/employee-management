package employee_management.controller;

import employee_management.entity.Employee;
import employee_management.security.JwtService;
import employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.servlet.OAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@ImportAutoConfiguration(
        exclude = OAuth2ClientWebSecurityAutoConfiguration.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void createEmployee_shouldReturnCreated() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeService.createEmployee(any(Employee.class)))
                .thenReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Anil",
                                    "lastName": "Kumar",
                                    "email": "anil@example.com",
                                    "department": "IT"
                                }
                                """))
                .andExpect(status().isCreated());
    }


    @Test
    void getAllEmployees_shouldReturnOk() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeService.getAllEmployees())
                .thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk());
    }


    @Test
    void getEmployeeById_shouldReturnOkWhenFound() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("IT");

        when(employeeService.getEmployeeById(1L))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk());
    }


    @Test
    void getEmployeeById_shouldReturnNotFoundWhenNotFound() throws Exception {

        when(employeeService.getEmployeeById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateEmployee_shouldReturnOk() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Anil");
        employee.setLastName("Kumar");
        employee.setEmail("anil@example.com");
        employee.setDepartment("Development");

        when(employeeService.updateEmployee(eq(1L), any(Employee.class)))
                .thenReturn(employee);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Anil",
                                    "lastName": "Kumar",
                                    "email": "anil@example.com",
                                    "department": "Development"
                                }
                                """))
                .andExpect(status().isOk());
    }


    @Test
    void deleteEmployee_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }


    @Test
    void createEmployee_shouldReturnBadRequestForInvalidData() throws Exception {

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "",
                                    "email": "invalid-email",
                                    "department": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateEmployee_shouldReturnBadRequestForInvalidData() throws Exception {

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "",
                                    "email": "invalid-email",
                                    "department": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}