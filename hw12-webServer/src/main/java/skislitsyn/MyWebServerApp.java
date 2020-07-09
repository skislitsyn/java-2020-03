package skislitsyn;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import skislitsyn.core.model.Address;
import skislitsyn.core.model.Phone;
import skislitsyn.server.UsersWebServerWithFormSecurity;

public class MyWebServerApp {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
	SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class,
		Address.class, Phone.class);
	SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
	UserDao userDao = new UserDaoHibernate(sessionManager);
	Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

	String hashLoginServiceConfigPath = FileSystemHelper
		.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
	LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
	// LoginService loginService = new InMemoryLoginServiceImpl(userDao);

	UsersWebServer usersWebServer = new UsersWebServerWithFormSecurity(WEB_SERVER_PORT, loginService, userDao, gson,
		templateProcessor);

	usersWebServer.start();
	usersWebServer.join();
    }
}
