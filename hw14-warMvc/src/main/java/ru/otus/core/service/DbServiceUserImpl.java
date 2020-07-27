package ru.otus.core.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

@Service
public class DbServiceUserImpl implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
	this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
	try (SessionManager sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		userDao.insertOrUpdate(user);
		long userId = user.getId();
		sessionManager.commitSession();

		logger.info("created user: {}", userId);
		return userId;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }

    @Override
    public Optional<User> getUser(long id) {
	try (SessionManager sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		Optional<User> userOptional = userDao.findById(id);

		logger.info("user: {}", userOptional.orElse(null));
		return userOptional;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
	    }
	    return Optional.empty();
	}
    }

    @Override
    public List<User> getUsers() {
	try (SessionManager sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		List<User> users = userDao.findAll();

		logger.info("users: {}", users);
		return users;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
	    }
	    return null;
	}
    }
}
