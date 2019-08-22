package com.makatizen.makahanap.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.ocpsoft.prettytime.PrettyTime;

public class DateUtils {


    public static String DateFormat(String oldStringDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldStringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldStringDate;
        }

        return newDate;
    }

    public static String DateToTimeFormat(String oldStringDate) {
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldStringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static String getLocalCurrentDate() {
        Date date = new Date();
        String strDateFormat = "yyyy/MM/dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }

}
