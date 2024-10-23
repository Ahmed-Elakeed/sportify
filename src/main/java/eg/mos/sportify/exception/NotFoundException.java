package eg.mos.sportify.exception;


/**
 * Custom exception class to handle resource not found errors.
 * This exception is thrown when a requested resource cannot be found
 * within the application, typically in response to a user request.
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
