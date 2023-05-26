package ch.hos6.openweb.urlShortener.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(@NotBlank(message = "Username is mandatory") String username,
                      @NotBlank(message = "Password is mandatory") String password) {
}
