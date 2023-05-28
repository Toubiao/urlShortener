package ch.hos6.openweb.urlShortener.unit;

import ch.hos6.openweb.urlShortener.domain.dto.UserDto;
import ch.hos6.openweb.urlShortener.domain.entity.User;
import ch.hos6.openweb.urlShortener.service.UserService;
import ch.hos6.openweb.urlShortener.domain.repository.UserRepository;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UsernameAlreadyTakenException;
import ch.hos6.openweb.urlShortener.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class UserServiceUnitTest {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapper = Mockito.mock(UserMapper.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, userMapper, passwordEncoder);
    }

    @Test
    public void testCreateUser_Success() throws UsernameAlreadyTakenException {
        UserDto userDto = new UserDto("test","password");
        UserDto userDtoWithEncodedPassword = new UserDto("test","encodedPassword");


        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(userMapper.DtoToUser(userDto)).thenReturn(user);
        when(userMapper.userToDto(user)).thenReturn(userDtoWithEncodedPassword);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals("test", createdUser.username());
        assertEquals("encodedPassword", createdUser.password());
    }

    @Test
    public void testCreateUser_UsernameAlreadyTaken() {
        UserDto userDto = new UserDto("test","password");

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(userMapper.DtoToUser(userDto)).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DuplicateKeyException(""));

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.createUser(userDto));
    }

}
