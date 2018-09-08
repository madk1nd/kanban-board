package ru.goodgame.backend.service;

import io.vertx.ext.mongo.MongoClient;
import ru.goodgame.backend.dto.KanbanList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ListServiceImpl implements ListService {

    @Nonnull private final MongoClient client;

    public ListServiceImpl(@Nonnull MongoClient client) {
        this.client = client;
    }

    @Override
    public List<KanbanList> getAllLists(String userId) {
        return new ArrayList<>();
    }

    @Override
    public void deleteBy(Integer id) {
    }

    @Override
    public KanbanList add(Integer ordinal, String title) {
        return null;
    }

    @Override
    public void udpate(List<KanbanList> kanbanLists) {

    }
}
