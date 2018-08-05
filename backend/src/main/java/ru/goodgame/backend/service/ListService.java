package ru.goodgame.backend.service;

import ru.goodgame.backend.dto.KanbanList;

import java.util.List;

public interface ListService {
    List<KanbanList> getAllLists(String userId);
    void deleteBy(Integer id);
    KanbanList add(Integer ordinal, String title);
}
