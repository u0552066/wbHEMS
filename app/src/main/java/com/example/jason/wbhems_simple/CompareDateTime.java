package com.example.jason.wbhems_simple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareDateTime {
    //CompareDateTime.isDateOneBigger(str1,(str2));
    //str1 > str2  =>true
    //now >  str2 =>true
    /*
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    public static boolean isDateOneBigger(String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        Date now = new Date() ; // 獲取當前時間
        Date dt2 = null;
        try {
           // now = sdf.parse();
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (now.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (now.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }
}

