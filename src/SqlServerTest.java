import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public final class SqlServerTest {
    static final int length = 1000;

    public final static Connection createConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=NORTHWND", "sa","1");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.204.128:1433;databaseName=NORTHWND", "sa", "1");
            return conn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void testQuerySingle() {
        Connection conn = createConnection();
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
        Connection conn = createConnection();
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
        Connection conn = createConnection();
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
        Connection conn = createConnection();
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
        Connection conn = createConnection();
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
        Connection conn = createConnection();
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

    public static void testUpdate() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess);
            Categories entity = selectCmd.selectById(provider, dataAccess.executeScalar(int.class, SqlQuery.toQuery("select max(CategoryID) from Categories")));
            entity.setCategoryName("new Category").setDescription("new Description");

            EntityCommandUpdate<Categories> cmd = new EntityCommandUpdate<>(Categories.class, dataAccess);
            cmd.setUpdatedFields(new HashSet<String>() {{
                add("CategoryName");
                add("Description");
            }});
            cmd.update(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testInsert() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();


            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandInsert<Categories> cmd = new ionix.Data.SqlServer.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.setInsertFields(new HashSet<String>() {{
                add("CategoryName");
                add("Description");
            }});
            cmd.insert(entity, provider);

            System.out.println(entity.getCategoryID());

            dataAccess.commit();
        }
    }

    public static void testDelete() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();


            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandExecute<Categories> cmd = new ionix.Data.SqlServer.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            System.out.println(entity.getCategoryID());

            cmd = new EntityCommandDelete<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testBatchUpdate() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item) -> {
                // item.setCategoryName(item.getCategoryName() + 1);
                // item.setDescription(item.getDescription() + 2);

                item.setCategoryName(item.getCategoryName().replace('1', ' ').trim());
                item.setDescription(item.getDescription().replace('2', ' ').trim());
            });

            BatchCommandExecute<Categories> cmd = new BatchCommandUpdate<>(Categories.class, dataAccess);
            int result = cmd.execute(entityList, provider).length;

            System.out.println(result);

            dataAccess.commit();
        }
    }


    public static void testBatchInsert() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item) -> {
                item.setCategoryName(item.getCategoryName() + 1);
                item.setDescription(item.getDescription() + 2);

                //item.setCategoryName(item.getCategoryName().replace('1', ' ').trim());
                // item.setDescription(item.getDescription().replace('2', ' ').trim());
            });

            BatchCommandInsert<Categories> cmd = new ionix.Data.SqlServer.BatchCommandInsert<>(Categories.class, dataAccess);
            cmd.setInsertFields(new HashSet<String>() {{
                add("CategoryName");
                add("Description");
            }});

            int result = cmd.insert(entityList, provider).length;

            System.out.println(result);

            dataAccess.commit();
        }
    }

    public static void testBatchDelete() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess);
            List<Categories> entityList = selectCmd.select(provider, null);

            BatchCommandInsert<Categories> cmd = new ionix.Data.SqlServer.BatchCommandInsert<>(Categories.class, dataAccess);
            cmd.setInsertFields(new HashSet<String>() {{
                add("CategoryName");
                add("Description");
            }});

            cmd.insert(entityList, provider);

            dataAccess.commit();

            List<Categories> deleteList = selectCmd.query(provider, SqlQuery.toQuery("select * from Categories where CategoryID > 8"));

            BatchCommandDelete<Categories> batchDeleteCmd = new BatchCommandDelete<>(Categories.class, dataAccess);
            int count = batchDeleteCmd.delete(deleteList, provider).length;
            dataAccess.commit();

            System.out.println("Silinen Kayıt Sayısı: " + count);
        }
    }

    public static void testCommandAdapter() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
                String pureSql = SqlQueryHelper.toParameterlessQuery(e.getQuery());
                if (!pureSql.toLowerCase().contains("select"))
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()));
              //  System.out.println(e.getQuery().toString());
            });

            final CommandFactory factory = new ionix.Data.SqlServer.CommandFactory(dataAccess);
            final EntityMetaDataProvider provider = new DbSchemaMetaDataProvider();

            CommandAdapter cmd = new CommandAdapter(factory, provider);
            List<Categories> list = cmd.select(Categories.class, null);

            Categories en = cmd.selectSingle(Categories.class, null);

            list = cmd.query(Categories.class, "select top 3 * from Categories;");
            en = cmd.querySingle(Categories.class, "select * from Categories where CategoryID=?", 3);

         //   en.setCategoryName(en.getCategoryName() + 5);

         //   cmd.update(Categories.class, en, "CategoryName", "Picture");
         //   cmd.update(Categories.class, en);

          //  Categories newEn = new Categories().setCategoryName("bu yeni").setDescription("Bu da Yeni").setPicture(new byte[20]);

          //  cmd.insert(Categories.class, newEn, "CategoryName");
         //   cmd.insert(Categories.class, newEn, "CategoryName", "Picture");
          //  cmd.insert(Categories.class, newEn);

         //   cmd.delete(Categories.class, newEn);

          //  list.forEach((item)->
           //     item.setDescription(item.getDescription() + 1)
          //  );

           // cmd.batchUpdate(Categories.class, list, "Description");
           // cmd.batchUpdate(Categories.class, list, "Description", "Picture");
            //cmd.batchUpdate(Categories.class, list);

            ArrayList<Categories> newList = new ArrayList<>();
            for(int j = 0; j < 3; ++j)
                newList.add(new Categories().setCategoryName(Integer.toString(j)).setDescription(Integer.toString(j)));//.setPicture(new byte[j]));

            cmd.batchInsert(Categories.class, newList, "CategoryName");
            cmd.batchInsert(Categories.class, newList, "CategoryName", "Picture");
            cmd.batchInsert(Categories.class, newList);

            cmd.batchDelete(Categories.class, newList);


            dataAccess.commit();
        }
    }
}
