package ru.goodgame.auth.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRepository implements IUserRepository {

    @Nonnull private static final QUsers USERS = QUsers.users;

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
        );
    }

    @Override
    public void saveRefreshToken(@Nonnull User user) {
//        factory.update(USERS)
//                .set(USERS.token, updateTokenField(user.getTokens()))
//                .where(USERS.id.eq(UUID.fromString(user.getId())))
//                .execute();
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
