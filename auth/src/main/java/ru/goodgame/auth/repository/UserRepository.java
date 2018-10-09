package ru.goodgame.auth.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

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
        );
    }

}
