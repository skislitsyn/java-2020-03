package skislitsyn;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.CachedDbServiceUser;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import skislitsyn.core.model.Address;
import skislitsyn.core.model.Phone;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);
    private static final int ITERATION_COUNT = 100;

    public static void main(String[] args) {
	SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class,
		Address.class, Phone.class);

	SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
	UserDao userDao = new UserDaoHibernate(sessionManager);
	DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
	DBServiceUser cachedDBServiceUser = new CachedDbServiceUser(userDao);

	long id = cachedDBServiceUser.saveUser(new User(0, "Вася"));

	long startTime = System.currentTimeMillis();
	for (int i = 0; i < ITERATION_COUNT; i++) {
	    Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);
	}
	long endTime = System.currentTimeMillis();

	long cachedStartTime = System.currentTimeMillis();
	for (int i = 0; i < ITERATION_COUNT; i++) {
	    Optional<User> mayBeCreatedUser = cachedDBServiceUser.getUser(id);
	}
	long cachedEndTime = System.currentTimeMillis();

	logger.info("--------------------------------------------------");
	logTimings(startTime, endTime, "without caching");
	logTimings(cachedStartTime, cachedEndTime, "with caching");
	logger.info("This time test with caching {} times faster",
		(endTime - startTime) / (cachedEndTime - cachedStartTime));

	System.gc();
	logger.info("--------------------------------------------------");
	logger.info("Now the cache should be empty and we should go to db");
	Optional<User> mayBeCreatedUser = cachedDBServiceUser.getUser(id);
    }

    private static void logTimings(long startTime, long endTime, String test) {
	logger.info("Get user {} times {} in: {} ms", ITERATION_COUNT, test, endTime - startTime);
    }
}
