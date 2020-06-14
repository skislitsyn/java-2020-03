package skislitsyn.core.dao;

import java.util.Optional;

import ru.otus.core.sessionmanager.SessionManager;
import skislitsyn.core.model.Account;

public interface AccountDao {
    Optional<Account> findByNo(long no);

    long insertAccount(Account account);

    void updateAccount(Account account);

    void insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
