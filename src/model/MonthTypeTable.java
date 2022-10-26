package model;

/**
 * A model of the Month/Type Table to use when filling out table.
 *
 * @author Nicholas Balabis
 */
public class MonthTypeTable {
    String month, type;
    Integer count;

    /**
     * MonthTypeTable constructor.
     *
     * @param month Month.
     * @param type type.
     * @param count count.
     */
    public MonthTypeTable(String month, String type, Integer count){
        this.month = month;
        this.type = type;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
