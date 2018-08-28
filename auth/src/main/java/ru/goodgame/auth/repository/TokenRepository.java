package ru.goodgame.auth.repository;

import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QTokens;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Service
public class TokenRepository implements ITokenRepository {


    @Nonnull private static final QTokens TOKENS = QTokens.tokens;

    @Nonnull private final SQLQueryFactory factory;

    public TokenRepository(@Nonnull SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    @Nonnull
    public List<String> findByUserId(@Nonnull UUID userId) {
        return factory.select(TOKENS.token)
                .from(TOKENS)
                .where(TOKENS.userId.eq(userId))
                .fetch();
    }

    @Override
    public void saveRefreshToken(@Nonnull UUID id, @Nonnull String refreshToken) {
        factory.insert(TOKENS)
                .columns(TOKENS.userId, TOKENS.token)
                .values(id, refreshToken)
                .execute();
    }

}
