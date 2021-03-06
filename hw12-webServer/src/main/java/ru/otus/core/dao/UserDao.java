package ru.otus.core.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

public interface UserDao {
    Optional<User> findById(long id);

    List<User> findAll();

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
