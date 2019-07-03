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

    public UserRepository(@Nonnull final SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Optional<User> findByUsername(@Nonnull final String username) {
        return Optional.ofNullable(
                factory.select(
                        Projections.constructor(
                                User.class,
                                USERS.id,
                                USERS.username,
                                USERS.password,
                                USERS.enabled
                        )
                )
                        .from(USERS)
                        .where(USERS.username.eq(username))
                        .fetchOne()
        );
    }

    @Nonnull
    @Override
    public Optional<User> findByUserId(@Nonnull final String userId) {
        return Optional.ofNullable(
                factory.select(
                        Projections.constructor(
                                User.class,
                                USERS.id,
                                USERS.username,
                                USERS.password,
                                USERS.enabled
                        )
                )
                        .from(USERS)
                        .where(USERS.id.eq(UUID.fromString(userId)))
                        .fetchOne()
        );
    }

    @Override
    public void persistUser(@Nonnull final String email,
                            @Nonnull final String password,
                            @Nonnull final String name) {
        factory.insert(USERS)
                .columns(USERS.username, USERS.password, USERS.name)
                .values(email, password, name)
                .execute();
    }

    @Override
    public void enableUser(@Nonnull final String username) {
        factory.update(USERS)
                .set(USERS.enabled, true)
                .where(USERS.username.eq(username))
                .execute();
    }

}
