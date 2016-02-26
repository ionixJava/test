import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

public final class Program {

    static final int length = 1000;

    public static void main(String[] args) {


        testUpdate();
       // testHashSet();
       // testFilterCriteriaTest();
        //System.out.println("\n");
        //testSelectById();
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

    public static void testQuerySingle() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            // String sql = "select CategoryID,CategoryName,Description from NORTHWND.dbo.Categories where CategoryID=?";
            String sql = "select * from NORTHWND.dbo.Categories where CategoryID=?";

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                Categories entity = cmd.querySingle(Categories.class, provider, SqlQuery.toQuery(sql, 7));
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            // System.out.println(Ser.toJson(entity));
        }
    }

    public static void testSelectSingle() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                Categories entity = cmd.selectSingle(Categories.class, provider, SqlQuery.toQuery(" where CategoryID=?", 7));
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entity));
        }
    }

    public static void testSelect() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                List<Categories> entityList = cmd.select(Categories.class, provider, null);
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entityList));
        }
    }

    public static void testQuery() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                // System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                List<Categories> entityList = cmd.query(Categories.class, provider, SqlQuery.toQuery("select * from NORTHWND.dbo.Categories"));
            }
            Date end = new Date();
            System.out.println((end.getTime() - start.getTime()));

            //System.out.println(Ser.toJson(entityList));
        }
    }

    public static void testSelectById() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                 System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Categories entity = cmd.selectById(Categories.class, provider, 8);

            System.out.println(Ser.toJson(entity));
        }
    }

    public static void testFilterCriteriaTest() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final Class entiyClass = Categories.class;
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            FilterCriteriaList filters = new FilterCriteriaList()
                    .add("CategoryID", ConditionOperator.In, "1", 2, 3, 4, 5)
                    .add("CategoryName", ConditionOperator.StartsWith, "Co");

            SqlQuery query = new SqlQueryProviderSelect(provider.createEntityMetaData(entiyClass)).toQuery()
                    .combine(filters.toQuery());
            EntityCommandSelect cmd = new EntityCommandSelect(dataAccess);//.setConvertType(true);
            List<Categories> entityList = cmd.query(entiyClass, provider, query);

            System.out.println();
            System.out.println(Ser.toJson(entityList));
        }
    }

    public static void testUpdate(){
        Connection conn = DB.createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            EntityCommandSelect selectCmd = new EntityCommandSelect(dataAccess);
            Categories entity = selectCmd.selectById(Categories.class, provider, dataAccess.executeScalar(SqlQuery.toQuery("select max(CategoryID) from Categories"), int.class));
            entity.setCategoryName("fcuk CategoryName").setDescription("fcuk Description");

            EntityCommandExecute cmd = new ionix.Data.SqlServer.EntityCommandUpdate(dataAccess, Categories.class);
            cmd.execute(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testHashSet(){
        ArrayList<String> al = new ArrayList<>();
        HashSet<String> h = new HashSet<>();
      //  Ha

        for(Integer j = 0; j < 1000000;++j){
            al.add(j.toString());
            h.add(j.toString());
        }

        Date start = new Date();
        for(int j = 0; j < 10000; ++j){
            al.contains("55555");
        }
        Date end = new Date();
        System.out.println((end.getTime() - start.getTime()));

        start = new Date();
        for(int j = 0; j < 10000; ++j){
            h.contains("55555");
        }
        end = new Date();
        System.out.println((end.getTime() - start.getTime()));
    }
}
