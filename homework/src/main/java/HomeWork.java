import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.repository.executor.DbExecutorImpl;
import core.sessionmanager.TransactionRunnerJdbc;
import crm.datasource.DriverManagerDataSource;
import crm.model.Client;
import crm.model.Manager;
import crm.service.DbServiceClientImpl;
import crm.service.DbServiceManagerImpl;
import dao.InMemoryUserDao;
import dao.UserDao;
import mapper.DataTemplateJdbc;
import mapper.EntityClassMetaData;
import mapper.EntitySQLMetaData;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.UsersWebServer;
import server.UsersWebServerSimple;
import services.TemplateProcessor;
import services.TemplateProcessorImpl;

import javax.sql.DataSource;


public class HomeWork {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";


    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB ";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws Exception {


        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new UsersWebServerSimple(WEB_SERVER_PORT, userDao,
                gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();




// Общая часть
     
         var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом



        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl();
        EntitySQLMetaData entitySQLMetaDataClient =  new EntitySQLMetaDataImpl(entityClassMetaDataClient);

        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient); //реализация DataTemplate, универсальная

// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);



// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData entityClassMetaDataManager = new EntityClassMetaDataImpl();
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);

        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager);
        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
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
