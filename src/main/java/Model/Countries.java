package Model;

public class Countries {

    private int countryID;
    private String county;

    /**
     * Countries includes the following parameters. We obtained this list from the database.
     * This is the desired country information.
     * @param countryID
     * @param countries
     */
    public Countries(int countryID, String countries) {

        this.countryID = countryID;
        this.county = countries;

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
    public String getCounty() {
        return county;
    }

    /**
     *
     * @param county
     */
    public void setCounty(String county) {
        this.county = county;
    }
}
