package ch.hos6.openweb.urlShortener.utils;

import ch.hos6.openweb.urlShortener.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Utility class for generating JSON Web Tokens (JWTs) for user authentication.
 * This class uses a JwtEncoder to encode the JWTs.
 *
 * @author Toubia Oussama
 */
@Service
public class JwtTokenUtils {
    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Constructs a JwtTokenUtils with the provided JwtEncoder.
     *
     * @param jwtEncoder the encoder to use for generating JWTs
     */
    public JwtTokenUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT for the provided user.
     * The JWT is issued by "self", includes the user's username and ID as claims,
     * and is valid from the current time until the current time plus the configured JWT expiration time.
     *
     * @param user the user for which to generate a JWT
     * @return a string representing the encoded JWT
     */
    public String token(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiration))
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


}
