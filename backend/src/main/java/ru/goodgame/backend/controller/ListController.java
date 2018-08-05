package ru.goodgame.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodgame.backend.service.ListService;

import javax.annotation.Nonnull;

//@CrossOrigin
@RestController
@RequestMapping("/api/list")
public class ListController {

    @Nonnull private final ListService listService;

    public ListController(@Nonnull ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/all")
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

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity add(@RequestParam("ordinal") Integer ordinal,
                              @RequestParam("title") String title) {
        return new ResponseEntity<>(listService.add(ordinal, title), HttpStatus.OK);
    }


}
