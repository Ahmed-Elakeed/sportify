package eg.mos.sportify.security;

import eg.mos.sportify.domain.User;
import eg.mos.sportify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for loading user-specific data during authentication.
 * This class implements the UserDetailsService interface to fetch user details
 * from the database and provide them to the Spring Security framework.
 */
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads user details by username.
     *
     * @param username the username of the user
     * @return UserDetails containing the user's information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(new String[]{})  // Adjust roles as necessary
                .build();
    }

    /**
     * Retrieves the username from the currently authenticated user's token.
     *
     * @return the username of the authenticated user, or null if not authenticated
     */
    public static String getUsernameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
