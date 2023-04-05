package Model;

public class FirstLevelDivisions {

    private int divisionID;
    private String division;
    private int countryID;

    /**
     * FirstLevelDivisions includes the following parameters. We obtained this list from the database.
     * This is the desired first level division information.
     * @param divisionID
     * @param division
     * @param countryID
     */
    public FirstLevelDivisions(int divisionID, String division, int countryID) {

        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;

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

}
