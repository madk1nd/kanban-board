package ru.goodgame.auth.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QTokens;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            Map<String, String> transform = factory//.select(TOKENS.remoteIp, TOKENS.token)
                    .from(TOKENS)
                    .where(TOKENS.userId.eq(user.getId()))
                    .transform(GroupBy.groupBy(TOKENS.remoteIp).as(TOKENS.token));
            System.out.println(transform);
            user.getTokens().putAll(transform);
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

    private Object updateTokenField(Map<String, String> tokens) {
        PGobject json = new PGobject();
        json.setType("json");
        try {
            json.setValue(
                    tokens.entrySet().stream()
                    .map(pair -> "\"" + pair.getKey() + "\": \"" + pair.getValue() + "\"")
                    .collect(Collectors.joining(", ", "{", "}"))
            );
        } catch (SQLException e) {
            // log
            e.printStackTrace();
        }
        System.out.println(json);
        return json;

    }

}
