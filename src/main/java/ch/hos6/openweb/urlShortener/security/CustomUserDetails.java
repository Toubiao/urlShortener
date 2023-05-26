package ch.hos6.openweb.urlShortener.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * This class represents a custom user detail implementation.
 * It is used for authentication and to provide additional user information.
 * Currently, it only includes non-implemented methods to keep a simple implementation for user security.
 * It always returns true for the account status checks, and null for the authorities.
 * For a more complex application, these methods can be overridden to provide real data.
 *
 * @author Toubia Oussama
 */
public class CustomUserDetails  {

    /**
     * This method should return the authorities granted to the user.
     * It is currently non-implemented and returns null.
     * @return null
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * This method should check if the user's account has not expired.
     * It is currently non-implemented and returns true.
     * @return true
     */
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * This method should check if the user's account is not locked.
     * It is currently non-implemented and returns true.
     * @return true
     */
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * This method should check if the user's credentials have not expired.
     * It is currently non-implemented and returns true.
     * @return true
     */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * This method should check if the user's account is enabled.
     * It is currently non-implemented and returns true.
     * @return true
     */
    public boolean isEnabled() {
        return true;
    }
}
