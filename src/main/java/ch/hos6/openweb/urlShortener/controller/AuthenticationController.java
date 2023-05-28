package ch.hos6.openweb.urlShortener.controller;


import ch.hos6.openweb.urlShortener.domain.dto.UserDto;
import ch.hos6.openweb.urlShortener.domain.entity.User;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UsernameAlreadyTakenException;
import ch.hos6.openweb.urlShortener.service.UserService;
import ch.hos6.openweb.urlShortener.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class to handle authentication operations.
 * This includes user login and registration endpoints.
 *
 * @author Toubia Oussama
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="*", maxAge=3600)
@Tag(name = "Authentication", description = "Endpoints for user authentication operations")
public class AuthenticationController {


    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    /**
     * Constructor for the AuthenticationController.
     *
     * @param jwtTokenUtils utility class for JWT token operations.
     * @param userService   the service handling user operations.
     */
    public AuthenticationController(JwtTokenUtils jwtTokenUtils, UserService userService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userService = userService;
    }

    /**
     * Endpoint for user login.
     * Authenticates the user and returns a JWT token.
     *
     * @param authentication Spring Security authentication object.
     * @return a JWT token.
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates the user and returns a JWT token")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<Map<String, String>> login(Authentication authentication) {
        if(authentication == null){
            throw new AuthenticationCredentialsNotFoundException("No credential found");
        }
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenUtils.token(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint for user registration.
     * Registers a new user and returns the created UserDto.
     *
     * @param user the UserDto object representing the user to register.
     * @return the created UserDto.
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Registers a new user and returns the created UserDto")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user) {
        UserDto createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
