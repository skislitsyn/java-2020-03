package skislitsyn.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import com.google.gson.Gson;

import ru.otus.core.dao.UserDao;
import ru.otus.server.UsersWebServerSimple;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.LoginServlet;

public class UsersWebServerWithFormSecurity extends UsersWebServerSimple {
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";

    private final LoginService loginService;

    public UsersWebServerWithFormSecurity(int port, LoginService loginService, UserDao userDao, Gson gson,
	    TemplateProcessor templateProcessor) {
	super(port, userDao, gson, templateProcessor);
	this.loginService = loginService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, Map<String, Boolean> paths) {
	servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/login");

	Constraint userConstraint = new Constraint();
	userConstraint.setName(Constraint.__FORM_AUTH);
	userConstraint.setAuthenticate(true);
	userConstraint.setRoles(new String[] { ROLE_NAME_USER, ROLE_NAME_ADMIN });

	Constraint adminConstraint = new Constraint();
	adminConstraint.setName(Constraint.__FORM_AUTH);
	adminConstraint.setAuthenticate(true);
	adminConstraint.setRoles(new String[] { ROLE_NAME_ADMIN });

	List<ConstraintMapping> constraintMappings = new ArrayList<>();
	for (String path : paths.keySet()) {
	    ConstraintMapping mapping = new ConstraintMapping();
	    mapping.setPathSpec(path);
	    mapping.setConstraint(isPrivileged(paths, path) ? adminConstraint : userConstraint);
	    constraintMappings.add(mapping);
	}

	ConstraintSecurityHandler security = new ConstraintSecurityHandler();
	FormAuthenticator authenticator = new FormAuthenticator("/login", "/login", false);
	security.setAuthenticator(authenticator);

	security.setLoginService(loginService);
	security.setConstraintMappings(constraintMappings);
//	security.setHandler(new HandlerList(servletContextHandler));

	return security;

    }

    private boolean isPrivileged(Map<String, Boolean> paths, String path) {
	return paths.get(path);
    }
}
