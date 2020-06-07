package skislitsyn.core.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.service.DbServiceException;
import skislitsyn.core.dao.AccountDao;
import skislitsyn.core.model.Account;

public class DbServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
	this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
	try (var sessionManager = accountDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		var accountNo = accountDao.insertAccount(account);
		sessionManager.commitSession();

		logger.info("created account: {}", accountNo);
		return accountNo;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }

    @Override
    public Optional<Account> getAccount(long no) {
	try (var sessionManager = accountDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		Optional<Account> accountOptional = accountDao.findByNo(no);

		logger.info("account: {}", accountOptional.orElse(null));
		return accountOptional;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
	    }
	    return Optional.empty();
	}
    }

    @Override
    public void updateAccount(Account account) {
	try (var sessionManager = accountDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		accountDao.updateAccount(account);
		sessionManager.commitSession();

		logger.info("updated account: {}", account.getNo());
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }

    @Override
    public void saveOrUpdateAccount(Account account) {
	try (var sessionManager = accountDao.getSessionManager()) {
	    sessionManager.beginSession();
	    try {
		accountDao.insertOrUpdate(account);
		sessionManager.commitSession();

		logger.info("created or updated account: {}", account.getNo());
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		sessionManager.rollbackSession();
		throw new DbServiceException(e);
	    }
	}
    }
}
