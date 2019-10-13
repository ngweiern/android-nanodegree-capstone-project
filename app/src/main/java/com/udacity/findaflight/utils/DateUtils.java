package com.udacity.findaflight.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    public static String getDateInDayMonthFormat(int day, int month, int year) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("d M yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM,EEEE");
        String originalDate = day + " " + month + " " + year;
        Date date = originalFormat.parse(originalDate);
        String formattedDate = targetFormat.format(date).toUpperCase();

        return formattedDate;
    }

    public static Date getDateFromString(String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }

    public static Date getDateTimeFromString(String dateTimeString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTimeString);
    }
}
