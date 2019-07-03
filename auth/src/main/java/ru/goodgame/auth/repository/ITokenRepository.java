package ru.goodgame.auth.repository;

import ru.goodgame.auth.model.Token;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface ITokenRepository {
    @Nonnull List<Token> getUserTokens(@Nonnull UUID userId);
    void saveRefreshToken(@Nonnull User user,
                          @Nonnull String refreshToken,
                          @Nonnull String remoteHost);
    void updateRefreshToken(@Nonnull User user,
                            @Nonnull String refreshToken,
                            @Nonnull String remoteHost);
}
