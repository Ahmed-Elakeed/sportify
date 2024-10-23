package eg.mos.sportify.exception;


/**
 * Custom exception class to handle validation errors.
 * This exception is thrown when a validation check fails,
 * indicating that the provided input does not meet the required criteria.
 */
public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}
