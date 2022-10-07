package model;

import java.time.LocalDate;

public class Appointment {
    public static void add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) {
        //format dates
        String start = startDate.toString() + " " + startHour + ":" + startMinute;
        String end = endDate.toString() + " " + endHour + ":" + endMinute;

//        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID)
//        VALUES ("Test Title", "Fake description", "Nowhere", "Made Up Type", 2, "2012-06-18 0:0", "2022-11-08 11:34:22", 1, 2);"
    }
}
