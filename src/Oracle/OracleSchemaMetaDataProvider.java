package Oracle;


import ionix.Data.*;

import java.lang.reflect.Field;

/**
 * Created by mehme on 3.03.2016.
 */
public class OracleSchemaMetaDataProvider extends DbSchemaMetaDataProvider {

    @Override
    protected void setExtendedSchema(SchemaInfo schemaInfo, Field field) {
        super.setExtendedSchema(schemaInfo, field);
        schemaInfo.setColumnName(schemaInfo.getColumnName().toUpperCase());
    }

    @Override
    protected EntityMetaData generateEntityMetaData(Class entityClass){
        String tableName = ExtAnnotation.getTableName(entityClass).split("\\.")[2];
        return new EntityMetaData(entityClass, tableName);
    }

}
