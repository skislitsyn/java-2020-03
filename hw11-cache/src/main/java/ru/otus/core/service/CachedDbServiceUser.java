package ru.otus.core.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

public class CachedDbServiceUser implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<Long, User> userCache = new MyCache<>();

    public CachedDbServiceUser(UserDao userDao) {
	this.userDao = userDao;
	registerListener();
    }

    @Override
    public long saveUser(User user) {
	try (SessionManager sessionManager = userDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		userDao.insertOrUpdate(user);
		long userId = user.getId();
		sessionManager.commitSession();

		userCache.put(userId, user);

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
	User cachedUser = userCache.get(id);
	if (cachedUser == null) {
	    try (SessionManager sessionManager = userDao.getSessionManager()) {
		sessionManager.beginSession();
		try {
		    Optional<User> userOptional = userDao.findById(id);

		    if (userOptional.isPresent()) {
			userCache.put(id, userOptional.get());
		    }

		    logger.info("user: {}", userOptional.orElse(null));
		    return userOptional;
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		    sessionManager.rollbackSession();
		}
		return Optional.empty();
	    }
	} else {
	    return Optional.of(cachedUser);
	}
    }

    private void registerListener() {
	HwListener<Long, User> listener = new HwListener<>() {
	    @Override
	    public void notify(Long key, User value, String action) {
		logger.info("key:{}, value:{}, action: {}", key, value, action);
	    }
	};
	userCache.addListener(listener);
    }
}
