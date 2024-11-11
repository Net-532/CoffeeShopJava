package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    static {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/coffee_shop";
            String username = "root"; // Ваше ім'я користувача
            String password = "bibaiboba_27"; // Ваш пароль

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("З'єднання встановлене.");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не знайдений.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Помилка підключення до бази даних.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null || isConnectionClosed()) {
            try {
                System.out.println("Перепідключення...");

                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffee_shop", "root", "bibaiboba_27");
            } catch (SQLException e) {
                System.out.println("Не вдалося перепідключитись.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    private static boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}
