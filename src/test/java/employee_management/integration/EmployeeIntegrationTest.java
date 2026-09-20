package employee_management.integration;

import employee_management.dto.AuthRequest;
import employee_management.dto.AuthResponse;
import employee_management.dto.EmployeeRequest;
import employee_management.entity.Employee;
import employee_management.repository.EmployeeRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void shouldLoginCreateEmployeeAndRetrieveEmployee() throws Exception {

        // -------------------------------------------------
        // STEP 1: Login and obtain JWT
        // -------------------------------------------------

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("admin");
        authRequest.setPassword("admin123");

        String loginJson =
                objectMapper.writeValueAsString(authRequest);

        String loginResponse =
                mockMvc.perform(
                                post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(loginJson)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode loginResult =
                objectMapper.readTree(loginResponse);

        String token =
                loginResult.get("token").asText();

        String role =
                loginResult.get("role").asText();

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("ADMIN", role);

        // -------------------------------------------------
        // STEP 2: Create employee using JWT
        // -------------------------------------------------

        EmployeeRequest employeeRequest =
                new EmployeeRequest();

        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john.doe@example.com");
        employeeRequest.setDepartment("IT");

        String employeeJson =
                objectMapper.writeValueAsString(employeeRequest);

        String createResponse =
                mockMvc.perform(
                                post("/api/v1/employees")
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(employeeJson)
                        )
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        Employee createdEmployee =
                objectMapper.readValue(
                        createResponse,
                        Employee.class
                );

        assertNotNull(createdEmployee.getId());
        assertEquals(
                "John",
                createdEmployee.getFirstName()
        );
        assertEquals(
                "Doe",
                createdEmployee.getLastName()
        );
        assertEquals(
                "john.doe@example.com",
                createdEmployee.getEmail()
        );
        assertEquals(
                "IT",
                createdEmployee.getDepartment()
        );

        Long employeeId =
                createdEmployee.getId();

        // -------------------------------------------------
        // STEP 3: Verify actual database
        // -------------------------------------------------

        Employee databaseEmployee =
                employeeRepository
                        .findById(employeeId)
                        .orElse(null);

        assertNotNull(databaseEmployee);

        assertEquals(
                "john.doe@example.com",
                databaseEmployee.getEmail()
        );

        // -------------------------------------------------
        // STEP 4: Retrieve employee through API
        // -------------------------------------------------

        String getResponse =
                mockMvc.perform(
                                get("/api/v1/employees/" + employeeId)
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        Employee retrievedEmployee =
                objectMapper.readValue(
                        getResponse,
                        Employee.class
                );

        assertEquals(
                employeeId,
                retrievedEmployee.getId()
        );

        assertEquals(
                "john.doe@example.com",
                retrievedEmployee.getEmail()
        );
    }

    @Test
    void shouldRejectEmployeeRequestWithoutAuthentication()
            throws Exception {

        EmployeeRequest employeeRequest =
                new EmployeeRequest();

        employeeRequest.setFirstName("Unauthorized");
        employeeRequest.setLastName("User");
        employeeRequest.setEmail(
                "unauthorized@example.com"
        );
        employeeRequest.setDepartment("IT");

        String employeeJson =
                objectMapper.writeValueAsString(
                        employeeRequest
                );

        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(employeeJson)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyDatabaseIntegration() {

        Employee employee =
                new Employee(
                        "Jane",
                        "Smith",
                        "jane.smith@example.com",
                        "HR"
                );

        Employee savedEmployee =
                employeeRepository.save(employee);

        assertNotNull(savedEmployee.getId());

        assertEquals(
                1,
                employeeRepository.count()
        );

        Employee databaseEmployee =
                employeeRepository
                        .findById(savedEmployee.getId())
                        .orElse(null);

        assertNotNull(databaseEmployee);

        assertEquals(
                "jane.smith@example.com",
                databaseEmployee.getEmail()
        );
    }
}