package ru.otus.core.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
	this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
	try (var sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		var userId = userDao.insertUser(user);
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
	try (var sessionManager = userDao.getSessionManager()) {
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
    public void updateUser(User user) {
	try (var sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		userDao.updateUser(user);
		sessionManager.commitSession();

		logger.info("updated user: {}", user.getId());
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }

    @Override
    public void saveOrUpdateUser(User user) {
	try (var sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		userDao.insertOrUpdate(user);
		sessionManager.commitSession();

		logger.info("created or updated user: {}", user.getId());
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }
}
