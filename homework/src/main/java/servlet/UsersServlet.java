package servlet;

import dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TemplateProcessor;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
   // private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";
    private static final String TEMPLATE_СLIENT_LIST = "clientList";
    private static final String TEMPLATE_MANAGER_LIST = "managerList";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
       // userDao.findRandomUser().ifPresent(randomUser -> paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, randomUser));
        paramsMap.put(TEMPLATE_СLIENT_LIST,userDao.getListClient());
        paramsMap.put(TEMPLATE_MANAGER_LIST,userDao.getListMahager());
        response.setContentType("text/html");

        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
