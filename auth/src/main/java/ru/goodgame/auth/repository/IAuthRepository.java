package ru.goodgame.auth.repository;

import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IAuthRepository {
    @Nonnull Optional<User> findByUsername(@Nonnull String username);
}
