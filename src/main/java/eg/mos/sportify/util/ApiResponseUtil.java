package eg.mos.sportify.util;

import eg.mos.sportify.dto.ApiResponse;

public class ApiResponseUtil {


    private ApiResponseUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Builds a successful ApiResponse with a custom message and data.
     *
     * @param message the success message.
     * @param data additional data to return in the response.
     * @return a constructed ApiResponse.
     */
    public static <T> ApiResponse<T> buildSuccessResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Builds an error ApiResponse with a custom message and data.
     *
     * @param message the error message.
     * @param data additional error details to return in the response.
     * @return a constructed ApiResponse.
     */
    public static <T> ApiResponse<T> buildErrorResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}
