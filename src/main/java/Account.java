import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Account {

    private final int id;
    private int amount;
    private int status;
    private static final String URL = "jdbc:postgresql://localhost/bank?user=postgres&password=1234";

    public Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("src/main/java/database.properties");
        props.load(in);
        in.close();

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    public Account(int id) {
        this.id = id;
    }

    public int checkAccount() {
        try (Connection connection = this.getConnection()) {

            String sql = "SELECT * FROM ACCOUNTS WHERE ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.last()) {
                System.out.println("Найденых строк " + resultSet.getRow());
                return resultSet.getRow();
            }

            connection.close();
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
        return 0;
    }

    public int getStatus() {
        return this.status;
    }

    public int getBalance() {
        try (Connection connection = this.getConnection()) {

            Statement statement = connection.createStatement();
            String sql = "SELECT AMOUNT FROM ACCOUNTS WHERE ID = " + this.id;

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                amount = resultSet.getInt("AMOUNT");
            }
            connection.close();
            resultSet.close();
            statement.close();

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
        return amount;
    }

    public void putMoney(int amount) {
        try (Connection connection = this.getConnection()) {
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
            psTransaction.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ;

            psTransaction.executeUpdate();
            System.out.println("Добавил данные от транзакции в базу данных");

            connection.close();
            psTransaction.close();
            preparedStatement.close();
            this.amount = (currentAmount + amount);

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
    }


    public void takeMoney(int amount) {
        try (Connection connection = this.getConnection()) {
            int currentAmount = this.getBalance();

            if (currentAmount - amount >= 0) {
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
                psTransaction.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                ;

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

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
    }

    public void transferMoney(int receiveId, int receiveAmount, int amount) {
        try (Connection connection = this.getConnection()) {
            int currentAmount = this.getBalance();

            if (currentAmount - amount >= 0) {
                String sql1 = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                preparedStatement1.setInt(1, (currentAmount - amount));
                preparedStatement1.setInt(2, this.id);

                preparedStatement1.executeUpdate();
                System.out.println(String.format("ИД %s Сумма обновлена с %s на %s", this.id, currentAmount, (currentAmount - amount)));

                String sql2 = "UPDATE ACCOUNTS SET AMOUNT = ? WHERE ID = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, (receiveAmount + amount));
                preparedStatement2.setInt(2, receiveId);

                preparedStatement2.executeUpdate();
                System.out.println(String.format("ИД %s Сумма обновлена с %s на %s", receiveId, receiveAmount, (receiveAmount + amount)));

                String sqlTransaction = "INSERT INTO TRANSFERS (SEND_ID, RECEIVE_ID, AMOUNT, DATE) VALUES (?, ?, ?, ?)";
                PreparedStatement psTransaction = connection.prepareStatement(sqlTransaction);
                psTransaction.setInt(1, this.id);
                psTransaction.setInt(2, receiveId);
                psTransaction.setInt(3, amount);
                psTransaction.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                ;

                psTransaction.executeUpdate();
                System.out.println("Добавил данные о транзакции в базу данных");

                connection.close();
                psTransaction.close();
                preparedStatement1.close();
                preparedStatement2.close();
                this.amount = (currentAmount - amount);
                this.status = 1;
            } else {
                System.out.println("У вас недостаточно средств!");
                this.status = 0;
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }
    }


    public List<String> getOperationListAll() {
        List<String> data = new ArrayList<>();
        try (Connection connection = this.getConnection()) {

            String sql = "SELECT * FROM OPERATIONS WHERE ID_ACCOUNT = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, this.id);
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
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getOperationListStartDate(Date startDate) {

        List<String> data = new ArrayList<>();


        try (Connection connection = this.getConnection()) {

            String sql = "SELECT * FROM OPERATIONS WHERE ID_ACCOUNT = ? AND DATE >= ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, this.id);
            preparedStatement.setDate(2, startDate);
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
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getOperationListEndDate(Date endDate) {

        List<String> data = new ArrayList<>();


        try (Connection connection = this.getConnection()) {

            String sql = "SELECT * FROM OPERATIONS WHERE ID_ACCOUNT = ? AND DATE <= ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, this.id);
            preparedStatement.setDate(2, endDate);
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
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getOperationList(Date startDate, Date endDate) {

        List<String> data = new ArrayList<>();

        try (Connection connection = this.getConnection()) {

            String sql = "SELECT * FROM OPERATIONS WHERE ID_ACCOUNT = ? AND DATE BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, this.id);
            preparedStatement.setDate(2, startDate);
            preparedStatement.setDate(3, endDate);
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
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка с подключением к БД!");
            e.printStackTrace();
        }

        return null;
    }
}

