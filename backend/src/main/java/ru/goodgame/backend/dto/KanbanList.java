package ru.goodgame.backend.dto;

import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KanbanList {
    private Integer id;
    private Integer ordinal;
    private String title;

    public KanbanList() {}

    public static KanbanList from(Tuple tuple) {
        return new KanbanList(
                tuple.get(0, Integer.class),
                tuple.get(1, Integer.class),
                tuple.get(2, String.class)
        );
    }
}
