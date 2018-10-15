package ru.goodgame.auth.utils;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.UUID;

public interface ITokenBuilder {
    @Nonnull String buildToken(@Nonnull UUID userId, @Nonnull Instant time);
    boolean invalid(@Nonnull String token);
    @Nonnull String getClaim(@Nonnull String token, @Nonnull String claimName);
}
