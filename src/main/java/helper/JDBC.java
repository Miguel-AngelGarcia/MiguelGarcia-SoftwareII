package helper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Will allow us to connect to the Database
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    //private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    //get rid of below and add top back after daylight savings bug ends?
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?serverTimezone=UTC"; // LOCAL

    private static PreparedStatement preparedStatement;
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface


    /**
     * method opens connections to the DB
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            //System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * method closes connection to DB
     */
    public static void closeConnection() {
        try {
            connection.close();
            //System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * method prepares the SQL statement
     * @param con
     * @param sqlStatement
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection con, String sqlStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     * returns preparedStatement
     * @return
     */
    public static PreparedStatement getPreparedStatement() {

        return preparedStatement;
    }

}
