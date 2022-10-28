package model;

/**
 * A model of the Location Table to use when filling out table.
 *
 * @author Nicholas Balabis
 */
public class LocationTable {
    String location;
    Integer count;

    /**
     * LocationTable constructor.
     *
     * @param location location.
     * @param count count.
     */
    public LocationTable(String location, Integer count){
        this.location = location;
        this.count = count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
