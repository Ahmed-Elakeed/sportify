package eg.mos.sportify.exception;


/**
 * Custom exception class to handle authorization-related errors.
 * This exception is thrown when a user does not have the necessary
 * permissions to perform a certain action.
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
