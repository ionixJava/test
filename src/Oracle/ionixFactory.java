package Oracle;


import ionix.Data.*;
import ionix.Data.DbAccess;
import ionix.Data.Oracle.CommandFactory;
import ionix.Data.SqlQueryHelper;
import ionix.Data.TransactionalDbAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ionixFactory {
    public static Connection createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@DB:1521:ORCL", "NORTHWND", "1");
            return conn;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static DbAccess creatDataAccess(Connection connection) {
        DbAccess dataAccess = new DbAccess(connection);
        dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
        );

        return dataAccess;
    }

    public static DbAccess creatDataAccess() {
        return creatDataAccess(createConnection());
    }

    public static TransactionalDbAccess createTransactionalDbAccess() {
        TransactionalDbAccess dataAccess = new TransactionalDbAccess(createConnection());
        dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
        );
        return dataAccess;
    }

    public static CommandFactory CreateFactory(DbAccess dataAccess) {
        return new CommandFactory(dataAccess);
    }


    public static CommandAdapter createCommand(DbAccess dataAccess) {
        return new CommandAdapter(CreateFactory(dataAccess), createEntityMetaDataProvider());
    }

    public static <TEntity> Repository<TEntity> createRepository(Class<TEntity> cls, DbAccess dataAccess){
        return new Repository<TEntity>(cls, createCommand(dataAccess));
    }

    public static EntityMetaDataProvider createEntityMetaDataProvider() {
        return new OracleSchemaMetaDataProvider();//Değiştir
    }
}
