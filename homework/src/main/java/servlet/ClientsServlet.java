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


public class ClientsServlet extends HttpServlet {

     private final TemplateProcessor templateProcessor;
    private final DbServiceClientImpl dbServiceClient;


    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClientImpl dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        Object viewList = req.getSession().getAttribute("getlist");
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clientList  = new ArrayList<>();
        if(viewList != null && viewList.equals("getlist")){
           clientList  = dbServiceClient.findAll();
        }

        paramsMap.put("clientList", clientList);
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
        String address = request.getParameter("address");
        if (buttonCreateClient != null) {
            if (buttonCreateClient.equals("create") && !clientName.equals("") && !clientPhone.equals("")
                    && !address.equals("")) {
              dbServiceClient.saveClient(new Client(clientName, clientPhone, address));
              }
        }

        if (buttonGetList != null) {

            if (buttonGetList.equals("getlist")) {
                request.getSession().setAttribute("getlist", "getlist");
                  doGet(request, response);
            }
        }
        response.sendRedirect("http://localhost:8080" + request.getServletPath());
        return;
    }

}
