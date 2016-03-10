package Oracle;

import Models.Categories;
import Models.Products;
import ionix.Data.*;
import ionix.Utils.Ser;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public final class OracleTest {
    static final int length = 1000;

    public final static Connection createConnection() {
        return ionixFactory.createConnection();
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

    public static void testUpdate() {
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
            cmd.setUpdatedFields(new HashSet<String>() {{
                add("CategoryName".toUpperCase());
                add("Description".toUpperCase());
            }});
            cmd.update(entity, provider);

            dataAccess.commit();
        }
    }

    public static void testInsert() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            Categories entity = new Categories().setCategoryName("Bu Insert").setDescription("Bu Insert Açıklama");

            EntityCommandInsert<Categories> cmd = new ionix.Data.Oracle.EntityCommandInsert<>(Categories.class, dataAccess);
            cmd.insert(entity, provider);

            System.out.println(entity.getCategoryID());

            dataAccess.commit();
        }
    }

    public static void testDelete() {
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

    public static void testBatchUpdate() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item) -> {
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


    public static void testBatchInsert() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final OracleSchemaMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            EntityCommandSelect<Categories> selectCmd = new EntityCommandSelect<>(Categories.class, dataAccess).setConvertType(true);
            List<Categories> entityList = selectCmd.select(provider, null);

            entityList.forEach((item) -> {
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

    public static void testBatchDelete() {
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

    public static void testCommandFactory() {
        Connection conn = createConnection();
        try (TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
            dataAccess.onExecuteSqlComplete((e) ->
                    System.out.println(SqlQueryHelper.toParameterlessQuery(e.getQuery()))
            );

            final EntityMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            Class<Categories> entityClass = Categories.class;
            CommandFactory f = new ionix.Data.Oracle.CommandFactory(dataAccess);
            Categories entity = f.createSelectCommand(entityClass).setConvertType(true).selectById(provider, 8);

            f.createEntityCommand(entityClass, EntityCommandType.Insert).execute(entity, provider);
            f.createEntityCommand(entityClass, EntityCommandType.Update).execute(entity, provider);
            f.createEntityCommand(entityClass, EntityCommandType.Delete).execute(entity, provider);


            List<Categories> entityList = f.createSelectCommand(entityClass).setConvertType(true).select(provider, null);
            f.createBatchCommand(entityClass, EntityCommandType.Insert).execute(entityList, provider);
            f.createBatchCommand(entityClass, EntityCommandType.Update).execute(entityList, provider);

            entityList = f.createSelectCommand(entityClass).setConvertType(true).query(provider, SqlQuery.toQuery("select * from Categories where CategoryID > ?", 8));
            int[] deleteCount = f.createBatchCommand(entityClass, EntityCommandType.Delete).execute(entityList, provider);

            dataAccess.commit();

            System.out.println("Silinen Kayıt: " + deleteCount.length);
        }
    }


    public static void sequenceTest() {
        String sql = "insert into Categories (categoryid,categoryname,description)" +
                "  VALUES (sqe_Categories.NEXTVAL,'with sequence', 'Açıklama')";

        SqlQuery q = SqlQuery.toQuery(sql);

        Connection conn = createConnection();
        try (DbAccess dataAccess = new DbAccess(conn)) {
            System.out.println(dataAccess.executeUpdateSequence(q, "CATEGORYID").getGeneratedKey());
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

            final CommandFactory factory = new ionix.Data.Oracle.CommandFactory(dataAccess);
            final EntityMetaDataProvider provider = new OracleSchemaMetaDataProvider();

            CommandAdapter cmd = new CommandAdapter(factory, provider);
            Repository<Categories> rep = new Repository<>(Categories.class, cmd);


            List<Categories> list = rep.select(null);

            Categories en = rep.selectSingle(null);

            list = rep.query("select * from Categories where rownum < 4");
            en = rep.querySingle("select * from Categories where CategoryID=?", 3);

            en.setCategoryName(en.getCategoryName() + 5);//.setPicture(new byte[16]);

            // rep.update(en, "PICTURE");
            // rep.update(en, "CATEGORYNAME", "PICTURE");
            //  rep.update(en);

            Categories newEn = new Categories().setCategoryName("bu yeni").setDescription("Bu da Yeni");//.setPicture(new byte[20]);

            //rep.insert(newEn, "CATEGORYNAME");
            rep.insert(newEn, "CATEGORYNAME", "PICTURE");
            //  rep.insert(newEn);

            //  rep.delete(newEn);


            list.forEach((item) ->
                    item.setDescription(item.getDescription() + 1)
            );

            //cmd.batchUpdate(Categories.class, list, "DESCRIPTION");
            //  cmd.batchUpdate(Categories.class, list, "DESCRIPTION", "PICTURE");
            //  cmd.batchUpdate(Categories.class, list);

            ArrayList<Categories> newList = new ArrayList<>();
            for (int j = 0; j < 3; ++j)
                newList.add(new Categories().setCategoryName(Integer.toString(j)).setDescription(Integer.toString(j)));//.setPicture(new byte[j]));

            rep.batchInsert(newList, "CATEGORYNAME");
            rep.batchInsert(newList, "CATEGORYNAME", "PICTURE");
            rep.batchInsert(newList);

            rep.batchDelete(newList);


            dataAccess.commit();
        }
    }


    public static void test(){
        try(TransactionalDbAccess dataAccess = ionixFactory.createTransactionalDbAccess())
        {
            Repository<Products> rep = ionixFactory.createRepository(Products.class, dataAccess);

            Products entity = rep.selectById(1);

            entity.setProductName("Chai değişti");

            rep.update(entity);

            List<Products> entityList = rep.query("select * from products where rownum <= 3");

            rep.batchInsert(entityList);


            dataAccess.commit();
        }
    }
}
