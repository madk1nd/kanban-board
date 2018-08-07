package ru.goodgame.backend.da;

import ru.goodgame.backend.dto.KanbanList;

import java.util.List;

public interface ListRepository {
    List<KanbanList> getAllLists(String userId);
    void deleteBy(Integer id);
    KanbanList add(Integer ordinal, String title);
    void update(List<KanbanList> kanbanLists);
}
