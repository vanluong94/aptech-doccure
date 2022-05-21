package vn.aptech.doccure.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String toStandardDate(LocalDateTime input) {
        return input.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public static String toStandardTime(LocalDateTime input) {
        return input.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

}
