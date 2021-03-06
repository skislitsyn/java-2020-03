package ru.otus.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
	this.dbServiceUser = dbServiceUser;
	this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	User user = dbServiceUser.getUser(extractIdFromRequest(request)).orElse(null);

	response.setContentType("application/json;charset=UTF-8");
	ServletOutputStream out = response.getOutputStream();
	out.print(gson.toJson(user));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String bodyData = getBodyData(request);
	User user = gson.fromJson(bodyData, User.class);

	dbServiceUser.saveUser(user);

	response.setContentType("application/json;charset=UTF-8");
	ServletOutputStream out = response.getOutputStream();
	out.print(gson.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
	String[] path = request.getPathInfo().split("/");
	String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
	return Long.parseLong(id);
    }

    private String getBodyData(HttpServletRequest request) throws IOException {
	StringBuilder buffer = new StringBuilder();
	try (BufferedReader reader = request.getReader()) {
	    String line;
	    while ((line = reader.readLine()) != null) {
		buffer.append(line);
	    }
	    return buffer.toString();
	}
    }
}
