package com.example.Controller;
import DatabaseAccessObject.*;
import Model.*;
import helper.JDBC;
import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for Modify Customer functionality
 */
public class ModifyCustomerController implements Initializable {

    @FXML
    private TextField customerName;
    @FXML
    private TextField phone;
    @FXML
    private TextField streetAddress;
    @FXML
    private TextField streetAddress2;
    @FXML
    private TextField city;
    @FXML
    private ComboBox stateProvince;
    @FXML
    private ComboBox country;
    @FXML
    private TextField postalCode;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label customerIDLabel;
    @FXML
    private Label userIDLabel;

    /**
     * sets customer info after user selected customer to modify
     * @param selectedCustomer
     * @throws SQLException
     * @throws IOException
     */
    public void getCustomerInfo(Customers selectedCustomer) throws SQLException, IOException {

        customerIDLabel.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerName.setText(selectedCustomer.getCustomerName());
        country.setValue(selectedCustomer.getCustomerCountry());
        postalCode.setText(selectedCustomer.getCustomerPostalCode());
        phone.setText(selectedCustomer.getCustomerPhone());
        stateProvince.setValue(selectedCustomer.getCustomerDivision());

        //now we need to split address by comma. If one, street address1 & city. If 2, street address1. street address2, city
        String[] addressInfo = selectedCustomer.getCustomerAddress().split(", ");
        int selectedAddressComma = addressInfo.length;

        if(selectedAddressComma == 1) {
            streetAddress.setText(addressInfo[0]);
        }

        if(selectedAddressComma == 2) {
            streetAddress.setText(addressInfo[0]);
            city.setText(addressInfo[1]);
        }

        if(selectedAddressComma == 3) {
            streetAddress.setText(addressInfo[0]);
            streetAddress2.setText(addressInfo[1]);
            city.setText(addressInfo[2]);
        }

        ObservableList<DivisionsCountries> allDivisionsInCountryList = DivisionsCountriesAccess.getDivisionFromCountryName(country.getValue().toString());
        ObservableList<String> divivsionsFromCountryName = FXCollections.observableArrayList();

        allDivisionsInCountryList.stream().map(DivisionsCountries::getDivision).forEach(divivsionsFromCountryName::add);
        stateProvince.setItems(divivsionsFromCountryName);


    }

    /**
     * sets provinces after country is populated/pickded
     * @param event
     * @throws SQLException
     */
    @FXML
    void setStateProvinces(ActionEvent event) throws SQLException {
        stateProvince.setDisable(false);
        ObservableList<DivisionsCountries> allDivisionsInCountryList = DivisionsCountriesAccess.getDivisionFromCountryName(country.getValue().toString());
        ObservableList<String> divivsionsFromCountryName = FXCollections.observableArrayList();

        allDivisionsInCountryList.stream().map(DivisionsCountries::getDivision).forEach(divivsionsFromCountryName::add);

        stateProvince.setValue("");
        stateProvince.setItems(divivsionsFromCountryName);
        //set state/province combo box with said divisions.

    }


    /**
     * created necessary data t start for customer modification
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int currUserID = LoginScreenController.getCurrUserID();
        userIDLabel.setText(String.valueOf(currUserID));

        //need to add state/provinces to the state/provence box
        //need to populate countries as well.
        //give same functionality, changing country, changes state/province

        ObservableList<Countries> allCountriesList = null;

        try {
            ObservableList<FirstLevelDivisions> allFirstLevelDivisionsList = FirstLevelDivisionsAccess.getFirstLevelDivisions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            allCountriesList = CountriesAccess.getCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //make countries names list to add to combo box
        ObservableList<String> allCountriesNamesList = FXCollections.observableArrayList();
        allCountriesList.stream().map(Countries::getCounty).forEach(allCountriesNamesList::add);
        country.setItems(allCountriesNamesList);


    }

    /**
     * begins process of saving changes to customer after modifcation
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void modifyCustomerSaveButton(ActionEvent event) throws IOException, SQLException {
        //get something to count total user IDs, then +1 to generate appointment ID

        String updateStatement = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        JDBC.openConnection();
        JDBC.setPreparedStatement(JDBC.connection, updateStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();

        String currCustName = customerName.getText();
        String currCustPostal = postalCode.getText();
        String currCustPhone = phone.getText();

        String nowTime = TimeLogicConverter.getLocalTime();
        String nowTimeUTC = TimeLogicConverter.convertDateTimeToUTC(nowTime);

        //address string
        String address1 = streetAddress.getText().trim();
        String address2 = streetAddress2.getText().trim();
        boolean address2Flag = (address2.length() > 0);
        String currCustCity = city.getText();
        String currCustAddress = address1 + ", " + currCustCity;
        if (address2Flag) {
            currCustAddress = address1 + ", " + address2 + ", " + currCustCity;
        }

        int currCustDivisionID = FirstLevelDivisionsAccess.getDivisionIDfromDivisionName(stateProvince.getValue().toString());
        int currCustID = Integer.parseInt(customerIDLabel.getText());

        ps.setString(1, currCustName);
        ps.setString(2, currCustAddress);
        ps.setString(3, currCustPostal);
        ps.setString(4, currCustPhone);
        ps.setString(5, nowTimeUTC);
        ps.setString(6, userIDLabel.getText());
        //need to verify this is correct
        ps.setInt(7, currCustDivisionID);
        ps.setInt(8, currCustID);

        //System.out.println("ps " + ps);
        ps.execute();


        JDBC.closeConnection();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * cancels the process of modifying appointment and returns user to dashboard
     * @param event
     * @throws IOException
     */
    @FXML public void modifyCustomerCancelButton (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


}
