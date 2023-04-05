package com.example.Controller;
import DatabaseAccessObject.AppointmentsAccess;
import DatabaseAccessObject.ContactsAccess;
import DatabaseAccessObject.CustomersAccess;
import Model.Appointments;
import Model.Contacts;
import Model.Customers;
import helper.ErrorChecker;
import helper.JDBC;
import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;


/**
 * Controller for AddAppointment, Lambda #1 in Initialize
 */
public class AddAppointmentController {

    @FXML private Label appointmentIDLabel;
    @FXML private Label userIDLabel;
    @FXML private TextField appointmentTitleField;
    //make a combo box, should only select current customers, not create new ones
    //@FXML private TextField customerNameField;
    @FXML private ComboBox customerNameField;
    @FXML private TextField customerIDField;
    //@FXML private ComboBox customerIDField;
    @FXML private ComboBox contactField;
    @FXML private DatePicker startDatePicker;
    @FXML private ChoiceBox startHourChoice;
    @FXML private ChoiceBox startMinuteChoice;
    @FXML private ChoiceBox startAmPmChoice;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox endHourChoice;
    @FXML private ChoiceBox endMinuteChoice;
    @FXML private ChoiceBox endAmPmChoice;
    @FXML private TextField locationField;
    @FXML private TextField typeField;
    @FXML private TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    ObservableList<String> amPmList = FXCollections.observableArrayList("AM", "PM");
    ObservableList<String> minuteList = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> hourList = FXCollections.observableArrayList("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

    ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();

    HashMap<TextField, Boolean> currAppTextFields = new HashMap<TextField, Boolean>();
    HashMap<ComboBox, Boolean> currApptComboBoxes = new HashMap<ComboBox, Boolean>();

    @FXML
    private ChoiceBox amPmChoiceBox;
    @FXML
    private ChoiceBox minuteBox;

    private int contactNameID;

    /**
     *
     * @throws SQLException
     */
    public AddAppointmentController() throws SQLException {
    }

    /**
     * will initialize the Add Appointment form with needed information
     * Lambda Expression #1 here, replaces loop. Uses getContactName to add name to allContactNamesList observable array
     * @throws SQLException if it cannot run a query
     */
    @FXML
    void initialize() throws SQLException {
        //sets start hour times
        startHourChoice.setItems(hourList);
        startHourChoice.setValue("12");
        startMinuteChoice.setItems(minuteList);
        startMinuteChoice.setValue("00");
        startAmPmChoice.setItems(amPmList);
        startAmPmChoice.setValue("AM");
        //sets start hour times
        endHourChoice.setItems(hourList);
        endHourChoice.setValue("12");
        endMinuteChoice.setItems(minuteList);
        endMinuteChoice.setValue("00");
        endAmPmChoice.setItems(amPmList);
        endAmPmChoice.setValue("AM");

        int currUserID = LoginScreenController.getCurrUserID();
        userIDLabel.setText(String.valueOf(currUserID));

        String now = TimeLogicConverter.getLocalTime();

        //will set the date Fields to user's current day
        LocalDate localDate = LocalDate.parse(now.substring(0, 10));
        startDatePicker.setValue(localDate);
        TimeLogicConverter.setEndDate(startDatePicker, endDatePicker);

        //check to see date and time. If date is the same, will not allow user to select earlier hour
        ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();

        ObservableList<Contacts> allContactsList = ContactsAccess.getContacts();

        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        ObservableList<String> allContactNamesList = FXCollections.observableArrayList();

        int intCurrApptID = AppointmentsAccess.generateAppointmentID();
        String stringCurrApptID = String.valueOf(intCurrApptID);
        /*FilteredList<Customers> customerNamesList = new FilteredList<>(allCustomersList,
                i-> i.getCustomerName() ==);*/
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);

        /**
         * Lambda Expression #1
         * with Contacts Observables List, for each contactName, add to allContactsNameList
         */
        allContactsList.forEach(contacts -> allContactNamesList.add(contacts.getContactName()));

        appointmentIDLabel.setText(stringCurrApptID);
        customerNameField.setItems(allCustomerNamesList);
        contactField.setItems(allContactNamesList);

    }

    /**
     * Gets CustomerID from selected customer
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void customerSelectedGetCustomerID(ActionEvent event) throws IOException, SQLException {
        String customerName = customerNameField.getValue().toString();
        int customerID = CustomersAccess.getSelectedCustomerID(customerName);
        customerIDField.setText(String.valueOf(customerID));

    }

    /**
     * Gets the ContactID for selected appointment contact
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void contactSelectedGetContactsID(ActionEvent event) throws IOException, SQLException {
        String contactName = contactField.getValue().toString();
        int contactID = ContactsAccess.getSelectedContactID(contactName);
        //System.out.println(contactID);
        contactNameID = contactID;

    }

    /**
     * This will add a newly filled out appointment
     * Has error checkers for overlap of appointments an incorrectly filled requirements
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void addAppointmentSaveButton(ActionEvent event) throws IOException, SQLException {
        //get something to count total user IDs, then +1 to generate appointment ID

        String insertStatement = "INSERT INTO client_schedule.appointments (Appointment_ID, Title, Description, Location, " +
                "Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        JDBC.openConnection();
        JDBC.setPreparedStatement(JDBC.connection, insertStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();

        //BIG ALERTS FOR INCOMPLETE OR INCORRECT DATE+ TIME
        //Checks if Reuired fields are filled out
        TextField[] textFieldsToCheck = new TextField[] {appointmentTitleField, locationField, typeField, descriptionField};
        ComboBox[] comboBoxesToCheck = new ComboBox[] {customerNameField, contactField};
        DatePicker[] datePickersToCheck = new DatePicker[] {startDatePicker, endDatePicker};


        Boolean allRequiredFieldsPass = ErrorChecker.requiredFieldsChecked(textFieldsToCheck, comboBoxesToCheck, datePickersToCheck);

        if (!allRequiredFieldsPass) {
            return;
        }

        int currApptID = Integer.parseInt(appointmentIDLabel.getText());
        String currApptTitle = appointmentTitleField.getText();
        String currApptLocation = locationField.getText();
        String currApptType = typeField.getText();
        String currApptDescription = descriptionField.getText();
        int currCustomerID  = Integer.parseInt(customerIDField.getText());
        //need to get date from 1 box, Hour, Minute and AM/PM. Then combine
        //start date time
        LocalDate apptStartDate = startDatePicker.getValue();
        String apptStartHour = startHourChoice.getValue().toString();
        String apptStartMinute = startMinuteChoice.getValue().toString();
        boolean apptStartPm = startAmPmChoice.getValue().toString().equals("PM");
        //end date time
        LocalDate apptEndDate = endDatePicker.getValue();
        String apptEndHour = endHourChoice.getValue().toString();
        String apptEndMinute = endMinuteChoice.getValue().toString();
        boolean apptEndPm = endAmPmChoice.getValue().toString().equals("PM");
        //Make this a Lambda?
        //do we need this T? or do we replace with " "
        String startDateTimeString = TimeLogicConverter.convertTime(apptStartDate, apptStartHour, apptStartMinute, apptStartPm);
        String endDateTimeString = TimeLogicConverter.convertTime(apptEndDate, apptEndHour, apptEndMinute, apptEndPm);

        //Now convert dateTimeStrings to format to enter into database. Do we need UTC?
        String startDateTimeStringUTC = TimeLogicConverter.convertDateTimeToUTC(startDateTimeString);
        String endDateTimeStringUTC = TimeLogicConverter.convertDateTimeToUTC(endDateTimeString);


        //Created and Updated Date. will use nowUTC
        String timeNow = TimeLogicConverter.getLocalTime();
        String nowUTC = TimeLogicConverter.convertDateTimeToUTC(timeNow);



        //make sure appointment has same start day and end day (Business hours are EST)
        boolean onSameDay = ErrorChecker.startDayEndDayOnSameDay(startDateTimeStringUTC, endDateTimeStringUTC);
        if (!onSameDay){
            return;
        }

        //makes sure appointment cannot take place on a weekend
        boolean onWeekDay = ErrorChecker.appointmentOnWeekday(startDateTimeStringUTC, endDateTimeStringUTC);
        if (!onWeekDay) {
            return;
        }

        //checks if selected appointment times are between Office Hours
        boolean hourChecker = ErrorChecker.withinBusinessHoursEST(startDateTimeStringUTC, endDateTimeStringUTC);
        if (!hourChecker) {
            return;
        }

        //Will check to confirm start time is before end time
        boolean startBeforeEnd = ErrorChecker.startBeforeEndChecker(startDateTimeStringUTC, endDateTimeStringUTC);

        if (!startBeforeEnd) {
            return;
        }

        Boolean overlapTest = ErrorChecker.apptTimeChecker(allAppointmentsList, startDateTimeStringUTC, endDateTimeStringUTC, currApptID);

        if (!overlapTest) {
            return;
        }

        ps.setInt(1, currApptID);
        ps.setString(2, currApptTitle);
        ps.setString(3, currApptDescription);
        ps.setString(4, currApptLocation);
        ps.setString(5, currApptType);
        //ps.setTimestamp(6, Timestamp.valueOf(startLocalDateTimeToAdd));
        //ps.setTimestamp(6, Timestamp.valueOf(converted to UTC start date));
        //ps.setTimestamp(7, Timestamp.valueOf(conerted to UTS end date));
        ps.setString(6, startDateTimeStringUTC);
        ps.setString(7, endDateTimeStringUTC);
        //need to verify this is correct
        ps.setString(8, nowUTC);
        ps.setString(9, "admin");
        ps.setString(10, nowUTC);
        ps.setInt(11, 1);
        ps.setInt(12, currCustomerID);
        //ps.setInt(13, Integer.parseInt(contactAccess.findContactID(addAppointmentContact.getValue())));
        //ps.setInt(14, Integer.parseInt(contactAccess.findContactID(userIDLabel.getText())));
        ps.setInt(13, Integer.parseInt(userIDLabel.getText()));
        ps.setInt(14, contactNameID);

        ps.execute();

        JDBC.closeConnection();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * Cancels the appointment add feature
     * @param event
     * @throws IOException
     */
    @FXML public void addAppointmentCancelButton (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * when user selected date, end date is automatically selected as the same date
     * @param event
     */
    @FXML
    public void endDateSelected(ActionEvent event) {
        TimeLogicConverter.setEndDate(startDatePicker, endDatePicker);
    }


}
