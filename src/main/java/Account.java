import java.sql.*;

public class Account {

    private int id;
    private int amount;
    private int status;
    private static final String URL = "jdbc:postgresql://localhost/bank?user=postgres&password=1234";

    public Account(int id) {
        this.id = id;
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

            int rows = preparedStatement.executeUpdate();
            System.out.println(String.format("Сумма обновлена с %s на %s", currentAmount, (currentAmount + amount)));

            connection.close();
            preparedStatement.close();
            this.amount = (currentAmount + amount);

        } catch (SQLException e) {
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

                int rows = preparedStatement.executeUpdate();
                System.out.println(String.format("Сумма обновлена с %s на %s", currentAmount, (currentAmount - amount)));

                connection.close();
                preparedStatement.close();
                this.amount = (currentAmount - amount);
                this.status = 1;
            } else {
                System.out.println("У вас недостаточно средств!");
                this.status = 0;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.status;
    }
}

