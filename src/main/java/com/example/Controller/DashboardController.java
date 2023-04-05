package com.example.Controller;

import DatabaseAccessObject.CustomersAccess;
import Model.Appointments;
import Model.Customers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

import DatabaseAccessObject.AppointmentsAccess;

/**
 * Controller to the dashboard screen.
 */
public class DashboardController {
    //Appointments Table
    @FXML
    private TableView<Appointments> dashboardAppointmentTable;
    @FXML
    private TableColumn<Appointments, Integer> apptIdcolumn;
    @FXML
    private TableColumn<Appointments, String> apptTitleColumn;
    @FXML
    private TableColumn<Appointments, String> apptDescColumn;
    @FXML
    private TableColumn<Appointments, String> appLocationColumn;
    @FXML
    private TableColumn<Appointments, String> apptContactColumn;
    @FXML
    private TableColumn<Appointments, String> apptTypeColumn;
    @FXML
    private TableColumn<Appointments, String> apptStartColumn;
    @FXML
    private TableColumn<Appointments, String> appEndColumn;
    @FXML
    private TableColumn<Appointments, Integer> apptCustId;
    @FXML
    private TableColumn<Appointments, Integer> apptUserID;

    //Custemrs Table
    @FXML
    private TableView<Customers> dashboardCustomerTable;
    @FXML
    private TableColumn<Customers, Integer> customerIdcolumn;
    @FXML
    private TableColumn<Customers, String> customerNameColumn;
    @FXML
    private TableColumn<Customers, String> customerAddress;
    @FXML
    private TableColumn<Customers, String> customerPostalCode;
    @FXML
    private TableColumn<Customers, String> customerPhone;
    @FXML
    private TableColumn<Customers, Integer> customerStateProvince;
    @FXML
    private TableColumn<Customers, String> customerCountry;


    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab appointmentTab;
    @FXML
    private Tab customerTab;
    @FXML
    private Button signOutButton;
    @FXML
    private MenuItem all;
    @FXML
    private MenuItem month;
    @FXML
    private MenuItem week;
    @FXML
    private Button reports;
    @FXML
    private SplitMenuButton viewByButton;

    private Stage stage;
    private int currentDivisionID;

    //@FXML
    //private TableView<Customers> dashboardCustomerTable;


    String query_string = "SELECT * FROM client_schedule.appointments";

    /**
     * Will query the database and populate Appointments Table and Customers Table
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        try {
            //add contents to Appointment Table
            ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();
            setAppointmentsTable(allAppointmentsList);

            //Adds contents to Customer Table
            ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();

            customerIdcolumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            //will impliment code to get State/Province and Country from divisionID
            customerStateProvince.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
            customerCountry.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));

            dashboardCustomerTable.setItems(allCustomersList);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to populate the Appointments Table
     * Reused by each 'View By'
     * @param selectedIntervalOfAppointments
     */
    void setAppointmentsTable(ObservableList<Appointments> selectedIntervalOfAppointments) {
        apptIdcolumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        apptDescColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        apptContactColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentContactID"));
        appEndColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentEnd")));
        apptCustId.setCellValueFactory((new PropertyValueFactory<>("appointmentCustomerID")));
        apptUserID.setCellValueFactory((new PropertyValueFactory<>("appointmentUserID")));

        //need to convert UTC time to user Machine's local time


        apptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        apptStartColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentStart")));

        dashboardAppointmentTable.setItems(selectedIntervalOfAppointments);


    }

    /**
     * takes user to add appointment process
     * @param event
     * @throws Exception
     */
    @FXML
    void addAppointmentButton(ActionEvent event) throws Exception {
        Parent addAppointments = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene scene = new Scene(addAppointments);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * allows user to modify appointment
     * @param event
     * @throws Exception
     */
    @FXML
    void modifyAppointmentButton(ActionEvent event) throws Exception {
        try {

            int row = dashboardAppointmentTable.getSelectionModel().getSelectedIndex();
            Appointments customers = dashboardAppointmentTable.getItems().get(row);
            //System.out.println(customers);


            Appointments selectedAppointment = dashboardAppointmentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyAppointment.fxml"));
            loader.load();

            //get controller
            ModifyAppointmentController modifyAppointmentController = loader.getController();
            modifyAppointmentController.getAppointmentInfo(selectedAppointment);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select an Appointment first");
            alert.show();
        } catch (IndexOutOfBoundsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select an Appointment first");
            alert.show();
        }
    }

    /**
     * allows user to sign out
     * @param event
     * @throws Exception
     */
    @FXML
    void signOutButtonClick(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * allows user to delete appointment
     * @param event
     * @throws SQLException
     */
    @FXML
    void deleteAppointmentButton(ActionEvent event) throws SQLException {
        try {
            int selectedAppointmentID = dashboardAppointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
            String selectedAppointmentType = dashboardAppointmentTable.getSelectionModel().getSelectedItem().getAppointmentType();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to delete selected appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            String result_string = String.valueOf(selectedAppointmentID);

            if (result_string == "null" && result.get() == ButtonType.OK ) {
                Alert noPartSearchBar = new Alert(Alert.AlertType.ERROR);
                noPartSearchBar.setTitle("Error Message");
                noPartSearchBar.setContentText("No Appointment Selected");
                noPartSearchBar.showAndWait();
            }

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirm.setTitle("Confirmation");
                alertConfirm.setContentText("Appointment ID: " + selectedAppointmentID + ", of Type: " + selectedAppointmentType + ", has been deleted");
                alertConfirm.showAndWait();

                AppointmentsAccess.deleteAppointment(selectedAppointmentID);
                ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();
                //allAppointmentsList = TimeLogicConverter.convertObservableListUTCtoLocal(allAppointmentsList);
                //noticed bug where once deleting an appointment, all times of other appointments go to UTC?????
                dashboardAppointmentTable.setItems(allAppointmentsList);
            }

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No Appointment selected");
            alert.show();
        } catch (IndexOutOfBoundsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No Appointment selected");
            alert.show();
        }

    }

    /**
     * allows user to begin process to add customer
     * @param event
     * @throws Exception
     */
    @FXML
    void addCustomerButton(ActionEvent event) throws Exception {
        Parent addCustomers = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene scene = new Scene(addCustomers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * begins proess to modify customer
     * @param event
     * @throws Exception
     */
    @FXML
    void modifyCustomerButton(ActionEvent event) throws Exception {
        try {
            Customers selectedCustomer = dashboardCustomerTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyCustomer.fxml"));
            loader.load();

            //get controller

            ModifyCustomerController modifyCustomerController = loader.getController();
            //below is what causes the null ointer
            modifyCustomerController.getCustomerInfo(selectedCustomer);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();



        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select a customer first");
            alert.show();
        }
    }

    /**
     * allows user to begin process of deleting customer
     * @param event
     * @throws SQLException
     */
    @FXML
    void deleteCustomerButton(ActionEvent event) throws SQLException {
        try{
            int selectedCustomerID = dashboardCustomerTable.getSelectionModel().getSelectedItem().getCustomerID();
            String selectedCustomerName = dashboardCustomerTable.getSelectionModel().getSelectedItem().getCustomerName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to delete selected customer?");
            Optional<ButtonType> result = alert.showAndWait();

            //NEED a checker to see if customer has any appointments together

            String result_string = String.valueOf(selectedCustomerID);
            //checks if a customer is selected
            if (result_string == "null" && result.get() == ButtonType.OK ) {
                Alert noPartSearchBar = new Alert(Alert.AlertType.ERROR);
                noPartSearchBar.setTitle("Error Message");
                noPartSearchBar.setContentText("No Customer Selected");
                noPartSearchBar.showAndWait();
            }

            if (result.isPresent() && result.get() == ButtonType.OK) {

                Boolean customerCanBeDeleted = AppointmentsAccess.canDeleteCustomer(selectedCustomerID);

                if (customerCanBeDeleted) {
                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirm.setTitle("Confirmation");
                    alertConfirm.setContentText(selectedCustomerName + ", Customer ID: " + selectedCustomerID + " has been deleted");
                    alertConfirm.showAndWait();

                    CustomersAccess.deleteCustomer(selectedCustomerID);
                    ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();
                    dashboardCustomerTable.setItems(allCustomersList);

                } else {
                    Alert cantBeDeleted = new Alert(Alert.AlertType.ERROR);
                    cantBeDeleted.setTitle("Error Message");
                    cantBeDeleted.setHeaderText("Cannot delete Customer");
                    cantBeDeleted.setContentText("Must delete appointments belonging to Customer ID: " + selectedCustomerID +
                            " before deleting Customer");
                    cantBeDeleted.showAndWait();
                }

            }

        } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No customer selected");
                alert.show();
        }
    }

    /**
     * allows user to view all appointments
     * @param event
     * @throws SQLException
     */
    @FXML
    void viewByAll(ActionEvent event) throws SQLException {
        try {
            ObservableList<Appointments> allAppointments = AppointmentsAccess.getAppointments();
            setAppointmentsTable(allAppointments);
            viewByButton.setText("View By: All");

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select interval from 'View By:' first");
            alert.show();
        }
    }

    /**
     * allows suer to view appointments by Month
     * @param event
     * @throws SQLException
     */
    @FXML
    void viewByMonth(ActionEvent event) throws SQLException {
        try {
            ObservableList<Appointments> monthlyAppointments = AppointmentsAccess.getAppointmentsByMonth();
            setAppointmentsTable(monthlyAppointments);
            viewByButton.setText("View By: Month");

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select interval from 'View By:' first");
            alert.show();
        }
    }

    /**
     * allows user to view appointments by week
     * @param event
     * @throws SQLException
     */
    @FXML
    void viewByWeek(ActionEvent event) throws SQLException {
        try {
            ObservableList<Appointments> weeklyAppointments = AppointmentsAccess.getAppointmentsByWeek();
            setAppointmentsTable(weeklyAppointments);
            viewByButton.setText("View By: Week");

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select interval from 'View By:' first");
            alert.show();
        }
    }

    /**
     * will take user to reports section
     * @param event
     * @throws Exception
     */
    @FXML
    void reportsButton(ActionEvent event) throws Exception {
        Parent reports = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene scene = new Scene(reports);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
