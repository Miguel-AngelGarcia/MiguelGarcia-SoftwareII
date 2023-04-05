package helper;

import DatabaseAccessObject.AppointmentsAccess;
import Model.Appointments;
import Model.DivisionsCountries;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;

/**
 * This will run our errorChecker methods, Should demonstrate advanced error planning
 * Methods are described below.
 */
public class ErrorChecker {

    /**
     * Will check to see if each Textfield is empty
     * @param textField
     * @return
     */
    public static Boolean textFieldChecker(TextField textField) {

        if (textField.getText().equals("")){
            return false;
        } else {
            return true;
        }
    }

    /**
     * will check to see if combox has a selected item
     * @param comboBox
     * @return
     */
    public static Boolean comboBoxChecker(ComboBox comboBox) {

        if (comboBox.getSelectionModel().isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    /**
     * checks to see if date fields are filled in
     * @param dateField
     * @return
     */
    public static Boolean dateFieldChecker(DatePicker dateField) {

        if (dateField.getValue() == null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * will highlight fields that are not filled in.
     * Returns alert to user if error
     * @param textFieldsToCheck
     * @param comboBoxesToCheck
     * @param datePickersToCheck
     * @return
     */
    public static Boolean requiredFieldsChecked(TextField[] textFieldsToCheck, ComboBox[] comboBoxesToCheck, DatePicker[] datePickersToCheck) {
        Boolean allFieldsPass = true;

        int typeFieldsFailCount = 0;

        //checks TextField to see if it empty
        for (int i = 0; i < textFieldsToCheck.length; i++) {

            Boolean checkedTextField = ErrorChecker.textFieldChecker(textFieldsToCheck[i]);
            //will add red border to any fields that are not filled out
            if(!checkedTextField) {
                textFieldsToCheck[i].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
                        new CornerRadii(3), BorderWidths.DEFAULT)));

                typeFieldsFailCount++;
                allFieldsPass = false;

            } else if (checkedTextField) {
                textFieldsToCheck[i].setStyle("-fx-border-color: transparent");
            }
        }

        //makes sure combo box choices have been select (not empty)
        for (int i = 0; i < comboBoxesToCheck.length; i++) {

            Boolean checkedComboBox = ErrorChecker.comboBoxChecker(comboBoxesToCheck[i]);
            //will add red border to any fields that are not filled out
            if(!checkedComboBox) {
                comboBoxesToCheck[i].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
                        new CornerRadii(3), BorderWidths.DEFAULT)));

                typeFieldsFailCount++;
                allFieldsPass = false;

            } else if (checkedComboBox) {
                comboBoxesToCheck[i].setStyle("-fx-border-color: transparent");
            }
        }

        //makes sure datePickers are not empty
        for (int i = 0; i < datePickersToCheck.length; i++){
            Boolean checkedDatePicker = ErrorChecker.dateFieldChecker(datePickersToCheck[i]);

            if (!checkedDatePicker) {
                datePickersToCheck[i].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
                        new CornerRadii(3), BorderWidths.DEFAULT)));

                typeFieldsFailCount++;
                allFieldsPass = false;

            } else if (checkedDatePicker) {
                comboBoxesToCheck[i].setStyle("-fx-border-color: transparent");
            }


        }

        if (typeFieldsFailCount > 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please correctly fill out required fields.");
            alert.show();
        }

        return allFieldsPass;
    }

    /**
     * Checks to see if times are within EST business hours
     * @param startDateTimeUTC
     * @param endDateTimeUTC
     * @return
     */
    public static Boolean withinBusinessHoursEST(String startDateTimeUTC, String endDateTimeUTC) {
        //initialize
        Boolean withinHours = null;

        //of time zone EST
        ZonedDateTime candidateStartDateTime = TimeLogicConverter.convertUTCToEST(startDateTimeUTC);
        ZonedDateTime candidateEndDateTime = TimeLogicConverter.convertUTCToEST(endDateTimeUTC);

        LocalTime candidateStartTime = candidateStartDateTime.toLocalTime();
        LocalTime candidateEndTime = candidateEndDateTime.toLocalTime();

        //start time 8:00AM & end time 10PM
        LocalTime estStartTime = LocalTime.of(8, 00);
        LocalTime estEndTime = LocalTime.of(22, 00);


        //Appoinments must be between 8AM EST and 10PM EST
        //An appointment can start AT 8est but not at 10PM
        Boolean candidateStartTimePasses = candidateStartTime.isBefore(estEndTime) && (candidateStartTime.isAfter(estStartTime) || candidateStartTime.equals(estStartTime));
        //An appointment can end AT 10PM, but not at 8am
        Boolean candidateEndTimePasses = (candidateEndTime.isBefore(estEndTime) || candidateEndTime.equals(estEndTime) ) && candidateEndTime.isAfter(estStartTime);

        Boolean[] withinHoursChecker = {candidateStartTimePasses, candidateEndTimePasses};

        //System.out.println("converted est start: " + candidateStartTime + " converted est end: " + candidateEndTime);
        //System.out.println("start: " + candidateStartTimePasses + " end: " + candidateEndTimePasses);
        if (candidateEndTime.equals(estStartTime)){
            withinHours = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed end time cannot occur at the beginning of business hours");
            alert.setContentText("Please select an end time between 8:00AM EST - 10:00PM EST");
            alert.show();

        } else if (candidateStartTime.equals(estEndTime)) {
            withinHours = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed start time cannot occur at the end of business hours");
            alert.setContentText("Please select a start time between 8:00AM EST - 10:00PM EST");
            alert.show();

        } else if (!withinHoursChecker[0] && (withinHoursChecker[1])) {
            withinHours = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed start time is outside of Business hours");
            alert.setContentText("Please select a start time between 8:00AM EST - 10:00PM EST");
            alert.show();

        } else if (!withinHoursChecker[1] && withinHoursChecker[0]) {
            withinHours = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed end time is outside of Business hours");
            alert.setContentText("Please select an end time between 8:00AM EST - 10:00PM EST");
            alert.show();
            withinHours = false;

        } else if (!withinHoursChecker[0] && !withinHoursChecker[1]) {
            withinHours = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed start & end times are outside of Business hours");
            alert.setContentText("Please select an appointment time between 8:00AM EST - 10:00PM EST");
            alert.show();
            withinHours = false;

        } else {
            withinHours = true;
        }


        return withinHours;
    }

    /**
     * checks to see if user generated appointment time is within business hours
     * @param startDateTimeUTC
     * @param endDateTimeUTC
     * @return
     */
    public static boolean appointmentOnWeekday(String startDateTimeUTC, String endDateTimeUTC) {
        //initialize
        Boolean onWeekDay = null;

        //of time zone EST
        ZonedDateTime candidateStartDateTime = TimeLogicConverter.convertUTCToEST(startDateTimeUTC);
        ZonedDateTime candidateEndDateTime = TimeLogicConverter.convertUTCToEST(endDateTimeUTC);

        DayOfWeek candidateStartDayOfWeek = candidateStartDateTime.getDayOfWeek();
        DayOfWeek candidateEndDayOfWeek = candidateEndDateTime.getDayOfWeek();

        //since you cannot make appointments that are not on same EST day, no need to check if end date is on weekend.
        if (candidateStartDayOfWeek.equals(DayOfWeek.SATURDAY)){
            onWeekDay = false;
        } else if (candidateEndDayOfWeek.equals(DayOfWeek.SUNDAY)) {
            onWeekDay = false;
        } else {
            onWeekDay = true;
        }

        if(!onWeekDay) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment cannot take place on the weekend.");
            alert.setContentText("Please select a day between Monday-Friday EST");
            alert.show();
        }

        return onWeekDay;
    }

    /**
     * checks if user generated appointment takes place on the same day (EST)
     * @param startDateTimeUTC
     * @param endDateTimeUTC
     * @return
     */
    public static Boolean startDayEndDayOnSameDay(String startDateTimeUTC, String endDateTimeUTC){
        Boolean startEndSameDay = null;

        //we take the UTC string and convert it to EST
        ZonedDateTime candidateStartDateTime = TimeLogicConverter.convertUTCToEST(startDateTimeUTC);
        ZonedDateTime candidateEndDateTime = TimeLogicConverter.convertUTCToEST(endDateTimeUTC);

        //get dates only
        LocalDate candidateStartDate = candidateStartDateTime.toLocalDate();
        LocalDate candidateEndDate = candidateEndDateTime.toLocalDate();

        //make sure dates are are the same. Cannot have appointment start one day and end the next
        if (candidateStartDate.equals(candidateEndDate)) {
            startEndSameDay = true;
        } else {
            startEndSameDay = false;
        }

        if(!startEndSameDay) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Start day and end day must be the same ");
            alert.setContentText("Please choose the same day (EST) for appointment start & end.");
            alert.show();
        }

        return startEndSameDay;
    }

    /**
     * Makes sure start time is before end time
     * @param startTimeString
     * @param endTimeString
     * @return
     */
    public static Boolean startBeforeEndChecker(String startTimeString, String endTimeString) {
        LocalDateTime startDateTime = TimeLogicConverter.convertStringToDateTime(startTimeString);
        LocalDateTime endDateTime = TimeLogicConverter.convertStringToDateTime(endTimeString);

        //checks to make sure end time =/= start time ex -> 8am-8am
        Boolean endEqualsStart = startDateTime.equals(endDateTime);

        if (endEqualsStart) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed start time cannot be the same as end time.");
            alert.setContentText("Please select an end time that occurs after the start time.");
            alert.show();
            return false;
        }

        Boolean startBeforeEnd = startDateTime.isBefore(endDateTime);

        if (!startBeforeEnd) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposed end time can not be before");
            alert.setContentText("Please select an end time that occurs after the start time.");
            alert.show();
            return false;
        }


        //to get a pass, start must occur before end AND end time cannot equal start time
        Boolean bothPass = startBeforeEnd && !endEqualsStart;
        return bothPass;
    }

    /**
     * makes sure proposed new appointment times do not overlapped with already scheduled appointments
     * @param appointmentsListFromController
     * @param utcStartTimeString
     * @param utcEndTimeString
     * @param candidateApptID
     * @return
     */
    //get from obersavble list
    public static Boolean apptTimeChecker(ObservableList<Appointments> appointmentsListFromController, String utcStartTimeString,
                                          String utcEndTimeString, int candidateApptID) {
        //turns UTC dateTime from potential new appointment into EST
        Boolean noOverlap = null;

        if (appointmentsListFromController.size() == 0) {
            return true;
        }

        ZonedDateTime interStartDateTimeEST = TimeLogicConverter.convertUTCToEST(utcStartTimeString);
        ZonedDateTime interEndDateTimeEST = TimeLogicConverter.convertUTCToEST(utcEndTimeString);

        LocalDateTime candidateStartTimeDateEST = interStartDateTimeEST.toLocalDateTime();
        LocalDateTime candidateEndDateTimeEST = interEndDateTimeEST.toLocalDateTime();


        //think variable as: "Candidate [variable] Production" -> Candidate [startEquals] Production [start/end or both]
        Boolean startEquals = false;
        Boolean endEquals = false;
        Boolean startInBetween = false;
        Boolean endInBetween = false;
        Boolean doesOverlap = false; // here, appt start & end both need to be either BEFORE production start OR end.
        //concerned about creating a 9:15-10 appoinment with 9:30 - 9:45 exists.
        //need these two to determine above
        Boolean firstTestPass = false;
        Boolean secondTestPass = false;

        //LOGIC: if candidate Start or END is in between any Production Appointment's Start or End, it overlaps -> FAIL
        //onlt need to check for appointments on same day
        for (Appointments appointment: appointmentsListFromController) {
            //get appointments on the same day. First, convert UTC string to EST. We will be working in EST as that is business hours
            //turns UTC dateTime from Appointment Objects into EST
            //GOT A BUG HERE> ERROR. cannot parse index 10, "T" in 2020-05-29T00:00' -> could not be parsed at index 10
            String prodApptStart = appointment.getAppointmentStart().toString() + ":00";
            String prodApptEnd = appointment.getAppointmentEnd().toString() + ":00";
            int prodApptID = appointment.getAppointmentID();
            String start10 = Character.toString(prodApptStart.charAt(10));
            String end10 = Character.toString(prodApptStart.charAt(10));

            LocalDateTime prodApptStartDateTime = TimeLogicConverter.convertUserLocalToEST(appointment.getAppointmentStart());
            LocalDateTime prodApptEndDateTime = TimeLogicConverter.convertUserLocalToEST(appointment.getAppointmentEnd());

            /* DONT NEED.Times from appointmentsListFromCtroller are already UserTimeZone. SHOULD NOT CONVERT AGAIN
            //IT IS A LocalDateTime variable
            //need to turn 2020-05-29T00:00 into -> 2020-05-29T00:00:00 (ADD EXTRA :00)
            if (start10.equals("T")) {
                prodApptStart = prodApptStart.replace("T", " ");
            }
            if (end10.equals("T")) {
                prodApptEnd = prodApptEnd.replace("T", " ");
            }

            //in EST
            ZonedDateTime productionApptStart = TimeLogicConverter.convertUTCToEST(prodApptStart);
            ZonedDateTime productionApptEnd = TimeLogicConverter.convertUTCToEST(prodApptEnd);
            */
            /*
            LocalDate prodApptDate = productionApptStart.toLocalDate();
            */

            //we are using everything in EST, so need to convert appointmentsFromList to EST

            LocalDate prodApptDate = appointment.getAppointmentStart().toLocalDate();
            LocalDate candidateApptDate = candidateStartTimeDateEST.toLocalDate();

            Boolean sameApptID = candidateApptID == prodApptID;

            //if Production Appointments have same date as candidate, then we will check those and their times to see if any overlap exists
            if (prodApptDate.equals(candidateApptDate) && !sameApptID) {

                //LocalDateTime prodApptStartDateTime = productionApptStart.toLocalDateTime();
                //LocalDateTime prodApptEndDateTime = productionApptEnd.toLocalDateTime();

                if (candidateStartTimeDateEST.equals(prodApptStartDateTime)) {
                    startEquals = true;
                }

                if (candidateEndDateTimeEST.equals(prodApptEndDateTime)) {
                    endEquals = true;
                }

                if (candidateStartTimeDateEST.isAfter(prodApptStartDateTime) && candidateStartTimeDateEST.isBefore(prodApptEndDateTime)) {
                    startInBetween = true;
                }

                if (candidateEndDateTimeEST.isAfter(prodApptStartDateTime) && candidateEndDateTimeEST.isBefore(prodApptEndDateTime)) {
                    endInBetween = true;
                }

                //this will be the Booleans that determin if OR is true or false
                if (candidateStartTimeDateEST.isBefore(prodApptStartDateTime) && (candidateEndDateTimeEST.isBefore(prodApptEndDateTime) || candidateEndDateTimeEST.equals(prodApptStartDateTime))) {
                    firstTestPass = true;
                }

                if ((candidateStartTimeDateEST.isAfter(prodApptStartDateTime) || candidateStartTimeDateEST.equals(prodApptEndDateTime)) && candidateEndDateTimeEST.isAfter(prodApptEndDateTime)) {
                    secondTestPass = true;
                }

                if (!firstTestPass && !secondTestPass) {
                    doesOverlap = true;
                }
                //above not working
                //,maybe try -> if candidateStart NOT [after Production Start OR equalTo] AND [before Production End OR equalTo]
                //-> if candidateEND NOT after Production Start AND before Production End
                // if Candidate start == production start -> FAIL
                // if Candidate end == production end -> FAIL
                //if Candidate Start between Production Start AND Production end -> FAIL
                //if Candidate End between Production Start AND Production end -> FAIL


            }


            if(startEquals) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Proposed appointment start time equals an already made appointment");
                alert.setContentText("Please select a start time after said appointment.");
                alert.show();
                return false;

            } else if (endEquals) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Proposed appointment end time equals an already made appointment");
                alert.setContentText("Please select an end time before said appointment.");
                alert.show();
                return false;

            } else if (startInBetween) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Proposed appointment start time is in between an already made appointment");
                alert.setContentText("Please select a start time after said appointment.");
                alert.show();
                return false;

            }else if (endInBetween){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Proposed appointment end time is in between an already made appointment");
                alert.setContentText("Please select an end time before said appointment.");
                alert.show();
                return false;

            } else if (doesOverlap) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Proposed appointment overlaps with an already made appointment");
                alert.setContentText("Please select start and end times before or after said appointment.");
                alert.show();
                return false;

            } else {
                noOverlap = true;
            }

        } // end of appointment for loop

        return noOverlap;
    }




}
