package com.samuelnotes.commonlibs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 系统工具�? * 
 * 
 */
public final class CommUtils {
    /**
     * 根据时间返回Calendar
     * 
     * @param dateStr
     * @return
     */
    public static Calendar calendarConver(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
