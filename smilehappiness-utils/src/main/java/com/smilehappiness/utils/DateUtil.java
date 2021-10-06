package com.smilehappiness.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 日期统一工具类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/10/3 11:35
 */
public class DateUtil extends DateUtils {

    /**
     * 日期格式：yyyyMMddHHmmssSSS
     */
    public static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";

    /**
     * 日期格式：yyyyMMddHHmmss
     */
    public static String YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";

    /**
     * The constant YYYYMMDDHHMMSS3.
     */
    public final static String YYYYMMDDHHMMSS3 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyMMdd
     */
    public static String YYMMDD = "yyMMdd";

    /**
     * 日期格式： yyyyMMdd
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    /**
     * 日期时间格式：dd-MM-yy
     */
    public static final String DD_MM_YY = "dd-MM-yy";
    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式： yyyy
     */
    public static final String YYYY = "yyyy";

    /**
     * 日期格式： MM
     */
    public static final String MM = "MM";

    /**
     * 日期格式： yyyyMM
     */
    public static final String YYYYMM = "yyyyMM";
    /**
     * 日期格式：yyyy年MM月dd日
     */
    public static final String YYYY_YEAR_MM_MONTH_DD_DAY = "yyyy年MM月dd日";

    /**
     * 时间格式：HH:mm
     */
    public static final String HH_COLON_MM = "HH:mm";
    /**
     * 时间格式：HHmm
     */
    public static final String HH_MM = "HHmm";


    /**
     * <p>
     * 计算请求耗时时间
     * <p/>
     *
     * @param start
     * @param end
     * @param timeUnit 统计单位
     * @return long
     * @Date 2021/8/28 15:08
     */
    public static long getTakeTime(LocalDateTime start, LocalDateTime end, TimeUnit timeUnit) {
        Duration duration = Duration.between(start, end);

        switch (timeUnit) {
            case DAYS:
                // 相差的天数
                return duration.toDays();
            case HOURS:
                // 相差的小时数
                return duration.toHours();
            case MINUTES:
                // 相差的分钟数
                return duration.toMinutes();
            case MILLISECONDS:
                // 相差毫秒数
                return duration.toMillis();
            case NANOSECONDS:
                // 相差的纳秒数
                return duration.toNanos();
            default:
                return 0;
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            if (StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat formater = new SimpleDateFormat(pattern);
            return formater.format(date);
        }
    }

    public static Date parse(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            try {
                return (new SimpleDateFormat(pattern)).parse(strDate);
            } catch (ParseException var3) {
                return null;
            }
        }
    }

    public static String strNow(String pattern) {
        return format(new Date(), pattern);
    }

    public static long getDiffSecond(Date start, Date end) {
        return (end.getTime() - start.getTime()) / 1000L;
    }

    public static long getDiffMinute(Date start, Date end) {
        long diffSeconds = getDiffSecond(start, end);
        return diffSeconds / 60L;
    }

    public static int getDiffDay(Date start, Date end) {
        start = getDayBegin(start);
        end = getDayBegin(end);
        if (start != null && end != null) {
            long diffSeconds = getDiffSecond(start, end);
            long day = diffSeconds / 60L / 60L / 24L;
            return (int) Math.floor((double) day);
        } else {
            return 0;
        }
    }

    public static int getDiffMonth(Date start, Date end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        return (endCalendar.get(1) - startCalendar.get(1)) * 12 + endCalendar.get(2) - startCalendar.get(2);
    }

    public static Date getDayBegin(Date date) {
        String format = DateFormatUtils.format(date, "yyyy-MM-dd");

        try {
            return parseDate(format.concat(" 00:00:00"), new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException var3) {
            return null;
        }
    }

    public static Date getDayEnd(Date date) {
        String format = DateFormatUtils.format(date, "yyyy-MM-dd");

        try {
            return parseDate(format.concat(" 23:59:59"), new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException var3) {
            return null;
        }
    }

    public static Date getMonthBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(5);
        cal.set(5, value);
        return getDayBegin(cal.getTime());
    }

    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(5);
        cal.set(5, value);
        return getDayEnd(cal.getTime());
    }

}