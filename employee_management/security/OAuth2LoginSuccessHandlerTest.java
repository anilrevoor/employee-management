package employee_management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuth2LoginSuccessHandlerTest {

    private final HttpServletRequest request =
            mock(HttpServletRequest.class);

    private final HttpServletResponse response =
            mock(HttpServletResponse.class);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void onAuthenticationSuccess_shouldSaveAuthenticationAndRedirect()
            throws Exception {

        OAuth2User oauth2User =
                new DefaultOAuth2User(
                        Collections.emptyList(),
                        Map.of(
                                "email", "test@gmail.com",
                                "name", "Test User"
                        ),
                        "email"
                );

        Authentication authentication =
                mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(oauth2User);

        OAuth2LoginSuccessHandler handler =
                new OAuth2LoginSuccessHandler();

        handler.onAuthenticationSuccess(
                request,
                response,
                authentication
        );

        Authentication savedAuthentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNotNull(savedAuthentication);

        assertSame(
                authentication,
                savedAuthentication
        );

        verify(response)
                .sendRedirect("/swagger-ui/index.html");
    }
}
