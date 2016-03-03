import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import Oracle.OracleTest;
import ionix.Data.*;

public final class Program {



    public static void main(String[] args) throws Exception {

        //SqlServerTest.testSelectById();
      //  OracleTest.testSelect();
       // OracleTest.testSelectSingle();
       // OracleTest.testQuery();
       // OracleTest.testQuerySingle();
       // OracleTest.testSelectById();

        OracleTest.testUpdate();

//        Connection conn = OracleTest.createConnection();
//
//         PreparedStatement ps = conn.prepareStatement("SELECT SYSDATE FROM DUAL");
//         ResultSet rs = ps.executeQuery();
//
//        while (rs.next()){
//            System.out.println(rs.getObject(1));
//        }

//        SqlServerTest.testBatchUpdate();
//        SqlServerTest.testBatchInsert();
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

    public static void testBatchInsert(){
        Connection conn = SqlServerTest.createConnection();
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
        Connection conn = SqlServerTest.createConnection();
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
