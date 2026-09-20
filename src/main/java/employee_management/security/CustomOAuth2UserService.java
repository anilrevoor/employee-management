package employee_management.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate;

    public CustomOAuth2UserService() {
        this.delegate = new DefaultOAuth2UserService();
    }

    CustomOAuth2UserService(DefaultOAuth2UserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauth2User =
                delegate.loadUser(userRequest);

        return new DefaultOAuth2User(
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                ),
                oauth2User.getAttributes(),
                "email"
        );
    }
}