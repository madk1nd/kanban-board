package ru.goodgame.auth.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QTokens;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.Token;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRepository implements IUserRepository {

    @Nonnull private static final QUsers USERS = QUsers.users;
    @Nonnull private static final QTokens TOKENS = QTokens.tokens;

    @Nonnull private final SQLQueryFactory factory;

    public UserRepository(@Nonnull SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Optional<User> findByUsername(@Nonnull String username) {
        return Optional.ofNullable(
                factory.select(
                        Projections.constructor(
                                User.class,
                                USERS.id,
                                USERS.username,
                                USERS.password
                        )
                )
                        .from(USERS)
                        .where(USERS.username.eq(username))
                        .fetchOne()
        ).map(user -> {
            List<Token> tokens = factory
                    .select(
                            Projections.constructor(
                                    Token.class,
                                    TOKENS.id,
                                    TOKENS.userId,
                                    TOKENS.token,
                                    TOKENS.remoteIp
                            )
                    )
                    .from(TOKENS)
                    .where(TOKENS.userId.eq(user.getId()))
                    .fetch();
            user.getTokens().addAll(tokens);
            return user;
        });
    }

    @Nonnull
    @Override
    public Optional<User> findByUserId(@Nonnull String userId) {
        return Optional.ofNullable(
                factory.select(
                        Projections.constructor(
                                User.class,
                                USERS.id,
                                USERS.username,
                                USERS.password
                        )
                )
                        .from(USERS)
                        .where(USERS.id.eq(UUID.fromString(userId)))
                        .fetchOne()
        ).map(user -> {
            List<Token> tokens = factory
                    .select(
                            Projections.constructor(
                                    Token.class,
                                    TOKENS.id,
                                    TOKENS.userId,
                                    TOKENS.token,
                                    TOKENS.remoteIp
                            )
                    )
                    .from(TOKENS)
                    .where(TOKENS.userId.eq(user.getId()))
                    .fetch();
            user.getTokens().addAll(tokens);
            return user;
        });
    }

    @Override
    public void saveRefreshToken(@Nonnull User user, String refreshToken, String remoteHost) {
        factory.insert(TOKENS)
                .columns(TOKENS.userId, TOKENS.token, TOKENS.remoteIp)
                .values(user.getId(), refreshToken, remoteHost)
                .execute();
    }

    @Override
    public void updateRefreshToken(@Nonnull User user, String refreshToken, String remoteHost) {
        factory.update(TOKENS)
                .set(TOKENS.token, refreshToken)
                .where(TOKENS.userId.eq(user.getId()).and(TOKENS.remoteIp.eq(remoteHost)))
                .execute();
    }

}
