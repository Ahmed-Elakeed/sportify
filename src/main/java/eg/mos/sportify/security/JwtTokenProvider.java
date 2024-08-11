package eg.mos.sportify.security;


import eg.mos.sportify.dto.AuthRequest;
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

@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret-key}")
    private String secretKey; // Use a strong key and keep it secure

    private static Key hmacKey;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    private void securityInit() {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(AuthRequest authRequest) {
        return Jwts.builder()
                .claim("username", authRequest.getUsername())
                .setSubject(authRequest.getUsername())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1L, ChronoUnit.HOURS)))
                .signWith(hmacKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        }catch (ExpiredJwtException expiredJwtException){
            return TokenStatus.EXPIRED;
        }
        catch (Exception e){
            return TokenStatus.ERROR;
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey).build().parseClaimsJws(token);
        String username = (String) jwt.getBody().get("username");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
