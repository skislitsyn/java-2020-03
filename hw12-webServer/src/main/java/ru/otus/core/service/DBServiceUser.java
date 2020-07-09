package ru.otus.core.service;

import java.util.List;
import java.util.Optional;

import ru.otus.core.model.User;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getUsers();

}
