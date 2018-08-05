package ru.goodgame.backend.controller;

import com.querydsl.sql.SQLQueryFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.goodgame.backend.QUsers;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final SQLQueryFactory factory;

    public UserController(SQLQueryFactory factory) {
        this.factory = factory;
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity getUserList() {
        QUsers users = QUsers.users;
        List<String> list = factory.select(users.username)
                .from(users)
                .fetch();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
