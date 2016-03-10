package Oracle;


import ionix.Data.*;

import java.lang.reflect.Field;

public class OracleSchemaMetaDataProvider extends DbSchemaMetaDataProvider {

    @Override
    protected void setExtendedSchema(SchemaInfo schemaInfo, Field field) {
        super.setExtendedSchema(schemaInfo, field);
        schemaInfo.setColumnName(schemaInfo.getColumnName().toUpperCase());
        if (schemaInfo.getDatabaseGeneratedOption() == StoreGeneratedPattern.Identity)
            schemaInfo.setDatabaseGeneratedOption(StoreGeneratedPattern.AutoGenerateSequence);
    }

    @Override
    protected EntityMetaData generateEntityMetaData(Class entityClass) {
        String tableName = null;

        String[] arr = ExtAnnotation.getTableName(entityClass).split("\\.");
        if (arr.length == 3)
            tableName = ExtAnnotation.getTableName(entityClass).split("\\.")[2];
        else
            tableName = ExtAnnotation.getTableName(entityClass);
        return new EntityMetaData(entityClass, tableName);

    }
}
