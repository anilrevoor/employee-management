package employee_management.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomOAuth2UserServiceTest {

    @Test
    void loadUser_shouldAssignRoleUser() {

        DefaultOAuth2UserService delegate =
                mock(DefaultOAuth2UserService.class);

        OAuth2UserRequest userRequest =
                mock(OAuth2UserRequest.class);

        OAuth2User googleUser =
                new DefaultOAuth2User(
                        Collections.emptyList(),
                        Map.of(
                                "email", "test@gmail.com",
                                "name", "Test User"
                        ),
                        "email"
                );

        when(delegate.loadUser(userRequest))
                .thenReturn(googleUser);

        CustomOAuth2UserService service =
                new CustomOAuth2UserService(delegate);

        OAuth2User result =
                service.loadUser(userRequest);

        assertNotNull(result);

        assertEquals(
                "test@gmail.com",
                result.getAttribute("email")
        );

        assertEquals(
                "Test User",
                result.getAttribute("name")
        );

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch("ROLE_USER"::equals)
        );

        verify(delegate).loadUser(userRequest);
    }
}