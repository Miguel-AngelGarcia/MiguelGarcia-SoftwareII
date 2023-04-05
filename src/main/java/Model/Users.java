package Model;

public class Users {

    private int userID;
    private String userName;
    private String userPassword;

    /**
     * User class includes the following parameters. We obtained this list from the database.
     * This is the desired user information.
     * @param userID
     * @param userName
     * @param userPassword
     */
    public Users(int userID, String userName, String userPassword) {

        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;

    }

    /**
     *
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
