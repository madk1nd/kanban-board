package ru.goodgame.auth.repository;

import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IUserRepository {
    @Nonnull Optional<User> findByUsername(@Nonnull String username);
    void saveRefreshToken(@Nonnull User user, String refreshToken, String remoteHost);
    void updateRefreshToken(@Nonnull User user, String refreshToken, String remoteHost);
}
