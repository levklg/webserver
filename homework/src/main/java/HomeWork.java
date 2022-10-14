import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.repository.executor.DbExecutorImpl;
import core.sessionmanager.TransactionRunnerJdbc;
import crm.datasource.DriverManagerDataSource;
import crm.model.Client;
import crm.service.DbServiceClientImpl;
import helpers.FileSystemHelper;
import mapper.DataTemplateJdbc;
import mapper.EntityClassMetaData;
import mapper.EntitySQLMetaData;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.UsersWebServer;
import server.UsersWebServerWithBasicSecurity;
import services.TemplateProcessor;
import services.TemplateProcessorImpl;

import javax.sql.DataSource;


public class HomeWork {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB ";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws Exception {



         var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl();
        EntitySQLMetaData entitySQLMetaDataClient =  new EntitySQLMetaDataImpl(entityClassMetaDataClient);

        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient); //реализация DataTemplate, универсальная


        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("Igir", "9109100000"));
        dbServiceClient.saveClient(new Client("Sergey", "9108764567"));
        dbServiceClient.saveClient(new Client("Danil", "9032342313"));




        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);


        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, gson, templateProcessor, dbServiceClient);

        usersWebServer.start();
        usersWebServer.join();

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
