package ru.goodgame.auth.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QTokens;
import ru.goodgame.auth.model.Token;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Service
public class TokenRepository implements ITokenRepository {

    @Nonnull private static final QTokens TOKENS = QTokens.tokens;

    @Nonnull private final SQLQueryFactory factory;

    public TokenRepository(@Nonnull final SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Nonnull
    @Override
    public List<Token> getUserTokens(@Nonnull final UUID userId) {
        return factory.select(
                        Projections.constructor(
                                Token.class,
                                TOKENS.id,
                                TOKENS.userId,
                                TOKENS.token,
                                TOKENS.remoteIp
                        )
                )
                .from(TOKENS)
                .where(TOKENS.userId.eq(userId))
                .fetch();
    }

    @Override
    public void saveRefreshToken(@Nonnull final User user,
                                 @Nonnull final String refreshToken,
                                 @Nonnull final String remoteHost) {
        factory.insert(TOKENS)
                .columns(TOKENS.userId, TOKENS.token, TOKENS.remoteIp)
                .values(user.getId(), refreshToken, remoteHost)
                .execute();
    }

    @Override
    public void updateRefreshToken(@Nonnull final User user,
                                   @Nonnull final String refreshToken,
                                   @Nonnull final String remoteHost) {
        factory.update(TOKENS)
                .set(TOKENS.token, refreshToken)
                .where(
                        TOKENS.userId.eq(user.getId())
                                .and(TOKENS.remoteIp.eq(remoteHost))
                )
                .execute();
    }
}
