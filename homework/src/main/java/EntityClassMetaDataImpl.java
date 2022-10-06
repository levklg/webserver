import crm.model.Id;
import mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {
   private Class<?> clazz;
   private  Object object;


    public EntityClassMetaDataImpl() {


    }

    @Override
    public String getName() {
     return clazz.getName();
    }

    @Override
    public Constructor getConstructor() {

        Constructor constructor = null;
        try {
            constructor  = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return  constructor;
    }

    @Override
    public Field getIdField() {

        Field[] fields =  clazz.getDeclaredFields();
        Field fieldId = null;

        for(Field field : fields) {
           if(field.isAnnotationPresent(Id.class)){

               fieldId = field;
            }
        }
        return fieldId;
    }

    @Override
    public List<Field> getAllFields() {

        Field[] fields=  clazz.getDeclaredFields();
        List<Field> fieldList =  Arrays.stream(fields).toList();
        return  fieldList;

    }

    @Override
    public List<Field> getFieldsWithoutId() {
        Field[] fields =  clazz.getDeclaredFields();
        List<Field> fieldListWithoutId = new ArrayList<>();

        for(Field field : fields) {
            if(!field.isAnnotationPresent(Id.class)){
               fieldListWithoutId.add(field);
            }
        }
        return fieldListWithoutId;
    }

   public Class<?> getClazz(){
        return this.clazz;
   }
   public Object getObject(){
        return this.object;
   }


   public void setClazz(Class<?> clazz){
        this.clazz = clazz;
   }

   public void setObject(Object object){
        this.object = object;
   }

}
