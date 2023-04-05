package helper;

import Model.Appointments;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will do time conversions for us.
 * Each method is described below
 */
public class TimeLogicConverter {

    /**
     * Method to get time information from Combo boxes and combine them into a single DateTime string
     * Will turn Am/PM times into the 24 hours Time format
     * @param datePicked
     * @param hour
     * @param minute
     * @param pmFlag
     * @return
     */
    public static String convertTime(LocalDate datePicked, String hour, String minute, boolean pmFlag) {
        if (pmFlag) {
            int tempHour= Integer.parseInt(hour) + 12;
            hour = String.valueOf(tempHour);
        }

        if (hour.equals("12") || hour.equals("24")) {
            int tempHour= Integer.parseInt(hour) - 12;
            hour = String.valueOf(tempHour);
        }
        hour = String.format("%02d", Integer.parseInt(hour));

        String dateTimeString = datePicked + " " + hour + ":" + minute + ":00";
        return dateTimeString;
    }

    /**
     * will take time from DB, split its properties, and fill in time fields in form for Modify Appointment
     * @param givenDateTime
     * @return
     */
    public static String[] deconvertTime(String givenDateTime) {
        //String givenDateFromModify = givenDateTime.toString();
        String datePicked = givenDateTime.substring(0, 10);
        String hour = givenDateTime.substring(11, 13);
        String minute = givenDateTime.substring(14, 16);
        //System.out.println(datePicked + " " + hour + " " + minute);
        //format is yyyy-mm-dd HH:MM:SS. do if time > 12, fill AM_PM as PM on the Modify controller. Done need to worry about 12 or 0
        String[] dateTimeArray = {datePicked, hour, minute };
        return dateTimeArray;
    }

    /**
     *
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime convertStringToDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime locateDateAndTime = LocalDateTime.parse(dateTimeString, formatter);

        return locateDateAndTime;
    }


    /**
     * Method to convert dateTimeString into UTC to put into the database
     * @param dateTime
     * @return
     */
    public static String convertDateTimeToUTC(String dateTime) {
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime locateDateAndTime = LocalDateTime.parse(dateTime, formatter);
        */

        LocalDateTime locateDateAndTime = convertStringToDateTime(dateTime);
        //System.out.println("localDatetime: " + locateDateAndTime);
        //turns LocalDateTime variable into ZonedTimeStamp wwith Local Time Zone
        ZonedDateTime zoneDateTime = locateDateAndTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));

        //converts localTime zone timestamp to UTC TimeStamp equivalent
        ZonedDateTime utcDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        //finds difference in time zone.
        //LocalDateTime localOffset = utcDateTime.toLocalDateTime();
        String utcReturn = utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return utcReturn;
    }

    /**
     * Takes UTC Time from Database, converts it to local time of Machine. To display localTime on Modify Appointment Form
     * @param utcDateTime
     * @return
     */
    public static String convertUTCToLocalDateTimeZone(LocalDateTime utcDateTime) {

        ZonedDateTime zoneDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localTimeConvert = zoneDateTime.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        String localTimeZoneString = localTimeConvert.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //return localTimeZoneTime;
        return localTimeZoneString;
    }

    /**
     * turns UTC time from DB into EST, so validate against business time
     * @param utcDateTime
     * @return
     */
    public static ZonedDateTime convertUTCToEST(String utcDateTime) {
        /*convert code to follow DRY protocols
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime locateDateAndTime = LocalDateTime.parse(dateTime, formatter);
        */

        //takes UTC time string and converts to LocalDateTime
        LocalDateTime localDateTimeIntermediate = convertStringToDateTime(utcDateTime);

        //assign UTC time zone to DateTime variable
        ZonedDateTime zoneDateTimeIntermediate = localDateTimeIntermediate.atZone(ZoneId.of("UTC"));
        ZonedDateTime estConvertedTime = zoneDateTimeIntermediate.withZoneSameInstant(ZoneId.of(ZoneId.of("America/New_York").toString()));

        //System.out.println("utc: " + utcDateTime);
        //System.out.println("est: " + estConvertedTime);

        return estConvertedTime;

    }

    /**
     * Will take time created by user and validate it against EST business hours
     * @param userLocalDateTime
     * @return
     */
    public static LocalDateTime convertUserLocalToEST(LocalDateTime userLocalDateTime) {

        ZonedDateTime zoneDateTimeIntermediate = userLocalDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime estConvertedTime = zoneDateTimeIntermediate.withZoneSameInstant(ZoneId.of(ZoneId.of("America/New_York").toString()));

        LocalDateTime estLocalDateTime = estConvertedTime.toLocalDateTime();

        return estLocalDateTime;
    }

    /**
     * method that gets local time, and formats it into desired time to be used.
     * @return
     */
    public static String getLocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatter);

        return formattedNow;
    }

    /**
     * Would convert UTC to local time. not necessary, so unused
     * @param appointmentsListFromDB
     * @return
     */
    public static ObservableList<Appointments> convertObservableListUTCtoLocal (ObservableList<Appointments> appointmentsListFromDB) {

        for (Appointments appointment: appointmentsListFromDB) {
            String convertedStartTime = TimeLogicConverter.convertUTCToLocalDateTimeZone(appointment.getAppointmentStart());
            String convertedEndTme = TimeLogicConverter.convertUTCToLocalDateTimeZone(appointment.getAppointmentEnd());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            appointment.setAppointmentStart(LocalDateTime.parse(convertedStartTime, formatter));
            appointment.setAppointmentEnd(LocalDateTime.parse(convertedEndTme, formatter));
        }

        return appointmentsListFromDB;
    }

    /**
     * initializes hash map
     */
    static HashMap<Integer, String> monthIntToString = new HashMap<>();

    /**
     * will set our hashmap
     */
    public static void setMonthHashMap() {
        monthIntToString.put(1, "January");
        monthIntToString.put(2, "February");
        monthIntToString.put(3, "March");
        monthIntToString.put(4, "April");
        monthIntToString.put(5, "May");
        monthIntToString.put(6, "June");
        monthIntToString.put(7, "July");
        monthIntToString.put(8, "August");
        monthIntToString.put(9, "September");
        monthIntToString.put(10, "October");
        monthIntToString.put(11, "November");
        monthIntToString.put(12, "December");
    }

    /**
     * Method will take integer for month from DB, and transform it into correct String ie->January
     * @param monthInt
     * @return
     */
    public static String convertMonthIntToString(int monthInt) {
        String monthString = monthIntToString.get(monthInt);


        return monthString;
    }


    /**
     * When user select start date, end date will automatically be selected.
     * Lambda Expression #2 is here. We are disabling end days that occur before start days
     * Appointment end should never be before start
     * @param startDatePicker
     * @param endDatePicker
     */
    public static void setEndDate(DatePicker startDatePicker, DatePicker endDatePicker) {
        LocalDate startDate = startDatePicker.getValue();
        endDatePicker.setValue(startDate);

        /**
         * Lambda Expression #2 here.
         * Disables potential end days that occur before start day
         */
        endDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate endDatePicker, boolean empty) {
                super.updateItem(startDate, empty);
                setDisable(endDatePicker.isBefore(startDate));
            }
        });

    }






}
