package model;

/**
 * A model of the Customers Table to use when filling out table.
 *
 * @author Nicholas Balabis
 */
public class CustomersTable {
    String customerName, address, postalCode, phone;
    Integer customerID, divisionID;

    /**
     * CustomersTable constructor.
     *
     * @param customerName Customer name.
     * @param address Customer address.
     * @param postalCode Customer postal code.
     * @param phone Customer phone number
     * @param customer_ID Customer ID.
     * @param divisionID Customer division ID.
     */
    public CustomersTable(String customerName, String address, String postalCode, String phone, Integer customer_ID, Integer divisionID) {
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.customerID = customer_ID;
        this.divisionID = divisionID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(Integer divisionID) {
        this.divisionID = divisionID;
    }
}
