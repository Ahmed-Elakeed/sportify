package eg.mos.sportify.security;

/**
 * Enum representing the status of a JWT token.
 * This enum is used to indicate whether a token is valid, expired, or has encountered an error during validation.
 */
public enum TokenStatus {
    VALID,
    EXPIRED,
    ERROR
}
