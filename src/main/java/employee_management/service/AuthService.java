package employee_management.service;

import employee_management.dto.AuthRequest;
import employee_management.dto.AuthResponse;
import employee_management.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {

        // Demo user for JWT authentication
        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            String role = "ADMIN";

            String token = jwtService.generateToken(
                    request.getUsername(),
                    role);

            return new AuthResponse(token, role);
        }

        throw new RuntimeException("Invalid username or password");
    }
}