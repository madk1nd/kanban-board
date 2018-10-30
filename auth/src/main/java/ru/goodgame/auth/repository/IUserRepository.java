package ru.goodgame.auth.repository;

import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IUserRepository {
    @Nonnull Optional<User> findByUsername(@Nonnull String username);
    @Nonnull Optional<User> findByUserId(@Nonnull String userIdFrom);
    void persistUser(@Nonnull String email, @Nonnull String password, @Nonnull String name);
    void enableUser(@Nonnull String username);
}
