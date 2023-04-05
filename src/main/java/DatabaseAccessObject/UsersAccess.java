package DatabaseAccessObject;

import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Users
 */
public class UsersAccess {
    /**
     * Will get List of Users
     * @return
     * @throws SQLException
     */
    public static ObservableList<Users> getUsers() throws SQLException {

        ObservableList<Users> usersObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.users";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String userPassword = rs.getString("Password");

            Users user = new Users(userID, userName, userPassword);

            usersObservableList.add(user);

        }// end of loop

        JDBC.closeConnection();
        return usersObservableList;

    }// end of ObservableList<Users> getUsers()


    /**
     * Confirms if email and Password are correct or incorrect
     * @param username
     * @param password
     * @return
     */
    public static int credentialsValidation(String username, String password) {
        try {
            int userID = 0;
            JDBC.openConnection();
            String query_string = "SELECT * FROM client_schedule.users WHERE User_Name = '" + username + "' AND Password = '" + password + "' LIMIT 1";
            //System.out.println(query_string);
            PreparedStatement ps = connection.prepareStatement(query_string);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                //added to make password test case sensitive
                if (rs.getString("Password").equals(password))
                    userID = rs.getInt("User_ID");
                    //System.out.println(userID);
            }
            JDBC.closeConnection();
            return userID;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
