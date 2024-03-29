package model;

/**
 * A model of the Appointments Table to use when filling out table.
 *
 * @author Nicholas Balabis
 */
public class AppointmentsTable {
    String title, description, location, type, start, end;
    Integer aptID, customerID, userID, contactID;

    /**
     * AppointmentsTable constructor.
     *
     * @param title Appointment Title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param start Appointment start date/time.
     * @param end Appointment end date/time.
     * @param aptID Appointment ID.
     * @param customerID Customer ID.
     * @param userID User ID.
     * @param contactID Contact ID.
     */
    public AppointmentsTable(String title, String description, String location, String type, String start, String end, Integer aptID, Integer customerID, Integer userID, Integer contactID){
        this.aptID = aptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getAptID() {
        return aptID;
    }

    public void setAptID(Integer aptID) {
        this.aptID = aptID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }
}
