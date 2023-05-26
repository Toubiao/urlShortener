package ch.hos6.openweb.urlShortener.security;

import ch.hos6.openweb.urlShortener.domain.repository.UserRepository;
import ch.hos6.openweb.urlShortener.errorhandling.exception.CustomUsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service class provides custom implementation of the UserDetailsService interface for authentication.
 * It is used by Spring Security to load user-specific data during security operations.
 * The class uses a UserRepository to retrieve user data from the database.
 *
 * @author Toubia Oussama
 */

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor for the CustomUserDetailService.
     * It initializes the UserRepository, which is used to retrieve user data from the database.
     *
     * @param userRepository A UserRepository instance
     */
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method loads the user details by the given username.
     * It overrides the loadUserByUsername method of the UserDetailsService interface.
     * The method uses the UserRepository to find the user in the database.
     * If the user is not found, it throws a CustomUsernameNotFoundException.
     * The exception can't be caught easily because it's thrown too deep from spring
     *
     * @param username The username of the user
     * @return A UserDetails instance containing the details of the user
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomUsernameNotFoundException("User not found: " + username));
    }


}
