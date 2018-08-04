package ru.goodgame.backend.dto;

import com.querydsl.core.Tuple;
import lombok.Data;
import ru.goodgame.backend.QLists;

import javax.annotation.Nonnull;

@Data
public class KanbanList {
    @Nonnull private Integer id;
    @Nonnull private Integer ordinal;
    @Nonnull private String title;

    public static KanbanList from(Tuple tuple) {
        return new KanbanList(
                tuple.get(0, Integer.class),
                tuple.get(1, Integer.class),
                tuple.get(2, String.class)
        );
    }
}
