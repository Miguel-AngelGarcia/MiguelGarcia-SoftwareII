package DatabaseAccessObject;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;
import javafx.fxml.FXML;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.ResolverStyle;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Countries
 */
public class CountriesAccess {

    /**
     * Will query QB and return list of countries
     * @return
     * @throws SQLException
     */
    public static ObservableList<Countries> getCountries() throws SQLException {

        ObservableList<Countries> countriesObservableList = FXCollections.observableArrayList();

        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.countries";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("Country");

            Countries countries = new Countries(countryID, country);

            countriesObservableList.add(countries);

        }//end of while loop

        JDBC.closeConnection();
        return countriesObservableList;
    }//end of ObservableList<Countries> getCountries() method


}
