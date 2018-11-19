package ru.goodgame.backend.dto;

import io.vertx.core.json.JsonObject;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Board {
    private final String id;
    private final String name;

    @Nonnull
    public JsonObject toUpdate() {
        return new JsonObject()
                .put("_id", new JsonObject().put("$oid", id))
                .put("name", name);
    }

    @Nonnull
    public JsonObject toDelete() {
        return new JsonObject()
                .put("_id", new JsonObject().put("$oid", id));
    }

    @Nonnull
    public JsonObject toJson() {
        return new JsonObject()
                .put("id", id)
                .put("name", name);
    }

    @Nonnull
    public static List<Board> fromJson(@Nullable JsonObject resultSet) {
        if (resultSet == null) {
            return Collections.emptyList();
        } else {
            return resultSet.getJsonArray("boards").stream()
                    .filter(o -> o instanceof JsonObject)
                    .map(o -> (JsonObject) o)
                    .map(json -> new Board(
                            json.getJsonObject("_id").getString("$oid"),
                            json.getString("name")
                    ))
                    .collect(Collectors.toList());
        }
    }
}
