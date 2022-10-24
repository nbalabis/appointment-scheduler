package helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper for manipulating dates and times.
 *
 * @author Nicholas Balabis
 */
public class TimeConversion {
    /**
     * Converts a String to LocalDate with yyyy-MM-dd formatting.
     *
     * @param dateString Date to be formatted.
     * @return Formatted LocalDate.
     */
    public static LocalDate formatDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Converts a date and two integers into a Date object.
     *
     * @param date LocalDate.
     * @param hour Integer representing hours.
     * @param minute Integer representing minutes.
     * @return Formatted date.
     * @throws ParseException Throws ParseException.
     */
    public static Date convertToDate(LocalDate date, Integer hour, Integer minute) throws ParseException {
        //concatenate date data
        String dateString = date.toString() + " " + hour + ":" + minute;

        //convert to date type
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return  dateFormat.parse(dateString);
    }

    /**
     * Converts a date in local time zone to UTC.
     *
     * @param date Date in local time zone.
     * @return UTC date.
     */
    public static String localToUTC(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime())));
    }

    /**
     * Converts a date in UTC time zone to local.
     *
     * @param date Date in UTC time zone.
     * @return Local date.
     */
    public static String UTCToLocal(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(new Date().getTime())));
    }

    /**
     * Converts a date in local time zone to EST.
     *
     * @param date Date in local time zone.
     * @return EST date.
     * @throws ParseException Throws ParseException.
     */
    public static String localToEST(Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimeZone etTimeZone = TimeZone.getTimeZone("America/New_York");
        dateFormat.setTimeZone( etTimeZone );
        return dateFormat.format(date.getTime());
    }
}
