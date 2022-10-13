package helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TimeConversion {
    public static LocalDate formatDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    public static Date convertToDate(LocalDate date, Integer hour, Integer minute) throws ParseException {
        //concatenate date data
        String dateString = date.toString() + " " + hour + ":" + minute;

        //convert to date type
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return  dateFormat.parse(dateString);
    }

    public static String toUTC(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime())));
    }
}
