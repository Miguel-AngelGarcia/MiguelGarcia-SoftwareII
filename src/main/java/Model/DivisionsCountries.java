package Model;

public class DivisionsCountries {

    private int divisionID;
    private String division;
    private int countryID;
    private String country;

    /**
     * This is a customer class. We want to be able to look up country name and division name easily
     * @param divisionID
     * @param division
     * @param countryID
     * @param country
     */
    public DivisionsCountries(int divisionID, String division, int countryID, String country) {

        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
        this.country = country;

    }

    /**
     *
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @param divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     *
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @return
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *
     * @param countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }



}
