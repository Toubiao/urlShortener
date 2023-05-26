package ch.hos6.openweb.urlShortener.domain.repository;

import ch.hos6.openweb.urlShortener.domain.entity.User;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * This interface defines the repository for managing Users in the MongoDB database.
 * It extends the MongoRepository interface provided by Spring Data MongoDB.
 * It provides a method for finding a User by username.
 *
 * @author Toubia Oussama
 */
public interface UserRepository extends MongoRepository<User,String> {

    /**
     * Retrieves a User by their username.
     * @param username the username of the User
     * @return an Optional containing the User if they exist, empty otherwise
     */
    Optional<User> findByUsername(@NonNull String username);

}
