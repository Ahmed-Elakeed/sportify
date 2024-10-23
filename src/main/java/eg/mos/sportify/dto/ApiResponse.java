package eg.mos.sportify.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Generic API response object that encapsulates the response
 * structure returned from API calls.
 *
 * @param <T> the type of the data contained in the response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    /**
     * Indicates whether the API request was successful.
     */
    private boolean success;

    /**
     * A message providing additional information about the response.
     */
    private String message;

    /**
     * The data returned by the API. This can be of any type specified
     * when creating the ApiResponse instance.
     */
    private T data;
}
