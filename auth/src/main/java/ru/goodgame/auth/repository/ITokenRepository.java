package ru.goodgame.auth.repository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface ITokenRepository {
    @Nonnull List<String> findByUserId(@Nonnull UUID userId);
    void saveRefreshToken(@Nonnull UUID id, @Nonnull String refreshToken);
    void delete(@Nonnull String token);
    void deleteAllBy(@Nonnull UUID userId);
}
