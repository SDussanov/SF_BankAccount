import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.http.DateParser.parseDate;

public class Account {

    private int id;
    private int amount;
    private int status;
    private static final String URL = "jdbc:postgresql://localhost/bank?user=postgres&password=1234";

    public Account(int id) {
        this.id = id;
    }

    public int getStatus() {
        return this.status;
    }

    public int getBalance() {
        try (Connection connection = DriverManager.getConnection(URL)) {

            Statement statement = connection.createStatement();
            String sql = "SELECT AMOUNT FROM ACCOUNTS WHERE ID = " + this.id;

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                amount = resultSet.getInt("AMOUNT");
            }
            connection.close();
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
        return amount;
    }

    public void putMoney(int amount) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            int currentAmount = this.getBalance();
            String sql = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, (currentAmount + amount));
            preparedStatement.setInt(2, this.id);

            preparedStatement.executeUpdate();

            System.out.println(String.format("Сумма обновлена с %s на %s", currentAmount, (currentAmount + amount)));

            String sqlTransaction = "INSERT INTO OPERATIONS (ID_ACCOUNT, TYPE, AMOUNT, DATE) VALUES (?, 1, ?, ?)";
            PreparedStatement psTransaction = connection.prepareStatement(sqlTransaction);
            psTransaction.setInt(1, this.id);
            psTransaction.setInt(2, amount);
            psTransaction.setDate(3, new java.sql.Date(System.currentTimeMillis()));;

            psTransaction.executeUpdate();
            System.out.println("Добавил данные от транзакции в базу данных");

            connection.close();
            psTransaction.close();
            preparedStatement.close();
            this.amount = (currentAmount + amount);

        } catch (SQLException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
    }


    public int takeMoney(int amount) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            int currentAmount = this.getBalance();

            if (currentAmount - amount >= 0){
                String sql = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, (currentAmount - amount));
                preparedStatement.setInt(2, this.id);

                preparedStatement.executeUpdate();
                System.out.println(String.format("Сумма обновлена с %s на %s", currentAmount, (currentAmount - amount)));

                String sqlTransaction = "INSERT INTO OPERATIONS (ID_ACCOUNT, TYPE, AMOUNT, DATE) VALUES (?, 2, ?, ?)";
                PreparedStatement psTransaction = connection.prepareStatement(sqlTransaction);
                psTransaction.setInt(1, this.id);
                psTransaction.setInt(2, amount);
                psTransaction.setDate(3, new java.sql.Date(System.currentTimeMillis()));;

                psTransaction.executeUpdate();
                System.out.println("Добавил данные о транзакции в базу данных");

                connection.close();
                psTransaction.close();
                preparedStatement.close();
                this.amount = (currentAmount - amount);
                this.status = 1;
            } else {
                System.out.println("У вас недостаточно средств!");
                this.status = 0;
            }


        } catch (SQLException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
        return this.status;
    }

    public List<String> getOperationList(Date firstDate){

        List<String> data = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {

            String sql = "SELECT * FROM OPERATIONS WHERE ID_ACCOUNT = ? AND DATE = ?";

//Date startDate = parseDate(firstDate);


            PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, this.id);
                preparedStatement.setDate(2, firstDate);
                ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add("User: id=" + resultSet.getInt("id_account") +
                        " type=" + resultSet.getString("type") +
                        " amount=" + resultSet.getString("amount") +
                        " date=" + resultSet.getString("date"));

                System.out.println("User: id=" + resultSet.getInt("id_account") +
                        " type=" + resultSet.getString("type") +
                        " amount=" + resultSet.getString("amount") +
                        " date=" + resultSet.getString("date"));
            }

            connection.close();
            resultSet.close();
            preparedStatement.close();
            return data;
        } catch (SQLException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }

        return null;
    }
}

