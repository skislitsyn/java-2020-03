package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ru.otus.core.service.DBServiceUser;

@Controller
public class UserController {

    private final DBServiceUser dbServiceUser;

    public UserController(DBServiceUser dbServiceUser) {
	this.dbServiceUser = dbServiceUser;
    }

    @GetMapping("/")
    public String indexView(Model model) {
	return "index.html";
    }

    @GetMapping("/users")
    public String usersView(Model model) {
	return "users.html";
    }

}
