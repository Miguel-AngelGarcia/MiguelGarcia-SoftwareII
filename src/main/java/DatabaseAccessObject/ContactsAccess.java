package DatabaseAccessObject;

import Model.Contacts;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Contacts
 */
public class ContactsAccess {

    /**
     * Will query DB and return list of contacts
     * @return
     * @throws SQLException
     */
    public static ObservableList<Contacts> getContacts() throws SQLException {

        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();

        String query_string = "SELECT * FROM client_schedule.contacts";

        PreparedStatement ps = connection.prepareStatement(query_string);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int contactID = rs.getInt("Contact_ID");
            String contactsName = rs.getString("Contact_Name");
            String contactsEmail = rs.getString("Email");

            Contacts contact = new Contacts(contactID, contactsName, contactsEmail);
            contactsObservableList.add(contact);

        } // end of while loop

        JDBC.closeConnection();
        return  contactsObservableList;
    }   // END of  ObservableList<Contacts> function

    /**
     * Will take contactName and return its ContactID
     * @param contactName
     * @return
     * @throws SQLException
     */
    //should we add the delete appointment action here?
    public static int getSelectedContactID (String contactName) throws SQLException{
        int contactID = 0;

        JDBC.openConnection();
        String query_string = "SELECT Contact_ID FROM client_schedule.contacts WHERE Contact_Name = '" + contactName + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            contactID = rs.getInt("Contact_ID");
        }

        return contactID;
    }

    /**
     * will takeID and return contact Name
     * @param contactID
     * @return
     * @throws SQLException
     */
    public static String getSelectedContactName(int contactID) throws SQLException{
        String contactName = "";

        JDBC.openConnection();
        String query_string = "SELECT Contact_Name FROM client_schedule.contacts WHERE Contact_ID = '" + contactID + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            contactName = rs.getString("Contact_Name");
        }

        return contactName;
    }

}