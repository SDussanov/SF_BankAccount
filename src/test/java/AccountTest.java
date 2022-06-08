import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AccountTest {

    @BeforeAll
    public static void before() {
        System.out.println("Start testing CalculateUtil.java");
    }


    @Test
    public void getConnectionTest() throws SQLException, IOException, ClassNotFoundException {

        Account account = new Account(1);
        Connection connection = account.getConnection();
        System.out.println(connection.getMetaData());
    }


    @AfterAll
    public static void after() {
        System.out.println("Finish testing CalculateUtil.java");
    }
}
