package employee_management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService = mock(JwtService.class);

    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter(jwtService);

    private final HttpServletRequest request =
            mock(HttpServletRequest.class);

    private final HttpServletResponse response =
            mock(HttpServletResponse.class);

    private final FilterChain filterChain =
            mock(FilterChain.class);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAuthenticateValidToken()
            throws ServletException, IOException {

        String token = "valid-token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenReturn("admin");

        when(jwtService.extractRole(token))
                .thenReturn("ADMIN");

        when(jwtService.isTokenValid(token, "admin"))
                .thenReturn(true);

        doAnswer(invocation -> {

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            assertNotNull(authentication);
            assertEquals("admin", authentication.getPrincipal());
            assertTrue(authentication.isAuthenticated());

            return null;

        }).when(filterChain).doFilter(request, response);

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNotNull(authentication);
        assertEquals("admin", authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void doFilterInternal_shouldContinueWithoutAuthenticationWhenHeaderMissing()
            throws ServletException, IOException {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_shouldContinueWithoutAuthenticationForInvalidToken()
            throws ServletException, IOException {

        String token = "invalid-token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenThrow(new RuntimeException("Invalid token"));

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
    }
}
