package eg.mos.sportify.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Configuration class for password encoding.
 * This class defines a bean for encoding passwords using BCrypt.
 */
@Configuration
public class PasswordEncoderConfiguration {

    /**
     * Creates a PasswordEncoder bean that uses BCrypt for password encoding.
     *
     * @return a PasswordEncoder instance configured with BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
