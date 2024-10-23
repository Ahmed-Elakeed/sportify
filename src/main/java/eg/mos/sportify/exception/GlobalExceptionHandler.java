package eg.mos.sportify.exception;


import eg.mos.sportify.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Global exception handler for the application.
 * This class handles exceptions thrown by controllers and
 * provides a uniform response structure for errors, ensuring
 * that clients receive consistent and meaningful error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles NotFoundException and returns a standardized response.
     *
     * @param exception the NotFoundException thrown
     * @return a ResponseEntity containing an ApiResponse with the error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), "Resource not found");
    }

    /**
     * Handles AuthorizationException and returns a standardized response.
     *
     * @param exception the AuthorizationException thrown
     * @return a ResponseEntity containing an ApiResponse with the error details
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationException(AuthorizationException exception) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, exception.getMessage(), "Authorization error");
    }

    /**
     * Handles ValidationException and returns a standardized response.
     *
     * @param exception the ValidationException thrown
     * @return a ResponseEntity containing an ApiResponse with the error details
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(ValidationException exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage(), "Validation error");
    }

    /**
     * Handles generic exceptions and returns a standardized response.
     *
     * @param exception the generic Exception thrown
     * @return a ResponseEntity containing an ApiResponse with the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), "Internal server error");
    }

    /**
     * Builds a standardized ResponseEntity with the given HTTP status, message, and data.
     *
     * @param status  the HTTP status to return
     * @param message the error message to include in the response
     * @param data    additional data to include in the response
     * @return a ResponseEntity containing an ApiResponse with the error details
     */
    private ResponseEntity<ApiResponse<String>> buildResponseEntity(HttpStatus status, String message, String data) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
