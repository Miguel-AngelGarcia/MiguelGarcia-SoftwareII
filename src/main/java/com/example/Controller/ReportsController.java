package com.example.Controller;

import DatabaseAccessObject.ContactsAccess;
import DatabaseAccessObject.ReportsAccess;
import Model.Contacts;
import Model.ReportByContact;
import Model.ReportsByCountry;
import Model.ReportsByMonthType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ReportsController {
    @FXML
    private TableView<ReportByContact> contactScheduleTable;
    @FXML
    private TableColumn<ReportByContact, Integer> apptIdColumn;
    @FXML
    private TableColumn<ReportByContact, String> titleColumn;
    @FXML
    private TableColumn<ReportByContact, String> contactTypeColumn;
    @FXML
    private TableColumn<ReportByContact, String> descriptionColumn;
    @FXML
    private TableColumn<ReportByContact, String> startColumn;
    @FXML
    private TableColumn<ReportByContact, String> endColumn;
    @FXML
    private TableColumn<ReportByContact, Integer> customerIdColumn;
    @FXML
    private ComboBox contactComboBox;

    @FXML
    private TableView<ReportsByMonthType> typeMonthTable;
    @FXML
    private TableColumn<ReportsByMonthType, String> monthColumn;
    @FXML
    private TableColumn<ReportsByMonthType, String> monthTypeColumn;
    @FXML
    private TableColumn<ReportsByMonthType, Integer> numApptColumn;

    @FXML
    private TableView<ReportsByCountry> customerCountryTable;
    @FXML
    private TableColumn<ReportsByCountry, String> countryColumn;
    @FXML
    private TableColumn<ReportsByCountry, Integer> countyCountyColumn;

    @FXML
    private Button exitButton;


    ObservableList<ReportByContact> contactsScheduleList = ReportsAccess.getReportsByContact();

    ObservableList<ReportByContact> contactsAppointmentsToDisplay = FXCollections.observableArrayList();

    /**
     * weill set the Contact - Appointments table for selected Contact
     * @param selectContactAppointments
     */
    void setContactScheduleTable(ObservableList<ReportByContact> selectContactAppointments) {
        apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        contactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));


        contactScheduleTable.setItems(selectContactAppointments);

    }

    /**
     * Takes given contact name, filters all Contact Appointments
     * Saves thi list as -> contactsAppointmentsToDisplay
     * @param givenContact
     */
    void getContactScheduleFromContactsScheduleList(String givenContact) {

        //need to clear last list / need a blank slate to fill out
        contactsAppointmentsToDisplay.clear();

        for(ReportByContact reportByContact: contactsScheduleList) {
            if (reportByContact.getContactName().equals(givenContact)) {

                //contactsScheduleList.stream().map(contactsScheduleList::add).forEach();
                contactsAppointmentsToDisplay.add(reportByContact);
            }
        }
    }

    /**
     *
     * @throws SQLException
     */
    public ReportsController() throws SQLException {
    }

    /**
     * initializes necessary information when user visits the reports section
     * @throws SQLException
     */
    @FXML
    void initialize() throws SQLException {
        try {
            ObservableList<Contacts> contactsObservableList = ContactsAccess.getContacts();

            //creates combo box contents
            ObservableList<String> contactsNames = FXCollections.observableArrayList();
            contactsObservableList.stream().map(Contacts::getContactName).forEach(contactsNames::add);
            contactComboBox.setItems(contactsNames);


            String defaultContactName = contactsScheduleList.get(0).getContactName();
            contactComboBox.setPromptText("Contact: " + defaultContactName);

            getContactScheduleFromContactsScheduleList(defaultContactName);
            setContactScheduleTable(contactsAppointmentsToDisplay);

            ObservableList<ReportsByMonthType> allReportsByMonthType = ReportsAccess.getReportsByMonthType();
            monthColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
            monthTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            numApptColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));


            typeMonthTable.setItems(allReportsByMonthType);

            ObservableList<ReportsByCountry> allReportsByCountry = ReportsAccess.getReportsByCountry();
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            countyCountyColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));

            customerCountryTable.setItems(allReportsByCountry);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * sets contact schedule after user selects Contact
     * @param event
     */
    @FXML
    void comboBoxContactClick(ActionEvent event) {
        String contactName = contactComboBox.getValue().toString();

        getContactScheduleFromContactsScheduleList(contactName);

        setContactScheduleTable(contactsAppointmentsToDisplay);

    }

    /**
     * exits reports page and directs user to dashboard
     * @param event
     * @throws IOException
     */
    @FXML
    public void reportsExitButton (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
