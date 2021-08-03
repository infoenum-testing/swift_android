package com.swift.dating.common;

import android.util.Patterns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ValidationUtils {
    /*
     *** Method to check if email is valid
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
     *** Method to check if text is empty or not
     */
    public static boolean isEmpty(String text) {
        return text.matches("");
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /*
     *** Method to check if date is valid or not
     */
    public static boolean checkAgeIsValid(String sdate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        long year = 0L;
        try {
            Date sDate = df.parse(sdate);
            Date cDate = df.parse(df.format(new Date()));
            long timeOne = cDate.getTime();
            long timeTwo = sDate.getTime();
            long diff = timeOne - timeTwo;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            year = diffDays / 365;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (year >= 18) && (year <= 80);
    }

    /**
     * get current date and time of the device
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

}
