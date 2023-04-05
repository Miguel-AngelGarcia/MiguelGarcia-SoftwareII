package com.example.Controller;

import DatabaseAccessObject.AppointmentsAccess;
import DatabaseAccessObject.UsersAccess;
import Model.Appointments;
import helper.AuditLog;
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
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable{
    private static int currUserID;
    //no longer local, an instance
    private static ResourceBundle rb;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    @FXML
    private Label timezone;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView sadCat;
    @FXML
    private Rectangle errorBox;
    @FXML
    private Label language;
    @FXML
    private Button signOn;
    @FXML
    private Label location;
    @FXML
    private Label langResult;

    ObservableList<String> languages = FXCollections.observableArrayList("English", "French");
    @FXML
    private ChoiceBox languageBox;

    public LoginScreenController() {
    }



    /**
     * Will perform logic to sign user in
     * @param event
     * @throws Exception
     */
    @FXML
    void signOnButton (ActionEvent event) throws Exception {

        Boolean loginSuccess = null;

        try {
            String currUser = userName.getText();
            String currPass = password.getText();

            /*testing to see if getting user input is there
             *System.out.println(currUser);
             *System.out.print(currPass);
             **/

            int userID = UsersAccess.credentialsValidation(currUser, currPass);

            if (userID > 0) {
                currUserID = userID;
                loginSuccess = true;
                ObservableList<Appointments> appointmentsWithin15List = AppointmentsAccess.getAppointmentsWith15Min();

                if (!appointmentsWithin15List.isEmpty()){
                    //NEED Appointment ID, date, and time.
                    String apptInfo = "";
                    for (Appointments apptIn15: appointmentsWithin15List) {
                        apptInfo += rb.getString("Appointment ID:") + " " + apptIn15.getAppointmentID()  + " "
                                + rb.getString("starting at") + " " + apptIn15.getAppointmentStart().toString() + " "
                                + rb.getString("will begin within 15 minutes.") + " \n";
                    }

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(rb.getString("Appointment within 15 minutes"));
                    alert.setContentText(apptInfo);
                    alert.showAndWait();


                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    // allContactsList.forEach(contacts -> allContactNamesList.add(contacts.getContactName()));
                    //alert.setTitle("Alert");

                    alert.setHeaderText(rb.getString("Alert"));
                    alert.setContentText(rb.getString("There are no appointments within 15 minutes"));
                    alert.showAndWait();

                }

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                //stage.setScene(new Scene( scene));
                stage.setScene(new Scene((Parent) scene));
                stage.show();


            } else if (userID == 0) {
                loginSuccess = false;
                errorLabel.setOpacity(1.0);
                sadCat.setOpacity(1.0);
                errorBox.setOpacity(1.0);

            }


        } catch (NumberFormatException e) {
            /*
            DD A NEW WARNING
             */
            loginSuccess = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Form contains blank fields or invalid values.");
            alert.showAndWait();
            return;
        }

        AuditLog.userActivityLogger(userName.getText(), loginSuccess);
    }

    /**
     * will allow syste to retrieve userID throughout application use
     * @return
     */
    public static int getCurrUserID() {
        return currUserID;
    }

    /**
     * gets title for application
     * @return
     */
    public static String getTitle() {
        return rb.getString("Teto the Time Teller");
    }

    /**
     * initializes data to begin login process
     * sets labels according to data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //languageBox.setValue("English");
        //languageBox.setItems(languages);

        ZoneId zone = ZoneId.systemDefault();
        timezone.setText(zone.getDisplayName(TextStyle.FULL, Locale.ROOT));

        //Locale.setDefault(Locale.FRANCE);
        /*Locale locale = Locale.getDefault();
        Locale.setDefault(locale);*/

        //need help with resource bundle.

        rb = ResourceBundle.getBundle("login", Locale.getDefault());
        //System.out.println(rb);

        userName.setPromptText(rb.getString("Username"));
        password.setPromptText(rb.getString("Password"));
        language.setText(rb.getString("Language"));
        signOn.setText(rb.getString("Sign on"));
        location.setText(rb.getString("Location"));
        errorLabel.setText(rb.getString("Please enter a valid Username or Password"));
        langResult.setText(rb.getString("LangResult"));

        if (Locale.getDefault().getLanguage().equals("fr")) {
            errorBox.setWidth(360);
            errorLabel.setMinWidth(340);
            //timezone.setPrefWidth(130);
            location.setPrefWidth(80);
            //language.setPrefWidth(50);
        }

        TimeLogicConverter.setMonthHashMap();

        //language.setData(rb.getString("Language"));

    }
}
