package ru.otus.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

@RestController
public class UserRestController {

    private final DBServiceUser dbServiceUser;

    public UserRestController(DBServiceUser dbServiceUser) {
	this.dbServiceUser = dbServiceUser;
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable(name = "id") long id) {
	return dbServiceUser.getUser(id).orElse(null);
    }

    @PostMapping("/api/user")
    public User saveUser(@RequestBody User user) {
	dbServiceUser.saveUser(user);
	return user;
    }

}
