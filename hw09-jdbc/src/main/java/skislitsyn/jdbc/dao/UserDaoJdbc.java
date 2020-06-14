package skislitsyn.jdbc.dao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class UserDaoJdbc implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final Class<User> clazz = User.class;
    private final JdbcMapper<User> jdbcMapper;

    public UserDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<User> jdbcMapper) {
	this.sessionManager = sessionManager;
	this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
	try {
	    return Optional.ofNullable(jdbcMapper.findById(id, clazz));
	} catch (Exception e) {
	    throw new UserDaoException(e);
	}
    }

    @Override
    public long insertUser(User user) {
	try {
	    jdbcMapper.insert(user);
	    return user.getId();
	} catch (Exception e) {
	    throw new UserDaoException(e);
	}
    }

    @Override
    public SessionManager getSessionManager() {
	return sessionManager;
    }

    @Override
    public void updateUser(User user) {
	try {
	    jdbcMapper.update(user);
	} catch (Exception e) {
	    throw new UserDaoException(e);
	}
    }

    @Override
    public void insertOrUpdate(User user) {
	try {
	    jdbcMapper.insertOrUpdate(user);
	} catch (Exception e) {
	    throw new UserDaoException(e);
	}
    }
}
