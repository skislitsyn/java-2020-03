package skislitsyn.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.services.TemplateProcessor;

public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public AdminServlet(TemplateProcessor templateProcessor, UserDao userDao) {
	this.templateProcessor = templateProcessor;
	this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
	Map<String, Object> paramsMap = new HashMap<>();
	DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
	List<User> users = dbServiceUser.getUsers();
	paramsMap.put(TEMPLATE_ATTR_USERS, users);

	response.setContentType("text/html");
	response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }

}
