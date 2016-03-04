package Oracle;

import Models.Categories;
import ionix.Data.*;
import ionix.Utils.Ser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public final class OracleTest {
    static final int length = 1000;

    public final static Connection createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String ok = "do not show dupicate for this";
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@DB:1521:ORCL", "NORTHWND", "1");
            return conn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void testQuerySingle() {
        Connection conn = createConnection();
        try (DbAccess dataAccess = new TransactionalDbAccess(conn)) {

            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            String sql = "select * from Categories where CategoryID=?";

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
                 System.out.println("Complete: " + e.getQuery().toString() + " - " + e.isSucceeded());
            });

            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

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

            String ok = "do not show dupicate for this";
            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

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


            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            List<Categories> el = cmd.query(provider, SqlQuery.toQuery("select * from Categories"));

            Date start = new Date();
            for (int j = 0; j < length; ++j) {
                List<Categories> entityList = cmd.query(provider, SqlQuery.toQuery("select * from Categories"));
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

            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

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


            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            FilterCriteriaList filters = new FilterCriteriaList()
                    .add("CategoryID", ConditionOperator.In, "1", 2, 3, 4, 5)
                    .add("CategoryName", ConditionOperator.StartsWith, "Co");

            SqlQuery query = new SqlQueryProviderSelect(provider.createEntityMetaData(Categories.class)).toQuery()
                    .combine(filters.toQuery());
            EntityCommandSelect<Categories> cmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = cmd.query(provider, query);

            System.out.println();
            System.out.println(Ser.toJson(entityList));
        }
    }

    public static void testUpdate(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) -> {
               // System.out.println(e.getQuery().toString() + " - " + e.isSucceeded());
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()));
            });

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            Categories entity = selectCmd.selectById(provider, dataAccess.executeScalar(int.class, SqlQuery.toQuery("select max(CategoryID) from Categories")));
            entity.setCategoryName("Seafood").setDescription("Seaweed and fish");

            EntityCommandUpdate<Categories> cmd = new EntityCommandUpdate<>(Categories.class, dataAccess);
            cmd.setUpdatedFields(new HashSet<String>() {{ add("CategoryName".toUpperCase()); add("Description".toUpperCase()); }});
            cmd.update(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testInsert(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()) )
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandInsert<Categories> cmd = new ionix.Data.Oracle.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.insert(entity, provider);

            System.out.println(entity.getCategoryID());

            dataAccess.commit();
        }
    }

    public static void testDelete(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();


            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandExecute<Categories> cmd = new ionix.Data.Oracle.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);
            dataAccess.commit();

            System.out.println(entity.getCategoryID());

            cmd = new ionix.Data.EntityCommandDelete<>(Categories.class, dataAccess);
            cmd.execute(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testBatchUpdate(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item)-> {
                 //item.setCategoryName(item.getCategoryName() + 1);
                 //item.setDescription(item.getDescription() + 2);

                item.setCategoryName(item.getCategoryName().replace('1', ' ').trim());
                item.setDescription(item.getDescription().replace('2', ' ').trim());
            });

            BatchCommandExecute<Categories> cmd = new ionix.Data.BatchCommandUpdate<>(Categories.class, dataAccess);
            int result = cmd.execute(entityList, provider).length;

            System.out.println(result);

            dataAccess.commit();
        }
    }


    public static void testBatchInsert(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item)-> {
                item.setCategoryName(item.getCategoryName() + 1);
                item.setDescription(item.getDescription() + 2);

                //item.setCategoryName(item.getCategoryName().replace('1', ' ').trim());
                // item.setDescription(item.getDescription().replace('2', ' ').trim());
            });

            BatchCommandInsert<Categories> cmd = new ionix.Data.Oracle.BatchCommandInsert<>(Categories.class, dataAccess);
           // cmd.setInsertFields(new HashSet<String>() {{ add("CategoryName"); add("Description"); }});

            int result = cmd.insert(entityList, provider).length;

            System.out.println(result);

            dataAccess.commit();
        }
    }

    public static void testBatchDelete(){
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = selectCmd.select(provider, null);

            BatchCommandInsert<Categories> cmd = new ionix.Data.Oracle.BatchCommandInsert<>(Categories.class, dataAccess);

            cmd.insert(entityList, provider);

            dataAccess.commit();

            List<Categories> deleteList = selectCmd.query(provider, SqlQuery.toQuery("select * from Categories where CategoryID > 8"));

            BatchCommandDelete<Categories> batchDeleteCmd = new BatchCommandDelete<>(Categories.class, dataAccess);
            int count = batchDeleteCmd.delete(deleteList, provider).length;
            dataAccess.commit();

            System.out.println("Silinen Kayıt Sayısı: " + count);
        }
    }




    public static void sequenceTest(){
        String sql = "insert into Categories (categoryid,categoryname,description)"+
                "  VALUES (sqe_Categories.NEXTVAL,'with sequence', 'Açıklama')";

        SqlQuery q = SqlQuery.toQuery(sql);

        Connection conn = createConnection();
        try (DbAccess dataAccess = new DbAccess(conn)) {
            System.out.println(dataAccess.executeUpdateSequence(q,"CATEGORYID").getGeneratedKey());
        }
    }

}
