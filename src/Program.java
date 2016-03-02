import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

public final class Program {

    static final int length = 1000;

    public static void main(String[] args) {
        testDelete();
    }

    public static void testQuerySingle() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {


            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            // String sql = "select CategoryID,CategoryName,Description from NORTHWND.dbo.Categories where CategoryID=?";
            String sql = "select * from NORTHWND.dbo.Categories where CategoryID=?";

            Categories temp = cmd.querySingle(provider, SqlQuery.toQuery(sql, 7));

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                Categories entity = cmd.querySingle(provider, SqlQuery.toQuery(sql, 7));
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


            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Categories temp = cmd.selectSingle(provider, SqlQuery.toQuery(" where CategoryID=?", 7));

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                Categories entity = cmd.selectSingle(provider, SqlQuery.toQuery(" where CategoryID=?", 7));
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


            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            List<Categories> temp = cmd.select(provider, null);

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                List<Categories> entityList = cmd.select(provider, null);
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
                 System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            List<Categories> el = cmd.query(provider, SqlQuery.toQuery("select * from NORTHWND.dbo.Categories"));

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                List<Categories> entityList = cmd.query(provider, SqlQuery.toQuery("select * from NORTHWND.dbo.Categories"));
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

            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            Categories entity = cmd.selectById(provider, 8);

            System.out.println(Ser.toJson(entity));
        }
    }

    public static void testFilterCriteriaTest() {
        Connection conn = DB.createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });


            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            FilterCriteriaList filters = new FilterCriteriaList()
                    .add("CategoryID", ConditionOperator.In, "1", 2, 3, 4, 5)
                    .add("CategoryName", ConditionOperator.StartsWith, "Co");

            SqlQuery query = new SqlQueryProviderSelect(provider.createEntityMetaData(Categories.class)).toQuery()
                    .combine(filters.toQuery());
            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess);//.setConvertType(true);
            List<Categories> entityList = cmd.query(provider, query);

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

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess);
            Categories entity = selectCmd.selectById(provider, dataAccess.executeScalar(SqlQuery.toQuery("select max(CategoryID) from Categories"), int.class));
            entity.setCategoryName("fcuk hamdi").setDescription("fcuk murat");

            EntityCommandExecute cmd = new ionix.Data.SqlServer.EntityCommandUpdate<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testInsert(){
        Connection conn = DB.createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();


            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandExecute<Categories> cmd = new ionix.Data.SqlServer.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            System.out.println(entity.getCategoryID());

            dataAccess.rollBack();
        }
    }

    public static void testDelete(){
        Connection conn = DB.createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();


            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandExecute<Categories> cmd = new ionix.Data.SqlServer.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            System.out.println(entity.getCategoryID());

            cmd = new ionix.Data.SqlServer.EntityCommandDelete<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            dataAccess.commit();
        }
    }

//    public static void testBatchUpdate(){
//        Connection conn = DB.createConnection();
//        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
//            dataAccess.onExecuteSqlComplete((e) -> {
//                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
//            });
//
//            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();
//
//            EntityCommandSelect selectCmd = new EntityCommandSelect(dataAccess)
//            Categories entity = selectCmd.select(Categories.class, provider, null);
//            entity.setCategoryName("fcuk hamdi").setDescription("fcuk murat");
//
//            EntityCommandExecute cmd = new ionix.Data.SqlServer.EntityCommandUpdate<>(dataAccess, Categories.class);
//            cmd.execute(entity, provider);
//
//            dataAccess.commit();
//        }
//    }


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

    public static void testBatchInsert(){
        Connection conn = DB.createConnection();
        String template = "insert into Categories (CategoryName, Description) values(?,?);";

        try {
            PreparedStatement ps = conn.prepareStatement(template);
            for(int j = 0; j < 15; ++j){
                ps.setObject(1, "c" + j);
                ps.setObject(2, "c" + j);

                ps.addBatch();
            }

          //  ps.executeb

            System.out.println( ps.executeBatch());

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            System.out.println(dataAccess.executeUpdate(q, null).getValue());


            dataAccess.commit();
        }
    }
}
