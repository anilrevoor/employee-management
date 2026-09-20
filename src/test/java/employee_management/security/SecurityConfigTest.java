package employee_management.security;

import employee_management.service.AuthService;
import employee_management.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    @MockitoBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void employeeApi_withoutAuthentication_shouldReturn401()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/employees")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void employeeApi_withAdminRole_shouldAllowAccess()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/employees")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "google-user",
            roles = "USER"
    )
    void employeeApi_withUserRole_shouldReturn403()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/employees")
                )
                .andExpect(status().isForbidden());
    }
}