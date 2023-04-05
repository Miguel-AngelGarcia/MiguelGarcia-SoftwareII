package DatabaseAccessObject;

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
 * Will allow user to access the database and retrieve information on Customers
 */
public class CustomersAccess {

    /**
     * Will query DB and return list of customers
     * @return
     * @throws SQLException
     */
    public static ObservableList<Customers> getCustomers() throws SQLException {

        ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();

        String query_string = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, " +
                "first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country \n" +
                "FROM client_schedule.customers \n" +
                "LEFT JOIN client_schedule.first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID \n" +
                "LEFT JOIN client_schedule.countries on first_level_divisions.Country_ID = countries.Country_ID";

        PreparedStatement ps = connection.prepareStatement(query_string);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int customerDivisionID = rs.getInt("Division_ID");
            String customerDivision = rs.getString("Division");
            int customerCountryID = rs.getInt("Country_ID");
            String customerCountry = rs.getString("Country");

            Customers customer = new Customers(customerID, customerName, customerAddress, customerPostalCode,
                    customerPhone, customerDivisionID, customerDivision, customerCountryID, customerCountry);

            customersObservableList.add(customer);

        } // end of while loop

        JDBC.closeConnection();
        return  customersObservableList;
    }   // END of  ObservableList<Customers> function

    /**
     * we get customer name, query DB and return CustomerID
     * @param customerName
     * @return
     * @throws SQLException
     */
    public static int getSelectedCustomerID (String customerName) throws SQLException{

        int customerID = 0;

        JDBC.openConnection();
        String query_string = "SELECT Customer_ID FROM client_schedule.customers WHERE Customer_Name = '" + customerName + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            customerID = rs.getInt("Customer_ID");
        }
        JDBC.closeConnection();
        return customerID;
    }

    /**
     * Willl take customerID, query DB, and return Customer name
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static String getSelectedCustomerName (int customerID) throws SQLException{

        String customerName = "";

        JDBC.openConnection();
        String query_string = "SELECT Customer_Name FROM client_schedule.customers WHERE Customer_ID = '" + customerID + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            customerName = rs.getString("Customer_Name");
        }
        JDBC.closeConnection();
        return customerName;
    }

    /**
     * Will generate our CustomerID for newlt created customer
     * @return
     * @throws SQLException
     */
    public static int generateCustomerID() throws SQLException {

        int customerID = 0;
        JDBC.openConnection();
        String query_string = "SELECT MAX(Customer_ID) FROM client_schedule.customers";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int maxCustomerID = rs.getInt("Max(Customer_ID)");
            customerID = maxCustomerID + 1;
        }

        JDBC.closeConnection();
        return customerID;
    }

    /**
     * will take customerID, and delete it IF it fits parameters
     * @param customerID
     * @throws SQLException
     */
    public static void deleteCustomer(int customerID) throws SQLException {
        //need to add a checker to confirm no appointments for the customer exist
        JDBC.openConnection();
        String query_string = "DELETE FROM client_schedule.customers WHERE Customer_ID = " + customerID;
        PreparedStatement ps = connection.prepareStatement(query_string);
        ps.executeUpdate();

        JDBC.closeConnection();
    }




}
