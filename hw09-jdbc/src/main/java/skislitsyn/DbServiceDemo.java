package skislitsyn;

import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import skislitsyn.core.model.Account;
import skislitsyn.core.service.DBServiceAccount;
import skislitsyn.core.service.DbServiceAccountImpl;
import skislitsyn.jdbc.dao.AccountDaoJdbc;
import skislitsyn.jdbc.dao.UserDaoJdbc;
import skislitsyn.jdbc.mapper.EntityClassMetaDataImpl;
import skislitsyn.jdbc.mapper.EntitySQLMetaDataImpl;
import skislitsyn.jdbc.mapper.JdbcMapperImpl;

public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
	var dataSource = new DataSourceH2();
	var demo = new DbServiceDemo();

	demo.createTables(dataSource);

	var sessionManager = new SessionManagerJdbc(dataSource);
	DbExecutorImpl<User> dbExecutorUser = new DbExecutorImpl<>();
	EntityClassMetaData<User> entityClassMetaDataUser = new EntityClassMetaDataImpl<>(User.class);
	EntitySQLMetaData entitySQLMetaDataUser = new EntitySQLMetaDataImpl<User>(entityClassMetaDataUser);
	JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<User>(sessionManager, dbExecutorUser, User.class,
		entityClassMetaDataUser, entitySQLMetaDataUser);
	var userDao = new UserDaoJdbc(sessionManager, jdbcMapperUser);

	var dbServiceUser = new DbServiceUserImpl(userDao);
	demo.testUserService(dbServiceUser);

	DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
	EntityClassMetaData<Account> entityClassMetaDataAccount = new EntityClassMetaDataImpl<>(Account.class);
	EntitySQLMetaData entitySQLMetaDataAccount = new EntitySQLMetaDataImpl<Account>(entityClassMetaDataAccount);
	JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<Account>(sessionManager, dbExecutorAccount,
		Account.class, entityClassMetaDataAccount, entitySQLMetaDataAccount);
	var accountDao = new AccountDaoJdbc(sessionManager, jdbcMapperAccount);

	var dbServiceAccount = new DbServiceAccountImpl(accountDao);
	demo.testAccountService(dbServiceAccount);
    }

    private void createTables(DataSource dataSource) throws SQLException {
	try (var connection = dataSource.getConnection();
		var pst = connection.prepareStatement(
			"create table user(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
	    pst.executeUpdate();
	}
	try (var connection = dataSource.getConnection();
		var pst = connection.prepareStatement(
			"create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
	    pst.executeUpdate();
	}
	System.out.println("tables created");
    }

    private void testUserService(DBServiceUser dbServiceUser) {
	var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 35));
	Optional<User> user = dbServiceUser.getUser(id);

	user.ifPresentOrElse(crUser -> logger.info("created user, id:{}, name:{}, age:{}", crUser.getId(),
		crUser.getName(), String.valueOf(crUser.getAge())), () -> logger.info("user was not created"));

	var id2 = dbServiceUser.saveUser(new User(0, "dbServiceUser2", 30));
	Optional<User> user2 = dbServiceUser.getUser(id2);

	user2.ifPresentOrElse(crUser -> logger.info("created user, id:{}, name:{}, age:{}", crUser.getId(),
		crUser.getName(), String.valueOf(crUser.getAge())), () -> logger.info("user was not created"));

	dbServiceUser.updateUser(new User(id, "dbServiceUser", 36));
	user = dbServiceUser.getUser(id);

	user.ifPresentOrElse(crUser -> logger.info("updated user, id:{}, name:{}, age:{}", crUser.getId(),
		crUser.getName(), String.valueOf(crUser.getAge())), () -> logger.info("user was not updated"));

	dbServiceUser.saveOrUpdateUser(new User(id2, "dbServiceUser2", 31));
	user2 = dbServiceUser.getUser(id2);

	user2.ifPresentOrElse(
		crUser -> logger.info("created or updated user, id:{}, name:{}, age:{}", crUser.getId(),
			crUser.getName(), String.valueOf(crUser.getAge())),
		() -> logger.info("user was not created nor updated"));

	dbServiceUser.saveOrUpdateUser(new User(3, "dbServiceUser3", 45));
	Optional<User> user3 = dbServiceUser.getUser(3);

	user3.ifPresentOrElse(
		crUser -> logger.info("created or updated user, id:{}, name:{}, age:{}", crUser.getId(),
			crUser.getName(), String.valueOf(crUser.getAge())),
		() -> logger.info("user was not created nor updated"));

    }

    private void testAccountService(DBServiceAccount dbServiceAccount) {
	var no = dbServiceAccount.saveAccount(new Account(0, "dbServiceAccount", 100.50));
	Optional<Account> account = dbServiceAccount.getAccount(no);

	account.ifPresentOrElse(
		crAccount -> logger.info("created account, no:{}, type:{}, rest:{}", crAccount.getNo(),
			crAccount.getType(), String.valueOf(crAccount.getRest())),
		() -> logger.info("account was not created"));

	var no2 = dbServiceAccount.saveAccount(new Account(0, "dbServiceAccount2", 200.00));
	Optional<Account> account2 = dbServiceAccount.getAccount(no2);

	account2.ifPresentOrElse(
		crAccount -> logger.info("created account, no:{}, type:{}, rest:{}", crAccount.getNo(),
			crAccount.getType(), String.valueOf(crAccount.getRest())),
		() -> logger.info("account was not created"));

	dbServiceAccount.updateAccount(new Account(no, "dbServiceAccount", 101.00));
	account = dbServiceAccount.getAccount(no);

	account.ifPresentOrElse(
		crAccount -> logger.info("updated account, no:{}, type:{}, rest:{}", crAccount.getNo(),
			crAccount.getType(), String.valueOf(crAccount.getRest())),
		() -> logger.info("account was not updated"));

	dbServiceAccount.saveOrUpdateAccount(new Account(no2, "dbServiceAccount2", 200.70));
	account2 = dbServiceAccount.getAccount(no2);

	account2.ifPresentOrElse(
		crAccount -> logger.info("created or updated account, no:{}, type:{}, rest:{}", crAccount.getNo(),
			crAccount.getType(), String.valueOf(crAccount.getRest())),
		() -> logger.info("account was not created nor updated"));

	dbServiceAccount.saveOrUpdateAccount(new Account(3, "dbServiceAccount3", 310.30));
	Optional<Account> account3 = dbServiceAccount.getAccount(3);

	account3.ifPresentOrElse(
		crAccount -> logger.info("created or updated account, no:{}, type:{}, rest:{}", crAccount.getNo(),
			crAccount.getType(), String.valueOf(crAccount.getRest())),
		() -> logger.info("account was not created nor updated"));

    }
}
