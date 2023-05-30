package com.training.erp.util;

import java.sql.Timestamp;
import java.util.Calendar;


public class UtilMethods {



    public static Timestamp calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
