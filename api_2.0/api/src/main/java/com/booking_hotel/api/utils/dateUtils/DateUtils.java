package com.booking_hotel.api.utils.dateUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_TIME_FORMAT_EMAIL = "HH:mm, dd MMMM yyyy";

    public static String now(String pattern) {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatZonedDateTimeString(String dateTimeString) {
        System.out.println(dateTimeString);
        ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return formatZonedDateTime(dateTime, DATE_TIME_FORMAT_EMAIL);
    }

    public static String formatZonedDateTime(ZonedDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
