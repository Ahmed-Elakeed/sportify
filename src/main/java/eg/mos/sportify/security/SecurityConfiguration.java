package eg.mos.sportify.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


/**
 * Security configuration class for the application.
 * This class configures the security settings, including authentication and authorization.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    /**
     * A list of URL patterns that should be publicly accessible without authentication.
     */
    @Value("#{'${spring.auth.white-list}'.split(',\\s*')}")
    private List<String> whiteList;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Configures HTTP security settings for the application.
     *
     * @param http the HttpSecurity object to be configured
     * @throws Exception if an error occurs during configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(this.whiteList.toArray(new String[0])).permitAll() // Allow public access to specified URLs
                .anyRequest().authenticated() // Require authentication for all other requests
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // Add JWT filter
    }

    /**
     * Configures web security settings to ignore specified URL patterns.
     *
     * @param web the WebSecurity object to be configured
     * @throws Exception if an error occurs during configuration
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(this.whiteList.toArray(new String[0])); // Ignore security for whitelisted URLs
    }

    /**
     * Provides the AuthenticationManager bean for the application.
     *
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs during bean creation
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
