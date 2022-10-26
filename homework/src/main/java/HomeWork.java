import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.repository.DataTemplateHibernate;
import core.repository.HibernateUtils;

import core.sessionmanager.TransactionManagerHibernate;
import crm.dbmigrations.MigrationsExecutorFlyway;
import crm.model.Address;
import crm.model.Client;
import crm.model.Phone;
import crm.service.DbServiceClientImpl;
import helpers.FileSystemHelper;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ClientsWebServer;
import server.ClientsWebServerWithBasicSecurity;
import services.TemplateProcessor;
import services.TemplateProcessorImpl;

import java.util.ArrayList;


public class HomeWork {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws Exception {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        dbServiceClient.saveClient(new Client("Igir", "9109100000","ул.Московская"));
        dbServiceClient.saveClient(new Client("Sergey", "9108764567", "ул.Ленина"));
        dbServiceClient.saveClient(new Client("Danil", "9032342313", "ул.Кирова"));


        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);


        ClientsWebServer usersWebServer = new ClientsWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, gson, templateProcessor, dbServiceClient);

        usersWebServer.start();
        usersWebServer.join();

    }


}
