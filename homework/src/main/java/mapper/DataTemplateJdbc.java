package mapper;


import core.repository.DataTemplate;
import core.repository.executor.DbExecutor;
import crm.model.Client;
import crm.model.Manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id)  {


        String s = entitySQLMetaData.getSelectByIdSql();
        ResultSet result = null;

        Object object = entitySQLMetaData.getObject();
        Class<?> clazz = object.getClass();
        String className = clazz.getSimpleName();
        Client client = null;
        Manager manager = null;


            try (var statement = connection.prepareStatement(s)) {
                var savePoint = connection.setSavepoint("savePointName");
                statement.setLong(1,id);
                try{

                result = statement.executeQuery();
                if(className.equals("Client")){
                       while (result.next()) {
                                client = new Client();
                                var valueID = result.getLong("id");
                                var valueName = result.getString("name");
                                client.setId(valueID);
                                client.setName(valueName);
                            }
                         }

                    if(className.equals("Manager")){
                        while (result.next()) {
                            manager = new Manager();
                            var valueID = result.getLong("id");
                            var valueLabel = result.getString("label");
                            var valueParam1 = result.getString("param1");

                            manager.setNo(valueID);
                            manager.setLabel(valueLabel);
                            manager.setParam1(valueParam1);
                        }
                    }
            }catch (SQLException ex){
                    connection.rollback(savePoint);
                }

        }catch (SQLException ex){
                System.out.println(ex);
        }


      if(client != null) {
          Optional<T> optional = (Optional<T>) Optional.of(client);
          return  optional;
      }
        if(manager != null) {
            Optional<T> optional = (Optional<T>) Optional.of(manager);
            return  optional;
        }

      return  null;

    }

    @Override
    public List<T> findAll(Connection connection) {

        Object object = entitySQLMetaData.getObject();
        Class<?> clazz = object.getClass();
        String className = clazz.getSimpleName();
        List<T> list = new ArrayList<>();
        ResultSet resultSet = null;

        String s = entitySQLMetaData.getSelectAllSql();
        try(var statement = connection.prepareStatement(s)){
           
        resultSet = statement.executeQuery();

            if(className.equals("Client")){
                try {
                    while (resultSet.next()) {

                        Client client = new Client();
                        var valueID = resultSet.getLong("id");
                        var valueName = resultSet.getString("name");
                        client.setId(valueID);
                        client.setName(valueName);
                        list.add((T) client);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }
            if(className.equals("Manager")){
                while (true) {
                    try {
                        if (!resultSet.next()) break;
                        Manager manager = new Manager();
                        long valueID = 0;

                        valueID = resultSet.getLong("id");
                        var valueLabel = resultSet.getString("label");
                        var valueParam1 = resultSet.getString("param1");
                        manager.setNo(valueID);
                        manager.setLabel(valueLabel);
                        manager.setParam1(valueParam1);

                        list.add((T) manager);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }

  } catch (SQLException e) {
      e.printStackTrace();

  }


return  list;
    }

    @Override
    public long insert(Connection connection, T client) {

        ResultSet result = null;// =0;
        long clientID = 0;
        String s = entitySQLMetaData.getInsertSql();
        try (var statement = connection.prepareStatement(s)) {
            var savePoint = connection.setSavepoint("savePointName");

            try{

              result = statement.executeQuery();
                while (result.next()) {
                    var r = result.getLong("id") ;
                  clientID =r;

                }

            }catch (SQLException ex){
                connection.rollback(savePoint);
            }

        }catch (SQLException ex){
            System.out.println(ex);
        }

        return  clientID;
    }

    @Override
    public void update(Connection connection, T client) {
       entitySQLMetaData.getUpdateSql();
    }

    @Override
    public void setObject(Object object) {
        entitySQLMetaData.setObject(object);
    }


}
