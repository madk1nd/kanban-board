package ru.goodgame.auth.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQueryFactory;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QUsers;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthRepository implements IAuthRepository {

    @Nonnull private final SQLQueryFactory factory;
    @Nonnull private final ObjectMapper mapper;

    public AuthRepository(@Nonnull SQLQueryFactory factory, @Nonnull ObjectMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public String getUser() {
        List<Tuple> fetch = factory
                .select(
                    QUsers.users.id,
                    QUsers.users.username,
                    QUsers.users.password,
                    QUsers.users.token
                )
                .from(QUsers.users)
                .fetch();

        System.out.println(fetch);
        PGobject pGobject = (PGobject) fetch.get(0).get(QUsers.users.token);

        System.out.println(pGobject);
        Map<String, String> tokens = getTokens(pGobject);
        System.out.println(tokens);

        return "test";
    }

    private Map<String, String> getTokens(PGobject pGobject) {
        String value = pGobject.getValue();
        try {
            return mapper.readValue(value, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class));
        } catch (IOException e) {
//            log.error
            return new HashMap<>();
        }
    }
}
