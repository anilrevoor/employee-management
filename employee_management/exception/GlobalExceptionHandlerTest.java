package employee_management.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleEmployeeNotFoundException_shouldReturnNotFound() {

        EmployeeNotFoundException exception =
                new EmployeeNotFoundException(
                        "Employee not found with id: 999");

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/v1/employees/999");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleEmployeeNotFoundException(
                        exception, request);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                404,
                response.getBody().getStatus()
        );

        assertEquals(
                "Employee Not Found",
                response.getBody().getError()
        );

        assertEquals(
                "Employee not found with id: 999",
                response.getBody().getMessage()
        );

        assertEquals(
                "/api/v1/employees/999",
                response.getBody().getPath()
        );

        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidationException_shouldReturnBadRequest() {

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        org.springframework.validation.BindingResult bindingResult =
                mock(org.springframework.validation.BindingResult.class);

        org.springframework.validation.FieldError fieldError =
                new org.springframework.validation.FieldError(
                        "employeeRequest",
                        "firstName",
                        "First name is required"
                );

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(java.util.List.of(fieldError));

        ResponseEntity<java.util.Map<String, String>> response =
                handler.handleValidationException(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "First name is required",
                response.getBody().get("firstName")
        );
    }

    @Test
    void handleGeneralException_shouldReturnInternalServerError() {

        Exception exception =
                new RuntimeException("Unexpected error");

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/v1/employees");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGeneralException(
                        exception, request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                500,
                response.getBody().getStatus()
        );

        assertEquals(
                "Internal Server Error",
                response.getBody().getError()
        );

        assertEquals(
                "An unexpected error occurred",
                response.getBody().getMessage()
        );

        assertEquals(
                "/api/v1/employees",
                response.getBody().getPath()
        );

        assertNotNull(response.getBody().getTimestamp());
    }
}