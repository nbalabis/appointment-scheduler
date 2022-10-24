package helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database connection helper.
 *
 * @author Nicholas Balabis
 */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    public static Connection connection;

    /**
     * Opens the database connection.
     */
    public static void openConnection() {
        try {
            Class.forName(driver);
            String password = "Passw0rd!";
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
