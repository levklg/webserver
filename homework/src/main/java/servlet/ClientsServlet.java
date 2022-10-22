package servlet;

import crm.model.Client;
import crm.model.Phone;
import crm.service.DbServiceClientImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TemplateProcessor;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;
    private final DbServiceClientImpl dbServiceClient;
    private boolean getList = false;

    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClientImpl dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        Map<String, Object> paramsMap = new HashMap<>();
        if (getList) {
            var clientList = dbServiceClient.findAll();
            paramsMap.put("clientList", clientList);
            getList = false;
        }
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage("clients.html", paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String clientName = request.getParameter("clientName");
        String clientPhone = request.getParameter("clientPhone");
        String buttonGetList = request.getParameter("getlist");
        String buttonCreateClient = request.getParameter("create");
        if (buttonCreateClient != null) {
            if (buttonCreateClient.equals("create") && !clientName.equals("") && !clientPhone.equals("")) {
                dbServiceClient.saveClient(new Client(clientName, clientPhone));
                doGet(request, response);
            }
        }

        if (buttonGetList != null) {

            if (buttonGetList.equals("getlist")) {
                getList = true;
                doGet(request, response);
            }
        }

    }

}
