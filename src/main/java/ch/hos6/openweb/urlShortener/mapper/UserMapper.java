package ch.hos6.openweb.urlShortener.mapper;

import ch.hos6.openweb.urlShortener.domain.dto.UserDto;
import ch.hos6.openweb.urlShortener.domain.entity.User;
import org.mapstruct.Mapper;

/**
 * This interface defines the mapper for converting between User and UserDto objects.
 * It uses the MapStruct library to automatically generate the implementation at compile time.
 * MapStruct provides the @Mapper annotation to declare this interface as a mapper.
 * The componentModel attribute is set to "spring", so that the mapper can be managed as a Spring bean.
 *
 * @author Toubia Oussama
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a User entity to a UserDto object.
     * @param user the User entity
     * @return the corresponding UserDto object
     */
    UserDto userToDto(User user);
    /**
     * Converts a UserDto object to a User entity.
     * @param userDto the UserDto object
     * @return the corresponding User entity
     */
    User DtoToUser(UserDto userDto);
}
