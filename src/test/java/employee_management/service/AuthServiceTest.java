package employee_management.service;

import employee_management.dto.AuthRequest;
import employee_management.dto.AuthResponse;
import employee_management.security.JwtService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final JwtService jwtService = mock(JwtService.class);

    private final AuthService authService =
            new AuthService(jwtService);

    @Test
    void login_shouldReturnTokenForValidCredentials() {

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(jwtService.generateToken("admin", "ADMIN"))
                .thenReturn("mock-jwt-token");

        AuthResponse response =
                authService.login(request);

        assertNotNull(response);
        assertEquals(
                "mock-jwt-token",
                response.getToken()
        );

        verify(jwtService)
                .generateToken("admin", "ADMIN");
    }

    @Test
    void login_shouldThrowExceptionForInvalidUsername() {

        AuthRequest request = new AuthRequest();
        request.setUsername("wrong");
        request.setPassword("admin123");

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        verifyNoInteractions(jwtService);
    }

    @Test
    void login_shouldThrowExceptionForInvalidPassword() {

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        verifyNoInteractions(jwtService);
    }
}