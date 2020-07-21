package ru.otus.server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.Gson;

import ru.otus.core.service.DBServiceUser;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersServlet;
import skislitsyn.servlet.AdminServlet;

public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceUser dbServiceUser;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerSimple(int port, DBServiceUser dbServiceUser, Gson gson, TemplateProcessor templateProcessor) {
	this.dbServiceUser = dbServiceUser;
	this.gson = gson;
	this.templateProcessor = templateProcessor;
	server = new Server(port);
    }

    @Override
    public void start() throws Exception {
	if (server.getHandlers().length == 0) {
	    initContext();
	}
	server.start();
    }

    @Override
    public void join() throws Exception {
	server.join();
    }

    @Override
    public void stop() throws Exception {
	server.stop();
    }

    private Server initContext() {

	ResourceHandler resourceHandler = createResourceHandler();
	ServletContextHandler servletContextHandler = createServletContextHandler();

	HandlerList handlers = new HandlerList();
	handlers.addHandler(resourceHandler);
//	handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));
	Map<String, Boolean> paths = new HashMap<>();
	paths.put("/users", false);
	paths.put("/api/user/*", false);
	paths.put("/admin", true);
	servletContextHandler.setSecurityHandler((SecurityHandler) applySecurity(servletContextHandler, paths));
	handlers.addHandler(servletContextHandler);

	server.setHandler(handlers);
	return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
	return servletContextHandler;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, Map<String, Boolean> paths) {
	return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
	ResourceHandler resourceHandler = new ResourceHandler();
	resourceHandler.setDirectoriesListed(false);
	resourceHandler.setWelcomeFiles(new String[] { START_PAGE_NAME });
	resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
	return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
	ServletContextHandler servletContextHandler = new ServletContextHandler(
		ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
	servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, dbServiceUser)),
		"/users");
	servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(dbServiceUser, gson)), "/api/user/*");
	servletContextHandler.addServlet(new ServletHolder(new AdminServlet(templateProcessor, dbServiceUser)),
		"/admin");
	return servletContextHandler;
    }
}