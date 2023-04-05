package Model;

/**
 * Class for ReportsByCountry Object, defined by author to fulfill project requirements.
 */
public class ReportsByCountry {

    private String customerCountry;
    private int customerCount;

    /**
     * constructor for our Reports object: customersByCountry
     * @param customerCountry
     * @param customerCount
     */
    public ReportsByCountry (String customerCountry, int customerCount) {
        this.customerCountry = customerCountry;
        this.customerCount = customerCount;
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

    /**
     *
     * @return
     */
    public int getCustomerCount() {
        return customerCount;
    }

    /**
     *
     * @param customerCount
     */
    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

}
