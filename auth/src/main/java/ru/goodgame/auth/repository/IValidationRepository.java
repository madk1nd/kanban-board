package ru.goodgame.auth.repository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IValidationRepository {
    void persistToken(@Nonnull String token,
                      @Nonnull String username);
    @Nonnull Optional<String> getBy(@Nonnull String token);
    void deleteBy(@Nonnull String token);
}
