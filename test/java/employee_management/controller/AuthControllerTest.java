package employee_management.controller;

import employee_management.dto.AuthResponse;
import employee_management.security.JwtService;
import employee_management.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.servlet.OAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ImportAutoConfiguration(
        exclude = OAuth2ClientWebSecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void login_shouldReturnTokenForValidRequest()
            throws Exception {

        when(authService.login(any()))
                .thenReturn(
                        new AuthResponse("mock-jwt-token", "ADMIN")
                );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content("""
                            {
                                "username": "admin",
                                "password": "admin123"
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("mock-jwt-token"));
    }

    @Test
    void login_shouldReturnBadRequestWhenUsernameMissing()
            throws Exception {

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content("""
                                {
                                    "username": "",
                                    "password": "admin123"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnBadRequestWhenPasswordMissing()
            throws Exception {

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content("""
                                {
                                    "username": "admin",
                                    "password": ""
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }
}