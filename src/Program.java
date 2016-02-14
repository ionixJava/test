import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

public final class Program {

    static final int length = 1000;
    public static void main(String[] args) {

        testQuery();

        testQuery();

        testSelect();

        testQuery();

      //  testQuerySingle();

      //  testQuerySingle();
       // testSelectSingle();
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



            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

           // String sql = "select CategoryID,CategoryName,Description from NORTHWND.dbo.Categories where CategoryID=?";
            String sql = "select * from NORTHWND.dbo.Categories where CategoryID=?";

            Date start = new Date();
            for(int j = 0; j < length; ++j){
            Categories entity = cmd.querySingle(Categories.class, provider, SqlQuery.toQuery(sql, 7));
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

           // System.out.println(Ser.toJson(entity));
        }
    }

    public static void testSelectSingle(){
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
               // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Date start = new Date();
            for(int j = 0; j < length; ++j){
                Categories entity = cmd.selectSingle(Categories.class, provider, SqlQuery.toQuery(" where CategoryID=?",7));
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entity));
        }
    }

    public static void testSelect(){
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Date start = new Date();
            for(int j = 0; j < length; ++j){
                List<Categories> entityList = cmd.select(Categories.class, provider, null);
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entityList));
        }
    }

    public static void testQuery(){
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

             Date start = new Date();
             for(int j = 0; j < length; ++j){
            List<Categories> entityList = cmd.query(Categories.class, provider, SqlQuery.toQuery("select * from NORTHWND.dbo.Categories"));
              }
             Date end = new Date();
             System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entityList));
        }
    }

}
