package ru.goodgame.auth.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private final String id;
    private final String username;
    private final String password;
    private final Map<String, String> tokens;

    public User(Object id, String username, String password, Object token) {
        this.id = id.toString();
        this.username = username;
        this.password = password;
        this.tokens = getTokens((PGobject) token);
    }

    private Map<String, String> getTokens(PGobject pGobject) {
        String value = pGobject.getValue();
        try {
            return new ObjectMapper().readValue(value, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class));
        } catch (IOException e) {
//            log.error
            return new HashMap<>();
        }
    }
}
