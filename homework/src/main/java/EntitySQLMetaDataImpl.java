import mapper.EntityClassMetaData;
import mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
   private EntityClassMetaDataImpl entityClassMetaData;


    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaDataClient) {
        entityClassMetaData = (EntityClassMetaDataImpl) entityClassMetaDataClient;

    }

    @Override
    public String getSelectAllSql() {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        Class<?> clazz = entityClassMetaData.getClazz();
        String nameClass =  clazz.getSimpleName();
        if(nameClass.equals("Client")){
            stringBuilder.append("client");
        }
        if(nameClass.equals("Manager")){
            stringBuilder.append("manager");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getSelectByIdSql() {

     Field field =  entityClassMetaData.getIdField();
     field.setAccessible(true);
        Long  value =  null;
        String nameParam = "";
        try {
            value  = (Long) field.get(entityClassMetaData.getObject());
            nameParam = field.getName();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
       StringBuilder stringBuilder = new StringBuilder();
       String nameTable = entityClassMetaData.getClazz().getSimpleName();
        stringBuilder = stringBuilder.append("SELECT * FROM ").append(nameTable.toLowerCase())
                .append(" WHERE ").append("id").append(" = ").append("?");

        return stringBuilder.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder stringBuilder = new StringBuilder();

        Object object = entityClassMetaData.getObject();

       List<Field> fieldList = entityClassMetaData.getAllFields();
       String schemaName = entityClassMetaData.getClazz().getSimpleName().toLowerCase();

        stringBuilder = stringBuilder.append( "insert into ").append(schemaName).append("(");


        StringBuilder columnNameStrBuld = new StringBuilder() ;
        StringBuilder valueStrBuld = new StringBuilder() ;

        for (Field field : fieldList){
            field.setAccessible(true);
            try {
                Object value =  field.get(object);
                Class<?> cl = field.getType();
                String nameClass =  cl.getName();

               if(value != null) {
                   columnNameStrBuld.append(field.getName());
                   columnNameStrBuld.append(", ");

                if(nameClass.equals( "java.lang.String")){
                    valueStrBuld.append("\'").append(value).append("\'");
                }else valueStrBuld.append(value);
                valueStrBuld.append(", ");
            }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        columnNameStrBuld.delete(columnNameStrBuld.length()-2, columnNameStrBuld.length());
        columnNameStrBuld.append(") values (");
        valueStrBuld.delete(valueStrBuld.length()-2, valueStrBuld.length());
        valueStrBuld.append(")");
        stringBuilder.append(columnNameStrBuld.toString()).append(valueStrBuld).append(" RETURNING id");
        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

    @Override
    public void setObject(Object object) {
        this.entityClassMetaData.setObject(object);
        this.entityClassMetaData.setClazz(object.getClass());
    }

    @Override
    public Object getObject() {
        return this.entityClassMetaData.getObject();
    }





}
