package ru.goodgame.auth.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class AuthRepository implements IAuthRepository {

    @Nonnull private static final QUsers USERS = QUsers.users;

    @Nonnull private final SQLQueryFactory factory;

    public AuthRepository(@Nonnull SQLQueryFactory factory) {
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
                                USERS.password,
                                USERS.token
                        )
                )
                        .from(USERS)
                        .where(USERS.username.eq(username))
                        .fetchOne()
        );
    }

}
