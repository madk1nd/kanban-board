package ru.goodgame.backend.service;

import org.springframework.stereotype.Service;
import ru.goodgame.backend.da.ListRepository;
import ru.goodgame.backend.dto.KanbanList;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class ListServiceImpl implements ListService {

    @Nonnull private final ListRepository listRepository;

    public ListServiceImpl(@Nonnull ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    public List<KanbanList> getAllLists(String userId) {
        return listRepository.getAllLists(userId);
    }

    @Override
    public void deleteBy(Integer id) {
        listRepository.deleteBy(id);
    }

    @Override
    public KanbanList add(Integer ordinal, String title) {
        return listRepository.add(ordinal, title);
    }

    @Override
    public void udpate(List<KanbanList> kanbanLists) {
        listRepository.update(kanbanLists);
    }
}
