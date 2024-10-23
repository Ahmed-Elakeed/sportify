package eg.mos.sportify.security;

import com.google.gson.Gson;
import eg.mos.sportify.dto.ApiResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Filter for processing JWT tokens in incoming requests.
 * This filter validates the token and sets the authentication in the SecurityContext if the token is valid.
 */
public class JwtTokenFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructs a JwtTokenFilter with the provided JwtTokenProvider.
     *
     * @param jwtTokenProvider the provider for handling JWT tokens
     */
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Filters the incoming requests and checks for a valid JWT token.
     *
     * @param request      the HTTP request
     * @param response     the HTTP response
     * @param filterChain  the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        TokenStatus tokenStatus = jwtTokenProvider.validateToken(token);

        if (token != null && tokenStatus.equals(TokenStatus.VALID)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } else {
            String errorMessage = tokenStatus.equals(TokenStatus.ERROR)
                    ? "Invalid or empty token"
                    : "Token expired, please login again";
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(errorMessage)
                    .data("Invalid credentials")
                    .build();
            String jsonResponse = convertToJson(apiResponse);
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonResponse);
            }
        }
    }

    /**
     * Converts the provided ApiResponse to a JSON string.
     *
     * @param genericResponse the ApiResponse to convert
     * @return the JSON representation of the ApiResponse
     */
    private String convertToJson(ApiResponse<?> genericResponse) {
        Gson gson = new Gson();
        return gson.toJson(genericResponse);
    }
}
