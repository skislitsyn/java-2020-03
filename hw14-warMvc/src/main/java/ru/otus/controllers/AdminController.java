package ru.otus.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

@Controller
public class AdminController {

    private final DBServiceUser dbServiceUser;

    public AdminController(DBServiceUser dbServiceUser) {
	this.dbServiceUser = dbServiceUser;
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
	List<User> users = dbServiceUser.getUsers();
	model.addAttribute("users", users);
	return "admin.html";
    }

}
