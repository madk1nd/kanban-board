package ru.goodgame.backend.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodgame.backend.service.ListService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/list")
public class ListController {

    @Nonnull private final ListService listService;

    public ListController(@Nonnull ListService listService) {
        this.listService = listService;
    }

    @PostMapping("/all")
    @ResponseBody
    public ResponseEntity getAllLists(@RequestParam("userId") String userId) {
        return new ResponseEntity<>(listService.getAllLists(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity getAllLists(@RequestParam("id") Integer id) {
        listService.deleteBy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
