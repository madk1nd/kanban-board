package ru.goodgame.auth.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QUsers;
import ru.goodgame.auth.model.User;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthRepository implements IAuthRepository {

    @Nonnull private static final QUsers USERS = QUsers.users;

    @Nonnull private final SQLQueryFactory factory;
    @Nonnull private final ObjectMapper mapper;

    public AuthRepository(@Nonnull SQLQueryFactory factory, @Nonnull ObjectMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public String getUser() {
        User user = factory
                .select(
                        Projections.constructor(
                                User.class,
                                USERS.id,
                                USERS.username,
                                USERS.password,
                                USERS.token
                        )
                )
                .from(USERS)
                .where(USERS.id.eq(UUID.fromString("a64515c3-14aa-4760-89b9-23f747387ec3")))
                .fetchOne();
//        a18275d1-14aa-4760-89b9-23f747387ec3

        System.out.println(user);
        System.out.println(user.getTokens().get("test"));
//        PGobject pGobject = (PGobject) fetch.get(0).get(USERS.token);

//        System.out.println(pGobject);
//        Map<String, String> tokens = getTokens(pGobject);
//        System.out.println(tokens);

        return "test";
    }


}
