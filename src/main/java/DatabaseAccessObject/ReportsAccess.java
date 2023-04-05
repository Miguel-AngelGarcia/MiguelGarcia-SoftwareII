package DatabaseAccessObject;

import Model.ReportByContact;
import Model.ReportsByCountry;
import Model.ReportsByMonthType;
import helper.JDBC;
import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Reports
 */
public class ReportsAccess {

    /**
     * Will query DB and get reports for Contact Appointment schedules
     * @return
     * @throws SQLException
     */
    public static ObservableList<ReportByContact> getReportsByContact() throws SQLException {
        ObservableList<ReportByContact> allAppointmentsByContactList = FXCollections.observableArrayList();

        JDBC.openConnection();
        String query_string = "SELECT \n" +
                "contacts.Contact_ID, contacts.Contact_Name, appointments.Appointment_ID, appointments.Customer_ID, " +
                "appointments.Title, appointments.Type, appointments.Description, appointments.Start, appointments.End\n" +
                "FROM client_schedule.contacts\n" +
                "INNER JOIN client_schedule.appointments on contacts.Contact_ID = appointments.Contact_ID";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            int appointmentID = rs.getInt("Appointment_ID");
            int appointmentCustomerID = rs.getInt("Customer_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentType = rs.getString("Type");
            String appointmentDescription = rs.getString("Description");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();

            ReportByContact reportByContact = new ReportByContact(contactID, contactName, appointmentID, appointmentCustomerID,
                    appointmentTitle, appointmentType, appointmentDescription, appointmentStart, appointmentEnd);

            allAppointmentsByContactList.add(reportByContact);

        }//end of while loop

        JDBC.closeConnection();
        return allAppointmentsByContactList;

    }// end of ObservableList<ReportByContact> getReportsByContact

    /**
     * Will query DB and report list of Appointments by Type and Month and their count
     * @return
     * @throws SQLException
     */
    public static ObservableList<ReportsByMonthType> getReportsByMonthType() throws SQLException {
        ObservableList<ReportsByMonthType> allReportsByMonthType = FXCollections.observableArrayList();

        JDBC.openConnection();
        String query_string = "SELECT DISTINCT Type, COUNT(Appointment_ID), Month(Start) from client_schedule.appointments\n" +
                " WHERE Year(Start) = Year(curdate())\n" +
                " GROUP BY Type, Month(Start), Year(Start)\n" +
                " ORDER BY Month(Start) ASC";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String appointmentType = rs.getString("Type");
            int count = rs.getInt("COUNT(Appointment_ID)");

            int monthInt = rs.getInt("Month(Start)");
            String month = TimeLogicConverter.convertMonthIntToString(monthInt);

            ReportsByMonthType reportsByMonthType = new ReportsByMonthType(month, appointmentType, count);
            allReportsByMonthType.add(reportsByMonthType);
        }//end of while loop

        JDBC.closeConnection();
        return allReportsByMonthType;
    }// end of ObservableList<ReportsByMonthType> allReportsByMonthType

    /**
     * Will query QB and return Customer Count by Country
     * @return
     * @throws SQLException
     */
    public static ObservableList<ReportsByCountry> getReportsByCountry() throws SQLException {
        ObservableList<ReportsByCountry> allReportsByCountry = FXCollections.observableArrayList();

        JDBC.openConnection();
        String query_string = "SELECT COUNT(Customer_ID), countries.Country FROM client_schedule.customers\n" +
                "LEFT JOIN client_schedule.first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID\n" +
                "LEFT JOIN client_schedule.countries on first_level_divisions.Country_ID = countries.Country_ID\n" +
                "GROUP BY countries.Country";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String country = rs.getString("Country");
            int countyCount = rs.getInt("COUNT(Customer_ID)");

            ReportsByCountry reportsByCountry = new ReportsByCountry(country, countyCount);

            allReportsByCountry.add(reportsByCountry);
        }//end of while loop

        JDBC.closeConnection();
        return allReportsByCountry;
    }// end of ObservableList<ReportsByCountry> getReportsBYCountry
}
