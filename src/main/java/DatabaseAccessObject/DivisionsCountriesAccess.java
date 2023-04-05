package DatabaseAccessObject;

import Model.DivisionsCountries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.ResolverStyle;

import static helper.JDBC.connection;

/**
 * Will allow user to access the database and retrieve information on Divisions-to-countries
 */
public class DivisionsCountriesAccess {

    /**
     * Will take CountrName, query DB and return its DivisionID
     * @param countryName
     * @return
     * @throws SQLException
     */
    public static ObservableList<DivisionsCountries> getDivisionFromCountryName(String countryName) throws SQLException{

        ObservableList<DivisionsCountries> divisionsCountriesObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT first_level_divisions.Division_ID, first_level_divisions.Division, first_level_divisions.Country_ID, " +
                "countries.Country " +
        "FROM client_schedule.first_level_divisions " +
        "LEFT JOIN client_schedule.countries ON first_level_divisions.Country_ID = countries.Country_ID " +
        "WHERE countries.Country = '" + countryName + "'";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("Country");

            DivisionsCountries divisionsCountries = new DivisionsCountries(divisionID, division, countryID, country);

            divisionsCountriesObservableList.add(divisionsCountries);

        }// end of while loop

        JDBC.closeConnection();
        return divisionsCountriesObservableList;

    }

    /**
     * Would cycle through DivisionIDs and match corresponding countries. Not used
     * @param divisionsCountriesObservableList
     * @param divisionID
     * @return
     */
    public static DivisionsCountries lookupDivisionsCountries(ObservableList<DivisionsCountries> divisionsCountriesObservableList, int divisionID) {

        for (DivisionsCountries divisionsCountries: divisionsCountriesObservableList){
            if(divisionsCountries.getCountryID() == divisionID)
                return divisionsCountries;

        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No item found");
        alert.show();
        return null;
    }

}
