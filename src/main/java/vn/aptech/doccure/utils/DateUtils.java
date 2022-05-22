package vn.aptech.doccure.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static String toStandardDate(LocalDateTime input) {
        return input.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public static String toStandardTime(LocalDateTime input) {
        return input.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
