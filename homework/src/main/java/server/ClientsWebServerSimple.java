package server;

import com.google.gson.Gson;
import crm.service.DbServiceClientImpl;

import helpers.FileSystemHelper;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.TemplateProcessor;
import servlet.ClientsServlet;


public class ClientsWebServerSimple implements ClientsWebServer {

    protected final TemplateProcessor templateProcessor;
    private final Gson gson;
    private final Server server;
    private final DbServiceClientImpl dbServiceClient;

    public ClientsWebServerSimple(int port, Gson gson, TemplateProcessor templateProcessor, DbServiceClientImpl dbServiceClient) {

        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
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
        handlers.addHandler(applySecurity(servletContextHandler, "/clients"));//, "/api/user/*","/login"


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath("static"));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");
        return servletContextHandler;
    }
}
