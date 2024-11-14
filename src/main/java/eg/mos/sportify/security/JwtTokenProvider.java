package eg.mos.sportify.security;


import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eg.mos.sportify.dto.user.UserAuthenticationDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


/**
 * Provider class for generating, validating, and parsing JWT tokens.
 */
@Component
public class JwtTokenProvider {


    @Value("${spring.jwt.secret-name}")
    private String secretName; // Use a strong key and keep it secure

    @Value("${spring.cloud.aws.region.static}")
    private String secretRegion;

    private static Key hmacKey;

    private final UserDetailsService userDetailsService;

    /**
     * Initializes the HMAC key after the class has been constructed.
     */
    @PostConstruct
    private void securityInit() {
        JsonObject secretJson = JsonParser.parseString(this.getSecret(secretName)).getAsJsonObject();
        String encodedKey = secretJson.get("secret").getAsString();
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(encodedKey),
                SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Fetching JWT secret from AWS secret manager by secret name for the specified region
     * @param secretName
     * @return JSON Object with the JWT secret value
     */
    private String getSecret(String secretName) {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(secretRegion)
                .build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        return getSecretValueResult.getSecretString();
    }

    /**
     * Constructs a JwtTokenProvider with the specified UserDetailsService.
     *
     * @param userDetailsService the service to load user details
     */
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Generates a JWT token for the given user authentication data.
     *
     * @param userAuthenticationDTO the user authentication data
     * @return the generated JWT token
     */
    public String generateToken(UserAuthenticationDTO userAuthenticationDTO) {
        return Jwts.builder()
                .claim("username", userAuthenticationDTO.getUsername())
                .setSubject(userAuthenticationDTO.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1L, ChronoUnit.HOURS)))
                .signWith(hmacKey)
                .compact();
    }

    /**
     * Resolves and retrieves the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the HTTP request
     * @return the JWT token, or null if not found
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return the status of the token (VALID, EXPIRED, or ERROR)
     */
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException expiredJwtException) {
            return TokenStatus.EXPIRED;
        } catch (Exception e) {
            return TokenStatus.ERROR;
        }
    }

    /**
     * Retrieves the Authentication object based on the provided JWT token.
     *
     * @param token the JWT token
     * @return the Authentication object if the token is valid; null otherwise
     */
    public Authentication getAuthentication(String token) {
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey).build().parseClaimsJws(token);
        String username = (String) jwt.getBody().get("username");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
