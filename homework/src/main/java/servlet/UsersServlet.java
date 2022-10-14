package servlet;

import crm.model.Client;
import crm.service.DbServiceClientImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TemplateProcessor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String TEMPLATE_СLIENT_LIST = "clientList";
    private List<Client> clientList = new ArrayList<>();
    private final TemplateProcessor templateProcessor;
    private   Map<String, Object> paramsMap = new HashMap<>();
private final DbServiceClientImpl dbServiceClient;
    public UsersServlet(TemplateProcessor templateProcessor,  DbServiceClientImpl dbServiceClient) {
        this.templateProcessor = templateProcessor;

        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

      Map<String, Object> paramsMap = new HashMap<>();

       paramsMap.put(TEMPLATE_СLIENT_LIST,clientList);
       response.setContentType("text/html");
       response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String clientName = request.getParameter("clientName");
        String clientPhone = request.getParameter("clientPhone");
        String buttonGetList = request.getParameter("getlist");
        String buttonCreateClient = request.getParameter("create");
        if(buttonCreateClient != null) {
            if (buttonCreateClient.equals("create") && !clientName.equals("") && !clientPhone.equals("")) {
                dbServiceClient.saveClient(new Client(clientName, clientPhone));
                clientList.clear();
                clientList = dbServiceClient.findAll();
            }
        }

        if(buttonGetList != null) {

            if (buttonGetList.equals("getlist")) {
                clientList.clear();
               clientList = dbServiceClient.findAll();
            }
        }
       doGet(request,response);
    }

}
