package eg.mos.sportify.exception;


import eg.mos.sportify.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .data("No found exception")
                        .build()
        );
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .data("Authorization exception")
                        .build()
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationException(ValidationException exception) {
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .data("Validation exception")
                        .build()
        );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception exception) {
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .data("Internal server error")
                        .build()
        );
    }
}
