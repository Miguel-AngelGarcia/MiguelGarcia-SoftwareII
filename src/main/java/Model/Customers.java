package Model;

public class Customers {

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private int customerDivisionID;
    private String customerDivision;
    private int customerCountryID;
    private String customerCountry;


    /**
     * Customer class includes the following parameters. We obtained this list from the database.
     * This is the desired customer information.
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostalCode
     * @param customerPhone
     * @param customerDivisionID
     * @param customerDivision
     * @param customerCountryID
     * @param customerCountry
     * Below are the appropriate setter and getter functions.
     */
    public Customers(int customerID, String customerName, String customerAddress, String customerPostalCode,
                     String customerPhone, int customerDivisionID, String customerDivision, int customerCountryID, String customerCountry) {

        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivisionID = customerDivisionID;
        this.customerDivision = customerDivision;
        this.customerCountryID = customerCountryID;
        this.customerCountry = customerCountry;

    }

    /**
     *
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     *
     * @param customerAddress
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     *
     * @return
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     *
     * @param customerPostalCode
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     *
     * @param customerPhone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     *
     * @return
     */
    public int getCustomerDivisionID() {
        return customerDivisionID;
    }

    /**
     *
     * @param customerDivisionID
     */
    public void setDivisionID(int customerDivisionID) {
        this.customerDivisionID = customerDivisionID;
    }

    /**
     *
     * @return
     */
    public String getCustomerDivision() {
        return customerDivision;
    }

    /**
     *
     * @param customerDivision
     */
    public void setCustomerDivision(String customerDivision) {
        this.customerDivision = customerDivision;
    }

    /**
     *
     * @return
     */
    public int getCustomerCountryID() {
        return customerCountryID;
    }

    /**
     *
     * @param customerCountryID
     */
    public void setCustomerCountryID(int customerCountryID) {
        this.customerCountryID = customerCountryID;
    }

    /**
     *
     * @return
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     *
     * @param customerCountry
     */
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

}
