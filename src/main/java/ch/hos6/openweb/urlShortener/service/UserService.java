package ch.hos6.openweb.urlShortener.service;

import ch.hos6.openweb.urlShortener.domain.dto.UserDto;
import ch.hos6.openweb.urlShortener.domain.entity.User;
import ch.hos6.openweb.urlShortener.domain.repository.UserRepository;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UsernameAlreadyTakenException;
import ch.hos6.openweb.urlShortener.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for managing users.
 * This service provides functionality for creating users, using UserRepository for data access, UserMapper for conversion between User and UserDto objects,
 * and PasswordEncoder for encoding user passwords.
 *
 * @author Toubia Oussama
 */
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the provided UserRepository, UserMapper, and PasswordEncoder.
     *
     * @param userRepository the repository to use for user data access
     * @param userMapper the mapper to use for converting between User and UserDto objects
     * @param passwordEncoder the encoder to use for encoding user passwords
     */
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user with the details provided in the UserDto object.
     * The user password is encoded before the user is saved to the repository.
     * If a user with the same username already exists, a UsernameAlreadyTakenException is thrown.
     *
     * @param userDto the details of the user to create
     * @return a UserDto object with the details of the created user
     * @throws UsernameAlreadyTakenException if a user with the same username already exists
     */
    public UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException {
        try{
            User user = userMapper.DtoToUser(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = this.userRepository.save(user);
            return userMapper.userToDto(user);
        }catch (DuplicateKeyException ex){
            log.error("Username already taken {} ", userDto.username(), ex);
            throw new UsernameAlreadyTakenException("Username is already taken.");
        }
    }
}
