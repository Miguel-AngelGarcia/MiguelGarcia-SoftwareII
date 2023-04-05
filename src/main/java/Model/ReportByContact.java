package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReportByContact {

    private int contactID;
    private String contactName;
    private int appointmentID;
    private int appointmentCustomerID;
    private String appointmentTitle;
    private String appointmentType;
    private String appointmentDescription;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;


    /**
     * constructor for our Reports object: appointmentsByContact
     * @param contactID
     * @param appointmentCustomerID
     * @param appointmentTitle
     * @param appointmentType
     * @param appointmentDescription
     * @param appointmentStart
     * @param appointmentEnd
     */
    public ReportByContact(int contactID, String contactName, int appointmentID, int appointmentCustomerID, String appointmentTitle, String appointmentType, String appointmentDescription,
                           LocalDateTime appointmentStart, LocalDateTime appointmentEnd) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.appointmentID = appointmentID;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentType = appointmentType;
        this.appointmentDescription = appointmentDescription;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
    }

    /**
     *
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     *
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     *
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     *
     * @return
     */
    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    /**
     *
     * @param appointmentCustomerID
     */
    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID = appointmentCustomerID;
    }

    /**
     *
     * @return
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     *
     * @param appointmentTitle
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
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
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     *
     * @param appointmentDescription
     */
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    /**
     *
     * @param appointmentStart
     */
    public void setAppointmentStart(LocalDateTime appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    /**
     *
     * @param appointmentEnd
     */
    public void setAppointmentEnd(LocalDateTime appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }
}
