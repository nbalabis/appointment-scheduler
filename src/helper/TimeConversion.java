package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeConversion {
    public static LocalDate formatDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}
