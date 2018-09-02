package ru.goodgame.backend.da;

import com.querydsl.sql.SQLQueryFactory;
import ru.goodgame.backend.QLists;
import ru.goodgame.backend.dto.KanbanList;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListRepositoryImpl implements ListRepository {

    @Nonnull private final SQLQueryFactory factory;

    public ListRepositoryImpl(@Nonnull SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<KanbanList> getAllLists(String userId) {
        QLists lists = QLists.lists;
        return factory.select(lists.id, lists.ordinal, lists.title)
                .from(lists)
                .fetch()
                .stream()
                .map(KanbanList::from)
                .collect(toList());
    }

    @Override
    public void deleteBy(Integer id) {
        QLists lists = QLists.lists;
        factory.delete(lists)
                .where(lists.id.eq(id))
                .execute();
    }

    @Override
    public KanbanList add(Integer ordinal, String title) {
        QLists lists = QLists.lists;
        factory.insert(lists)
                .columns(lists.ordinal, lists.title)
                .values(ordinal, title)
                .execute();
        return factory.select(lists.id, lists.ordinal, lists.title)
                .from(lists)
                .where(lists.ordinal.eq(ordinal))
                .fetch()
                .stream()
                .map(KanbanList::from)
                .findFirst().orElse(null);
    }

    @Override
    public void update(List<KanbanList> kanbanLists) {
        QLists lists = QLists.lists;
        kanbanLists.forEach(kanbanList ->
                factory.update(lists)
                        .set(lists.ordinal, kanbanList.getOrdinal())
                        .where(lists.id.eq(kanbanList.getId()))
                        .execute());
    }
}
