package skislitsyn.jdbc.dao;

import java.sql.Connection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import skislitsyn.core.dao.AccountDao;
import skislitsyn.core.dao.AccountDaoException;
import skislitsyn.core.model.Account;
import skislitsyn.jdbc.mapper.JdbcMapperImpl;

public class AccountDaoJdbc implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<Account> dbExecutor;
    private final Class<Account> clazz = Account.class;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DbExecutorImpl<Account> dbExecutor) {
	this.sessionManager = sessionManager;
	this.dbExecutor = dbExecutor;
	jdbcMapper = new JdbcMapperImpl<Account>(sessionManager, dbExecutor, clazz);
    }

    @Override
    public Optional<Account> findByNo(long no) {
	try {
	    return Optional.ofNullable(jdbcMapper.findById(no, clazz));
	} catch (Exception e) {
	    throw new AccountDaoException(e);
	}
    }

    @Override
    public long insertAccount(Account account) {
	try {
	    jdbcMapper.insert(account);
	    return account.getNo();
	} catch (Exception e) {
	    throw new AccountDaoException(e);
	}
    }

    @Override
    public SessionManager getSessionManager() {
	return sessionManager;
    }

    private Connection getConnection() {
	return sessionManager.getCurrentSession().getConnection();
    }

    @Override
    public void updateAccount(Account account) {
	try {
	    jdbcMapper.update(account);
	} catch (Exception e) {
	    throw new AccountDaoException(e);
	}
    }

    @Override
    public void insertOrUpdate(Account account) {
	try {
	    jdbcMapper.insertOrUpdate(account);
	} catch (Exception e) {
	    throw new AccountDaoException(e);
	}
    }
}
