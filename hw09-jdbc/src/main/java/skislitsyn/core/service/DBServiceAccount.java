package skislitsyn.core.service;

import java.util.Optional;

import skislitsyn.core.model.Account;

public interface DBServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long no);

    void updateAccount(Account account);

    void saveOrUpdateAccount(Account account);
}
