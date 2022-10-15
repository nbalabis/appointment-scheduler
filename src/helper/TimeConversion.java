package helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    public static String localToUTC(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime())));
    }

    public static String UTCToLocal(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(new Date().getTime())));
    }

    public static String localToEST(Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimeZone etTimeZone = TimeZone.getTimeZone("America/New_York");
        dateFormat.setTimeZone( etTimeZone );
        return dateFormat.format(date.getTime());
    }
}
