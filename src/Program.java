import java.math.BigDecimal;
import java.sql.Connection;

import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

public final class Program {

    public static void main(String[] args) {
        testSelectSingle();
    }

    public static void testTransaction() {
        Connection conn = DB.createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            SqlQuery q = SqlQuery.toQuery("UPDATE SICIL SET ADI=@Name WHERE SICILK=@Id")
                    .paramater("Name", "Cihat")
                    .paramater("Id", 9372);

            System.out.println(dataAccess.executeUpdate(q));


            dataAccess.commit();
        }
    }

    public static void testQuerySingle(){
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {



            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess).setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Categories entity = cmd.querySingle(Categories.class, provider, SqlQuery.toQuery("select * from NORTHWND.dbo.Categories where CategoryID=7"));

            System.out.println(Ser.toJson(entity));

        }
    }

    public static void testSelectSingle(){
        Connection conn = DB.createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Categories entity = cmd.selectSingle(Categories.class, provider, SqlQuery.toQuery(" where CategoryID=?",7));

            System.out.println(Ser.toJson(entity));

            dataAccess.commit();
        }
    }
}
