package DatabaseAccessObject;

import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Appointments
 */
public class AppointmentsAccess {

    /**
     * Willl query DB and return list of appointments
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointments() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            //rs.getTimeStamp should get it from the DB in UTC and then convert.
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentsObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsObservableList;

    } // END of  ObservableList<Appointments> function

    /**
     * Will be used to generate appt ID for new appointments
     * @return
     * @throws SQLException
     */
    public static int generateAppointmentID() throws SQLException {

        int appointmentID = 0;
        JDBC.openConnection();
        String query_string = "SELECT MAX(Appointment_ID) FROM client_schedule.appointments";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int maxAppointmentID = rs.getInt("Max(Appointment_ID)");
            appointmentID = maxAppointmentID + 1;
        }

        JDBC.closeConnection();
        return appointmentID;
    }

    /**
     * Will delete appointment
     * @param appointmentID
     * @throws SQLException
     */
    //should we add the delete appointment action here?
    public static void deleteAppointment(int appointmentID) throws SQLException {

        JDBC.openConnection();
        String query_string = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + appointmentID;
        PreparedStatement ps = connection.prepareStatement(query_string);
        ps.executeUpdate();

        JDBC.closeConnection();
    }

    /**
     * not used
     * @param givenCustomerID
     * @return
     * @throws SQLException
     */
    //delete this. just pass the pbject when you select the appointment
    public static ObservableList<Appointments> getAppointmentToModify(int givenCustomerID) throws SQLException {
        ObservableList<Appointments> appointmentInfoObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = " + givenCustomerID;

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentInfoObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentInfoObservableList;

    } // END of  ObservableList<appointmentInfoObservableList> function

    /**
     * will query DB and get appointment for the month (of current year)
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointmentsByMonth() throws SQLException {
        ObservableList<Appointments> appointmentsMonthlyObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments\n" +
                "WHERE MONTH(start) = (MONTH(curdate()) + 0) AND Year(Start) = YEAR(curdate());";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            //rs.getTimeStamp should get it from the DB in UTC and then convert.
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentsMonthlyObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsMonthlyObservableList;

    } // END of  ObservableList<Appointments> getAppointmentsByMonth function

    /**
     * Will query DB and reutn appointments for the week
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointmentsByWeek() throws SQLException {
        ObservableList<Appointments> appointmentsWeeklyObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments\n" +
                //"WHERE Start BETWEEN date_add(curdate(), interval - WEEKDAY(curdate()) DAY)\n" +
                //"AND now()";
                "WHERE YEARWEEK(START, 1) = YEARWEEK(CURDATE(), 1)";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            //rs.getTimeStamp should get it from the DB in UTC and then convert.
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentsWeeklyObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsWeeklyObservableList;

    } // END of  ObservableList<Appointments> getAppointmentsByMonth function

    /**
     * will get list of appointments that will occur within the next 15 minutes
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointmentsWith15Min() throws SQLException {

        ObservableList<Appointments> appointmentsWithin15observableList = FXCollections.observableArrayList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime nowIntermediate = LocalDateTime.now();
        String nowIntermediateString = nowIntermediate.format(formatter);

        String nowInUTC = TimeLogicConverter.convertDateTimeToUTC(nowIntermediateString);

        LocalDateTime nowInUTCLDT = TimeLogicConverter.convertStringToDateTime(nowInUTC);
        LocalDateTime nowPlus15Min = nowInUTCLDT.plusMinutes(15);

        String startTestTime = nowInUTCLDT.toString();
        String endTestTime = nowPlus15Min.toString();

        JDBC.openConnection();

        String query_string = "SELECT * FROM client_schedule.appointments \n" +
                "WHERE START BETWEEN '" + startTestTime + "' AND '" + endTestTime + "'";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            //rs.getTimeStamp should get it from the DB in UTC and then convert.
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentsWithin15observableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsWithin15observableList;
    }

    /**
     * will check if a customer can be deleted
     * @param customerIDTocheck
     * @return
     * @throws SQLException
     */
    public static Boolean canDeleteCustomer(int customerIDTocheck) throws SQLException {
        int customerAppointmentCount = 0;
        Boolean canDelete;

        JDBC.openConnection();
        String query_string = "SELECT COUNT(Appointment_ID) FROM client_schedule.appointments WHERE Customer_ID = " + customerIDTocheck;

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            customerAppointmentCount = rs.getInt("COUNT(Appointment_ID)");
        }

        if (customerAppointmentCount > 0) {
            canDelete = false;
        } else {
            canDelete = true;
        }

        return canDelete;
    }
}
