package Model;

import helper.TimeLogicConverter;

import java.util.HashMap;

public class ReportsByMonthType {

    private String appointmentMonth;
    private String appointmentType;
    private int appointmentCount;

    /**
     * constructor for our Reports object: appointmentsCountByMonthAndType
     * @param appointmentMonth
     * @param appointmentType
     * @param appointmentCount
     */
    public ReportsByMonthType (String appointmentMonth, String appointmentType, int appointmentCount) {
        this.appointmentMonth = appointmentMonth;
        this.appointmentType = appointmentType;
        this.appointmentCount = appointmentCount;

    }

    /**
     *
     * @return
     */
    public String getAppointmentMonth() {
        return appointmentMonth;
    }

    /**
     *
     * @param appointmentMonth
     */
    public void setAppointmentMonth(String appointmentMonth) {
        this.appointmentMonth = appointmentMonth;
    }

    /**
     *
     * @return
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     *
     * @param appointmentType
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     *
     * @return
     */
    public int getAppointmentCount() {
        return appointmentCount;
    }

    /**
     *
     * @param appointmentCount
     */
    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public void setMonthString(int monthInt) {
        String monthString = TimeLogicConverter.convertMonthIntToString(monthInt);
        this.appointmentMonth = monthString;
    }




}
