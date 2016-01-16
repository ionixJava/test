import java.math.BigDecimal;
import java.sql.Connection;
import ionix.Data.*;

public final class Program {

    public static void main(String[] args){
        Connection conn = DB.createConnection();
        try(TransactionalDbAccess dataAccess = new TransactionalDbAccess(conn)) {
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
}
