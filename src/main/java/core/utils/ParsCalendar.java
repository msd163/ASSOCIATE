package core.utils;

import core.enums.TtCalendarItem;
import core.enums.TtCompareResult;
import core.enums.TtSort;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import java.time.LocalTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author masoud
 * @version 1.95.05.06
 */
public class ParsCalendar {

    private static final ParsCalendar instance = new ParsCalendar();
    private static Calendar persCal;
    private static Calendar gregCal;

    ///========================================================
    ///---CONSTRUCTOR
    ///========================================================
    private ParsCalendar() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
    }

    /**============================
     Day of week
     ==============================**/
    /**
     * Returning title of day in week
     *
     * @return for example "شنبه"
     */
    public String getDayOfWeekTitle() {
        return getDayOfWeekTitle(persCal.get(Calendar.DAY_OF_WEEK));
    }

    private String getDayOfWeekTitle(int dayNumber) {

        switch (dayNumber) {
            case 7:
                return "شنبه";
            case 1:
                return "یکشنبه";
            case 2:
                return "دوشنبه";
            case 3:
                return "سه شنبه";
            case 4:
                return "چهارشنبه";
            case 5:
                return "پنجشنبه";
            case 6:
                return "جمعه";
        }
        return "";
    }


    /**============================
     Public
     ==============================**/

    /**
     * Refreshing Persian and Geog Date
     */
    public void refresh() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
    }

    /**
     * @param gShortDateTime
     * @return
     * @format: 2016-01-01 10:10
     */
    public String gregorianToJalaliDateTime(String gShortDateTime) {
        if (gShortDateTime == null || gShortDateTime.isEmpty()) {
            return "";
        }
        String[] ss = gShortDateTime.split(" ");
        if (ss.length != 2) {
            return "";
        }
        String[] tm = ss[1].split(":");

        int h, m, s;
        try {
            h = Integer.parseInt(tm[0]);
        } catch (Exception e) {
            h = 0;
        }
        try {
            m = Integer.parseInt(tm[1]);
        } catch (Exception e) {
            m = 0;
        }
        try {
            s = Integer.parseInt(tm[2]);
        } catch (Exception e) {
            s = 0;
        }
        return gregorianToJalaliDate(ss[0])
            + " " + ((h < 10) ? "0" + h : h)
            + ":" + (m < 10 ? "0" + m : m)
            + ":" + (s < 10 ? "0" + s : s);
    }

    /**
     * format: 2016-01-01
     *
     * @param gShortDate
     * @return
     */
    public String gregorianToJalaliDate(String gShortDate) {
        if (gShortDate == null || gShortDate.isEmpty()) {
            return "";
        }
        String[] s = gShortDate.split(" ")[0].split("-");
        if (s.length != 3) {
            return "";
        }

        gregCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        gregCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        gregCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));

        persCal.setTime(gregCal.getTime());

        return getPersDate();
    }

    ///========================================================
    ///---PRIVATE
    ///========================================================
    private String getPersDate() {
        int m = persCal.get(Calendar.MONTH) + 1;
        return (persCal.get(Calendar.YEAR) < 10 ? "0" + persCal.get(Calendar.YEAR) : persCal.get(Calendar.YEAR)) + "/"
            + (m < 10 ? "0" + m : m) + "/"
            + (persCal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + persCal.get(Calendar.DAY_OF_MONTH) : persCal.get(Calendar.DAY_OF_MONTH));
    }

    public long getMillisecond(String shortDateTime) {
        try {
            return JalaliDateTimeToGregorian(shortDateTime).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public Date JalaliDateTimeToGregorian(String shortDateTime) {
        if (shortDateTime == null || shortDateTime.isEmpty()) {
            return null;
        }

        String[] ss = shortDateTime.split(" ");
        if (ss.length != 2) {
            return null;
        }
        String[] tm = ss[1].split(":");

        int h, m, s, year, day, month;
        try {
            h = Integer.parseInt(tm[0]);
        } catch (Exception e) {
            h = 0;
        }
        try {
            m = Integer.parseInt(tm[1]);
        } catch (Exception e) {
            m = 0;
        }
        try {
            s = Integer.parseInt(tm[2]);
        } catch (Exception e) {
            s = 0;
        }
//
        tm = ss[0].split("/");
        try {
            year = Integer.parseInt(tm[0]);
        } catch (Exception e) {
            year = 0;
        }
        try {
            month = Integer.parseInt(tm[1]);
        } catch (Exception e) {
            month = 0;
        }
        try {
            day = Integer.parseInt(tm[2]);
        } catch (Exception e) {
            day = 0;
        }
//
        persCal.set(year, month - 1, day, h, m, s);
//
        return persCal.getTime();
    }

    public String getShortDateTime() {
        persCal.setTime(new Date());
        return getPersDate() + " " + getPersTime();
    }

    private String getPersTime(Calendar time) {
        return (time.get(Calendar.HOUR) < 10 ? "0" + time.get(Calendar.HOUR) : time.get(Calendar.HOUR))
            + ":" + (time.get(Calendar.MINUTE) < 10 ? "0" + time.get(Calendar.MINUTE) : time.get(Calendar.MINUTE))
            + ":" + (time.get(Calendar.SECOND) < 10 ? "0" + time.get(Calendar.SECOND) : time.get(Calendar.SECOND));
    }

    private String getPersTime() {
        return (LocalTime.now().getHour() < 10 ? "0" + LocalTime.now().getHour() : LocalTime.now().getHour())
            + ":" + (LocalTime.now().getMinute() < 10 ? "0" + LocalTime.now().getMinute() : LocalTime.now().getMinute())
            + ":" + (LocalTime.now().getSecond() < 10 ? "0" + LocalTime.now().getSecond() : LocalTime.now().getSecond());
    }

    public String getShortDateTime(TtCalendarItem calendarItem, int afterNow) {
        persCal.setTime(new Date());
        return getPersDate(calendarItem, afterNow) + " " + getPersTime();
    }

    /**
     * return some days after current date
     * if 'dayAfterNow' is negative, return some days befor current date
     *
     * @param calendarItem
     * @param afterNow
     * @return
     */
    private String getPersDate(TtCalendarItem calendarItem, int afterNow) {
        switch (calendarItem) {
            case Day:
                persCal.set(Calendar.DAY_OF_MONTH, persCal.get(Calendar.DAY_OF_MONTH) + afterNow);
                break;
            case Month:
                persCal.set(Calendar.MONTH, persCal.get(Calendar.MONTH) + afterNow);
                break;
            case Year:
                persCal.set(Calendar.YEAR, persCal.get(Calendar.YEAR) + afterNow);
                break;

        }
        int m = persCal.get(Calendar.MONTH) + 1;
        return (persCal.get(Calendar.YEAR) < 10 ? "0" + persCal.get(Calendar.YEAR) : persCal.get(Calendar.YEAR)) + "/"
            + (m < 10 ? "0" + m : m) + "/"
            + (persCal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + persCal.get(Calendar.DAY_OF_MONTH) : persCal.get(Calendar.DAY_OF_MONTH));
    }

    public String getShortDateTime(TtCalendarItem calendarItem, int after, String shortDateTime) {
        persCal.setTime(new Date());
        try {
            return getPersDate(calendarItem, after, shortDateTime) + " " + shortDateTime.split(" ")[1];
        } catch (Exception e) {
            return getPersDate(calendarItem, after, shortDateTime) + " " + getPersTime();
        }
    }

    private String getPersDate(TtCalendarItem calendarItem, int after, String shortDate) {
        if (shortDate == null) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            //
            return "";
        }

        switch (calendarItem) {
            case Day:
                persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
                persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
                persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]) + after);
                break;
            case Month:
                persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
                persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1 + after);
                persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));
                break;
            case Year:
                persCal.set(Calendar.YEAR, Integer.parseInt(s[0]) + after);
                persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
                persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));
                break;
            case Hour:
            case Minute:
            case Second:
                return shortDate;

        }
        int m = persCal.get(Calendar.MONTH) + 1;
        return (persCal.get(Calendar.YEAR) < 10 ? "0" + persCal.get(Calendar.YEAR) : persCal.get(Calendar.YEAR)) + "/"
            + (m < 10 ? "0" + m : m) + "/"
            + (persCal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + persCal.get(Calendar.DAY_OF_MONTH) : persCal.get(Calendar.DAY_OF_MONTH));
    }

    public String getShortTime() {
        return getPersTime();
    }

    public String getShortDate(TtCalendarItem calendarItem, int afterNow) {
        persCal.setTime(new Date());
        return getPersDate(calendarItem, afterNow);
    }

    public String getShortDate(TtCalendarItem calendarItem, int after, String shortDate) {
        return getPersDate(calendarItem, after, shortDate);
    }

    public String getDateInMonthString() {
        persCal.setTime(new Date());
        return getPersDay()
            + " " + getMonthName(persCal.get(Calendar.MONTH) + 1)
            + " " + getPersYearI();
    }

    private String getPersDay() {
        return "" + (persCal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + persCal.get(Calendar.DAY_OF_MONTH) : persCal.get(Calendar.DAY_OF_MONTH));
    }

    private String getMonthName(int monthNumber) {

        switch (monthNumber) {
            case 1:
                return "فروردین";
            case 2:
                return "اردیبهشت";
            case 3:
                return "خرداد";
            case 4:
                return "تیر";
            case 5:
                return "مرداد";
            case 6:
                return "شهریور";
            case 7:
                return "مهر";
            case 8:
                return "آبان";
            case 9:
                return "آذر";
            case 10:
                return "دی";
            case 11:
                return "بهمن";
            case 12:
                return "اسفند";
        }
        return "";
    }

    private int getPersYearI() {
        return persCal.get(Calendar.YEAR);
    }

    private int getPersYearI(int decimalCount) {
        return persCal.get(Calendar.YEAR) % ((int) Math.pow(10, decimalCount));
    }

    public String getDateTimeInMonthString(String shortDateTime) {
        if (shortDateTime == null || shortDateTime.isEmpty()) {
            return "";
        }
        String[] dt = shortDateTime.split(" ");
        String[] s = dt[0].split("/");
        if (s.length != 3) {
            return "";
        }

        return s[2]
            + " " + getMonthName(s[1])
            + " " + s[0]
            + ((dt.length > 1) ? " " + dt[1] : "");
    }

    private String getMonthName(String monthNumber) {
        int mn = 0;
        try {
            mn = Integer.parseInt(monthNumber);
        } catch (Exception e) {
        }
        return getMonthName(mn);
    }

    /**
     * Returning date by title of month
     *
     * @param shortDate
     * @return for example: "9 خرداد 1399"
     */
    public String getDateInMonthString(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] dt = shortDate.split(" ");
        String[] s = dt[0].split("/");
        if (s.length != 3) {
            return "";
        }

        return s[2]
            + " " + getMonthName(s[1])
            + " " + s[0];
    }

    /**
     * Returning Date with title of day of week in first and month title after day of month.
     *
     * @param shortDate
     * @return "شنبه 9 خرداد 1366"
     */
    public String getDateWithMonthTitleAndDayOfWeekTitle(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] dt = shortDate.split(" ");
        String[] s = dt[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));

        return getDayOfWeekTitle() + " " + Integer.parseInt(s[2])
            + " " + getMonthName(s[1])
            + " " + s[0];
    }

    /**
     * Returning Date with title of day of week in first and month title after day of month.
     *
     * @param shortDateOrDateTime
     * @return "شنبه 9 خرداد 1366 ساعت 11:11:11"
     */
    public String getDateTimeWithMonthTitleAndDayOfWeekTitle(String shortDateOrDateTime) {
        if (shortDateOrDateTime == null || shortDateOrDateTime.isEmpty()) {
            return "";
        }
        String[] dt = shortDateOrDateTime.split(" ");
        String[] s = dt[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));

        return getDayOfWeekTitle() + " " + Integer.parseInt(s[2])
            + " " + getMonthName(s[1])
            + " " + s[0]
            + (dt.length > 1 ? " ساعت " + dt[1] : "");
    }

    public String getDateTimeWithMonthTitleAndDayOfWeekTitle() {
        persCal.setTime(new Date());
        return getDateTimeWithMonthTitleAndDayOfWeekTitle(getPersDate() + " " + getPersTime());
    }

    public String getDateWithMonthTitleAndDayOfWeekTitle() {
        persCal.setTime(new Date());
        return getDateTimeWithMonthTitleAndDayOfWeekTitle(getPersDate());
    }

    public String getDateFromDateTime(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] dt = shortDate.split(" ");
        return dt[0];
    }

    ///========================================================
    ///---PUBLIC WEEK AND MONTH BORDER
    ///========================================================
    public String getStartWeekDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, persCal.get(Calendar.DAY_OF_MONTH) - persCal.get(Calendar.DAY_OF_WEEK));
        return getPersDate();
    }

    public String getStartWeekDate(int weekAfterThisWeek) {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, persCal.get(Calendar.DAY_OF_MONTH) - persCal.get(Calendar.DAY_OF_WEEK) + 7 * weekAfterThisWeek);
        return getPersDate();
    }

    public String getStartWeekDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]) - persCal.get(Calendar.DAY_OF_WEEK));
        return getPersDate();
    }

    public String getEndWeekDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, persCal.get(Calendar.DAY_OF_MONTH) - persCal.get(Calendar.DAY_OF_WEEK) + 6);
        return getPersDate();
    }

    public String getEndWeekDate(int weekAfterThisWeek) {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, persCal.get(Calendar.DAY_OF_MONTH) - persCal.get(Calendar.DAY_OF_WEEK) + 6 + 7 * weekAfterThisWeek);
        return getPersDate();
    }

    public String getEndWeekDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]) - persCal.get(Calendar.DAY_OF_WEEK) + 6);
        return getPersDate();
    }

    public String getStartMonthDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        return getPersDate();
    }

    public String getStartYearDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.MONTH, 0);
        return getPersDate();
    }

    public String getStartYearDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.MONTH, 0);
        return getPersDate();
    }

    public String getStart6MonthDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.MONTH, persCal.get(Calendar.MONTH) < 6 ? 0 : 6);
        return getPersDate();
    }

    public String getStart6MonthDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.MONTH, persCal.get(Calendar.MONTH) < 6 ? 0 : 6);
        return getPersDate();
    }

    public String getStartSessionDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.MONTH,
            persCal.get(Calendar.MONTH) < 3 ? 0 :
                (persCal.get(Calendar.MONTH) < 6 ? 3 :
                    (persCal.get(Calendar.MONTH) < 9 ? 6 :
                        9)));
        return getPersDate();
    }

    public String getStartSessionDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.MONTH,
            persCal.get(Calendar.MONTH) < 3 ? 0 :
                (persCal.get(Calendar.MONTH) < 6 ? 3 :
                    (persCal.get(Calendar.MONTH) < 9 ? 6 :
                        9)));
        return getPersDate();
    }

    public String getStartMonthDate(int monthAfterThisMonth) {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.MONTH, persCal.get(Calendar.MONTH) + monthAfterThisMonth);
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        return getPersDate();
    }

    public String getStartMonthDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, 1);
        return getPersDate();
    }


    public String getEndMonthDate() {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.DAY_OF_MONTH, getMonthLength(Calendar.MONTH, isLeapYear(Calendar.YEAR)) - 1);
        return getPersDate();
    }

    private int getMonthLength(int month, boolean isleapyear) {
        if (month < 1 || month > 12) {
            return -1;
        }
        if (month >= 1 && month <= 6) {
            return 31;
        } else if (month >= 7 && month <= 11) {
            return 30;
        } else {
            if (isleapyear) {
                return 30;
            } else {
                return 29;
            }
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public String getEndMonthDate(int monthAfterThisMonth) {
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.MONTH, persCal.get(Calendar.MONTH) + monthAfterThisMonth);
        persCal.set(Calendar.DAY_OF_MONTH, getMonthLength(Calendar.MONTH, isLeapYear(Calendar.YEAR)) - 1);
        return getPersDate();
    }

    public String getEndMonthDate(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return "";
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return "";
        }
        persCal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
        gregCal = Calendar.getInstance();
        persCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
        persCal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        persCal.set(Calendar.DAY_OF_MONTH, getMonthLength(Calendar.MONTH, isLeapYear(Calendar.YEAR)) - 1);
        return getPersDate();
    }

    ///========================================================
    ///---PUBLIC ELEMENT DATES
    ///========================================================
    public String getShortDateTime(long timeInMillisecond) {
        persCal.setTimeInMillis(timeInMillisecond);
        return getPersDate() + " " + getPersTime(persCal);
    }

    public String getYear() {
        persCal.setTime(new Date());
        return "" + getPersYearI();
    }

    public String getYear(int decimalCount) {
        persCal.setTime(new Date());
        StringBuilder persYear = new StringBuilder("" + getPersYearI(decimalCount));

        for (int i = persYear.length(); i < decimalCount; i++) {
            persYear.insert(0, "0");
        }

        return persYear.toString();
    }

    public int getYearI(String dateOrdateTime) {
        return Integer.parseInt(getYear(dateOrdateTime));
    }

    public String getYear(String dateOrdateTime) {
        if (dateOrdateTime != null && !dateOrdateTime.isEmpty()) {
            return dateOrdateTime.split("/")[0];
        }
        return "0";
    }

    public String getMonth() {
        persCal.setTime(new Date());
        return getPersMonth();

    }

    private String getPersMonth() {
        int m = persCal.get(Calendar.MONTH) + 1;
        return "" + (m < 10 ? "0" + m : m);
    }

    public int getMonthI() {
        persCal.setTime(new Date());
        return persCal.get(Calendar.MONTH) + 1;
    }

    public int getMonthI(String shortDate) {
        if (shortDate == null || shortDate.isEmpty()) {
            return -111;
        }
        String[] s = shortDate.split(" ")[0].split("/");
        if (s.length != 3) {
            return -111;
        }
        return Integer.parseInt(s[1]);
    }

    public String getDay() {
        persCal.setTime(new Date());
        return getPersDay();
    }

    public int getDayI() {
        persCal.setTime(new Date());
        return persCal.get(Calendar.DAY_OF_MONTH);
    }

    public String getHours() {
        return "" + (LocalTime.now().getHour() < 10 ? "0" + LocalTime.now().getHour() : LocalTime.now().getHour());
    }

    public int getHoursI() {
        return LocalTime.now().getHour();
    }

    public String getMinutes() {
        return "" + (LocalTime.now().getMinute() < 10 ? "0" + LocalTime.now().getMinute() : LocalTime.now().getMinute());
    }

    public int getMinutesI() {
        return LocalTime.now().getMinute();
    }

    public String getSeconds() {
        return "" + (LocalTime.now().getSecond() < 10 ? "0" + LocalTime.now().getSecond() : LocalTime.now().getSecond());
    }

    public int getSecondsI() {
        return LocalTime.now().getSecond();
    }

    public TtCompareResult compareDateTime(String firstDateTime, String secondDateTime) {
        if (firstDateTime == null || secondDateTime == null) {
            return TtCompareResult.Unknown;
        }
        firstDateTime = firstDateTime.trim();
        secondDateTime = secondDateTime.trim();

        if (firstDateTime.equals(secondDateTime)) {
            return TtCompareResult.Equal;
        }
        String fDate, fTime, sDate, sTime;

        String[] fSplit = firstDateTime.split(" ");
        String[] sSplit = secondDateTime.split(" ");
        try {
            fDate = fSplit[0];
            sDate = sSplit[0];
            TtCompareResult tcr = compareDate(fDate, sDate);
            if (tcr != TtCompareResult.Equal) {
                return tcr;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        try {
            fTime = fSplit[1];
            sTime = sSplit[1];
            return compareTime(fTime, sTime);

        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }

    }

    public TtCompareResult compareDateTimeWithNow(String dateTime) {
        TtCompareResult ttCompareResult = compareDateTime(dateTime, getShortDateTime());
        switch (ttCompareResult) {
            case SecondIsBigger:
                return TtCompareResult.LessThanNow;
            case FirstIsBigger:
                return TtCompareResult.BiggerThanNow;
            default:
                return ttCompareResult;
        }
    }

    ///========================================================
    ///---PUBLIC COPMPARE
    ///========================================================
    public TtCompareResult compareDate(String firstDate, String secondDate) {
        if (firstDate == null || firstDate.isEmpty()) {
            return TtCompareResult.Unknown;
        }
        firstDate = firstDate.split(" ")[0];
        secondDate = secondDate.split(" ")[0];

        if (firstDate.equals(secondDate)) {
            return TtCompareResult.Equal;
        }
        int startYear, startDay, startMonth,
            endDay, endMonth, endYear;

        String[] fDateSplit = firstDate.split("/");
        String[] eDateSplit = secondDate.split("/");
        try {
            startYear = Integer.parseInt(fDateSplit[0]);
            endYear = Integer.parseInt(eDateSplit[0]);
            if (startYear > endYear) {
                return TtCompareResult.FirstIsBigger;
            } else if (startYear < endYear) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        try {
            startMonth = Integer.parseInt(fDateSplit[1]);
            endMonth = Integer.parseInt(eDateSplit[1]);
            if (startMonth > endMonth) {
                return TtCompareResult.FirstIsBigger;
            } else if (startMonth < endMonth) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        try {
            startDay = Integer.parseInt(fDateSplit[2]);
            endDay = Integer.parseInt(eDateSplit[2]);
            if (startDay > endDay) {
                return TtCompareResult.FirstIsBigger;
            } else if (startDay < endDay) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        return TtCompareResult.Equal;
    }

    public TtCompareResult compareDateWithNow(String date) {
        TtCompareResult ttCompareResult = compareDate(date, getShortDate());
        switch (ttCompareResult) {
            case SecondIsBigger:
                return TtCompareResult.LessThanNow;
            case FirstIsBigger:
                return TtCompareResult.BiggerThanNow;
            default:
                return ttCompareResult;
        }
    }

    public TtCompareResult compareTime(String firstTime, String secondTime) {
        if (firstTime.equals(secondTime)) {
            return TtCompareResult.Equal;
        }
        int fHour, fSec, fMin,
            eSec, eMin, eHour;

        String[] startDateSplit = firstTime.split(":");
        String[] endDateSplit = secondTime.split(":");
        try {
            fHour = Integer.parseInt(startDateSplit[0]);
            eHour = Integer.parseInt(endDateSplit[0]);
            if (fHour > eHour) {
                return TtCompareResult.FirstIsBigger;
            } else if (fHour < eHour) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        try {
            fMin = Integer.parseInt(startDateSplit[1]);
            eMin = Integer.parseInt(endDateSplit[1]);
            if (fMin > eMin) {
                return TtCompareResult.FirstIsBigger;
            } else if (fMin < eMin) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        try {
            fSec = Integer.parseInt(startDateSplit[2]);
            eSec = Integer.parseInt(endDateSplit[2]);
            if (fSec > eSec) {
                return TtCompareResult.FirstIsBigger;
            } else if (fSec < eSec) {
                return TtCompareResult.SecondIsBigger;
            }
        } catch (Exception e) {
            return TtCompareResult.Unknown;
        }
        return TtCompareResult.Equal;
    }

    public String durationOnTheDay(String startDate, String endDate) {
        int res = durationOnTheDayI(startDate, endDate, false);
        if (res < 0) {
            return "خطا(" + durationOnTheDayI(startDate, endDate, false) + " روز)";
        }
        return durationOnTheDayI(startDate, endDate, false) + " روز";

    }

    ///========================================================
    ///---PUBLIC DURATION
    ///========================================================
    // If date was less than now, returned value will  be negative(-).
    public int durationOnTheDayUntilNowIConsiderNegative(String date) {
        return durationOnTheDayI(getShortDateTime(), date, true);
    }

    // If date was less than now, returned value will not be negative(-).
    public int durationOnTheDayUntilNowI(String date) {
        return durationOnTheDayI(getShortDateTime(), date, false);
    }

    /**
     * @param startDate
     * @param endDate
     * @param considerNegative: if is true, negative duration will have a negative value, else it will turn to positive value
     * @return
     */
    public int durationOnTheDayI(String startDate, String endDate, boolean considerNegative) {

        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            return -1;
        }
        int diffDay = 0,
            startYear, startDay, startMonth,
            endDay, endMonth, endYear;
        boolean isNegative = false;

        String[] startDateSplit = startDate.split(" ")[0].split("/");
        String[] endDateSplit = endDate.split(" ")[0].split("/");

        startYear = Integer.parseInt(startDateSplit[0]);
        endYear = Integer.parseInt(endDateSplit[0]);
        try {
            if (startYear > endYear) {
                isNegative = true;
                int tmp = startYear;
                startYear = endYear;
                endYear = tmp;
                startDay = Integer.parseInt(endDateSplit[2]);
                startMonth = Integer.parseInt(endDateSplit[1]);
                endDay = Integer.parseInt(startDateSplit[2]);
                endMonth = Integer.parseInt(startDateSplit[1]);
            } else {
                startDay = Integer.parseInt(startDateSplit[2]);
                startMonth = Integer.parseInt(startDateSplit[1]);
                endDay = Integer.parseInt(endDateSplit[2]);
                endMonth = Integer.parseInt(endDateSplit[1]);

            }
        } catch (Exception e) {
            return -1;
        }

        if (startDay > 31 || startDay < 1) {
            return -1;
        }
        if (startMonth > 12 || startMonth < 1) {
            return -1;
        }

        if (endDay > 31 || endDay < 1) {
            return -1;
        }
        if (endMonth > 12 || endMonth < 1) {
            return -1;
        }

        for (int y = startYear; y < endYear; y++) {
            diffDay += isLeapYear(y) ? 366 : 365;
        }

        for (int m = 1; m < endMonth; m++) {
            diffDay += (m < 7) ? 31
                : (m < 12) ? 30
                : isLeapYear(endYear) ? 30
                : 29;
        }

        diffDay += endDay;

        for (int m = 1; m < startMonth; m++) {
            diffDay -= (m < 7) ? 31
                : (m < 12) ? 30
                : isLeapYear(startYear) ? 30
                : 29;
        }

        diffDay -= (startDay);

        if (considerNegative && isNegative) {
            return -1 * diffDay;
        } else {
            return diffDay;
        }
    }

    public String durationOnTheDay(List<String[]> dates) {
        int res = 0;
        int temp;
        for (Iterator<String[]> iterator = dates.iterator(); iterator.hasNext(); ) {
            String[] date = iterator.next();
            if (date.length != 2) {
                continue;
            }
            temp = durationOnTheDayI(date[0], date[1], false);

            if (temp < 1) {
                continue;
            }

            res += temp;
        }
        return res + " روز";
    }

    public String durationOnTheDate(String startDate, String endDate) {

        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            return null;
        }

        int[] res = durationOnTheDateI(startDate, endDate, 0);

        if (res.length == 0) {
            return "خطا ۰۰۰";
        }
        if (res.length < 2) {
            return "خطا.. " + res[0];
        }

        if (res.length != 3) {
            return "خطا ۰۹۹";
        }

        int diffDay = res[0],
            diffMonth = res[1],
            diffYear = res[2];

        String result = "";

        result += diffYear > 0 ? diffYear + " سال" : "";
        result += (diffYear > 0 && diffMonth > 0) ? " و " : "";
        result += diffMonth > 0 ? diffMonth + " ماه" : "";
        result += (diffMonth > 0 && diffDay > 0
            || diffYear > 0 && diffDay > 0) ? " و " : "";
        result += diffDay > 0 ? diffDay + " روز" : "";

        if ("".equals(result)) {
            result = "صفر";
        }
        return result;
    }

    private int[] durationOnTheDateI(String startDate, String endDate, int distanceCarry) {

        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            return null;
        }
        int startYear, startDay, startMonth,
            endDay, endMonth, endYear;

        String[] startDateSplit = startDate.split(" ")[0].split("/");
        String[] endDateSplit = endDate.split(" ")[0].split("/");

        startYear = Integer.parseInt(startDateSplit[0]);
        endYear = Integer.parseInt(endDateSplit[0]);
        try {
            if (startYear > endYear) {
                return new int[]{-1};
            } else {
                startDay = Integer.parseInt(startDateSplit[2]);
                startMonth = Integer.parseInt(startDateSplit[1]);
                endDay = Integer.parseInt(endDateSplit[2]);
                endMonth = Integer.parseInt(endDateSplit[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{-2};
        }

        if (startDay > 31 || startDay < 1) {
            return new int[]{-3};
        }
        if (startMonth > 12 || startMonth < 1) {
            return new int[]{-4};
        }

        if (endDay > 31 || endDay < 1) {
            return new int[]{-5};
        }
        if (endMonth > 12 || endMonth < 1) {
            return new int[]{-6};
        }

        boolean isLeapYear = isLeapYear(endYear);

        endDay -= distanceCarry;

        if (startDay > endDay) {
            endDay += getMonthLength(endMonth, isLeapYear);
            endMonth = (endMonth == 1) ? 12 : endMonth - 1;
            endYear = (endMonth == 12) ? endYear - 1 : endYear;
            if (startYear > endYear) {
                return new int[]{-7};
            }
        }

        if (startMonth > endMonth) {
            endMonth += 12;
            endYear--;
            if (startYear > endYear) {
                return new int[]{-8};
            }
        }

        return new int[]{endDay - startDay,
            endMonth - startMonth,
            endYear - startYear};
    }

    public String durationOnTheDateOrTime(String startDateTime, String endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return null;
        }
        int[] res = durationOnTheDateI(startDateTime, endDateTime, 0);

        if (res == null) {
            return "خطا ۰تهی";
        }
        if (res.length == 0) {
            return "خطا ۰۰۰";
        }
        if (res.length < 2) {
            return "خطا.. " + res[0];
        }

        if (res.length != 3) {
            return "خطا ۰۹۹";
        }

        int diffDay = res[0],
            diffMonth = res[1],
            diffYear = res[2];

        String result = "";

        result += diffYear > 0 ? diffYear + " سال" : "";
        result += (diffYear > 0 && diffMonth > 0) ? " و " : "";
        result += diffMonth > 0 ? diffMonth + " ماه" : "";
        result += (diffMonth > 0 && diffDay > 0
            || diffYear > 0 && diffDay > 0) ? " و " : "";
        result += diffDay > 0 ? diffDay + " روز" : "";

        if ("".equals(result) && startDateTime.contains(" ") && endDateTime.contains(" ")) {
            try {
                String stt, edt;
                stt = startDateTime.split(" ")[1];
                edt = endDateTime.split(" ")[1];

                res = durationOnTheTimeI_HM(stt, edt);

                int diffM = res[0],
                    diffH = res[1];
                result += diffH > 0 ? diffH + " ساعت" : "";
                if (diffH == 0) {
                    result += (diffH > 0 && diffM > 0) ? " و" : "";
                    result += diffM > 0 ? diffM + " دقیقه" : "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("".equals(result)) {
                result = "کمتر از یک دقیقه";
            }
        }
        if ("".equals(result)) {
            result = "امروز";
        }
        return result;
    }

    public String durationOnTheDateOrTime(String dateTime) {
        return durationOnTheDateOrTime(dateTime, getShortDateTime());
    }

    private int[] durationOnTheTimeI_HM(String startTime, String endTime) {

        int startH, startM,
            endH, endM;

        String[] startTimeSplit = startTime.split(":");
        String[] endTimeSplit = endTime.split(":");

        startH = Integer.parseInt(startTimeSplit[0]);
        endH = Integer.parseInt(endTimeSplit[0]);
        try {
            if (startH > endH) {
                return new int[]{-1};
            } else {
                startM = Integer.parseInt(startTimeSplit[1]);
                endM = Integer.parseInt(endTimeSplit[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{-2};
        }
        if (startH > 23 || startH < 0) {
            return new int[]{-3};
        }
        if (startM > 59 || startM < 0) {
            return new int[]{-4};
        }

        if (startM > endM) {
            endM += 60;
            endH--;
            if (startH > endH) {
                return new int[]{-8};
            }
        }

        return new int[]{endM - startM, endH - startH};
    }

    public String durationOnTheDate(List<String[]> dates) {

        int diffDay = 0,
            diffMonth = 0,
            diffYear = 0;

        for (Iterator<String[]> iterator = dates.iterator(); iterator.hasNext(); ) {
            String[] date = iterator.next();
            if (date.length != 2) {
                continue;
            }
            int[] res = durationOnTheDateI(date[0], date[1], 0);

            if (res.length != 3) {
                continue;
            }

            diffDay += res[0];
            diffMonth += res[1];
            diffYear += res[2];
        }

        if (diffDay > 29) {
            int kh = diffDay / 30;
            diffDay = diffDay % 30;
            diffMonth += kh;
        }

        if (diffMonth > 12) {
            int kh1 = diffMonth / 12;
            diffMonth = diffMonth % 12;
            diffYear += kh1;
        }

        String result = "";

        result += diffYear > 0 ? diffYear + " سال" : "";
        result += (diffYear > 0 && diffMonth > 0) ? " و " : "";
        result += diffMonth > 0 ? diffMonth + " ماه" : "";
        result += (diffMonth > 0 && diffDay > 0
            || diffYear > 0 && diffDay > 0) ? " و " : "";
        result += diffDay > 0 ? diffDay + " روز" : "";

        if ("".equals(result)) {
            result = "صفر";
        }
        return result;

    }

    /**
     * ============================
     * Public Duration On Month
     * ==============================
     **/
    public int durationOnTheMonthI(String startDate, String endDate) {
        int i = durationOnTheDayI(startDate, endDate, false);
        return i > 0 ? i / 30 : i;
    }

    public int durationOnTheMonthI(String date) {
        int i = durationOnTheDayI(getShortDate(), date, false);
        return i > 0 ? i / 30 : i;
    }

    ///========================================================
    ///---PUBLIC DISTANCE
    ///========================================================
    //     * در این توابع، بازه زمانی وابسته به تغییر روز نیست
    //     * میزان فاصله بر اساس ساعت محاسبه می شود
    //     * اگر روز هم عوض شود، بازه زمانی تا اختلاف 24 ساعت در نظر گرفته می شود.
    private int[] distanceOnTheTimeI_HM(String startTime, String endTime) {

        int startH, startM,
            endH, endM;
        int distanceCarry = 0;

        String[] startTimeSplit = startTime.split(":");
        String[] endTimeSplit = endTime.split(":");

        startH = Integer.parseInt(startTimeSplit[0]);
        endH = Integer.parseInt(endTimeSplit[0]);

        try {
            if (startH > endH) {
                distanceCarry = 1;
            }
            startM = Integer.parseInt(startTimeSplit[1]);
            endM = Integer.parseInt(endTimeSplit[1]);

        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{-2};
        }
        if (startH > 23 || startH < 0) {
            return new int[]{-3};
        }
        if (startM > 59 || startM < 0) {
            return new int[]{-4};
        }

        if (startM > endM) {
            endM += 60;
            endH--;
            if (startH > endH) {
                distanceCarry = 1;
            }
        }

        return new int[]{endM - startM, (distanceCarry * 24) + endH - startH, distanceCarry};
    }

    public String distanceOnTheDateOrTime(String startDateTime, String endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return null;
        }
        int[] res;
        int diffM,
            diffH,
            distance;
        String result = "", resTime = "";
        String stt, edt;

        try {
            stt = startDateTime.split(" ")[1];
            edt = endDateTime.split(" ")[1];

            res = distanceOnTheTimeI_HM(stt, edt);

            diffM = res[0];
            diffH = res[1];
            distance = res[2];
            resTime += diffH > 0 ? diffH + " ساعت" : "";
            if (diffH == 0) {
                resTime += (diffH > 0 && diffM > 0) ? " و" : "";
                resTime += diffM > 0 ? diffM + " دقیقه" : "";
            }
        } catch (Exception e) {
            return "خطا 001";
        }

        res = durationOnTheDateI(startDateTime, endDateTime, distance);

        if (res == null) {
            return "خطا ۰تهی";
        }
        if (res.length == 0) {
            return "خطا ۰۰۰";
        }
        if (res.length < 2) {
            return "خطا.. " + res[0];
        }

        if (res.length != 3) {
            return "خطا ۰۹۹";
        }

        int diffDay = res[0],
            diffMonth = res[1],
            diffYear = res[2];


        result += diffYear > 0 ? diffYear + " سال" : "";
        result += (diffYear > 0 && diffMonth > 0) ? " و " : "";
        result += diffMonth > 0 ? diffMonth + " ماه" : "";
        result += (diffMonth > 0 && diffDay > 0
            || diffYear > 0 && diffDay > 0) ? " و " : "";
        result += diffDay > 0 ? diffDay + " روز" : "";

        if ("".equals(result)) {
            result = resTime;
        }
        if ("".equals(result)) {
            result = "کمتر از یک دقیقه";
        }
        return result;
    }

    ///========================================================
    ///---PUBLIC ARRAY
    ///========================================================
    public int[] getGregorianYearArrayInRangeI(Integer fromYear, Integer toYear, TtSort sort) {
        if (fromYear != null) {
            int[] s;
            if (toYear == null) {
                toYear = Calendar.getInstance().get(Calendar.YEAR);
            }

            toYear -= fromYear;

            s = new int[toYear + 1];
            if (sort == TtSort.AECS) {
                for (int i = 0; i <= toYear; i++) {
                    //
                    s[i] = fromYear + i;
                }
            } else {
                for (int i = toYear; i >= 0; i--) {
                    //
                    s[toYear - i] = fromYear + i;
                }
            }
            return s;
        }
        //
        return null;
    }

    public int[] getGregorianYearArrayInRangeI(String fromDate, String toDate) {
        return getGregorianYearArrayInRangeI(fromDate, toDate, TtSort.DESC);
    }

    public int[] getGregorianYearArrayInRangeI(String fromDate, String toDate, TtSort sort) {
        if (fromDate != null && !fromDate.isEmpty()) {
            int syi, eyi;
            int[] s;
            String y = fromDate.split("/")[0];
            syi = Integer.parseInt(y);
            if (toDate != null && !toDate.isEmpty()) {
                y = toDate.split("/")[0];
                eyi = Integer.parseInt(y);
                //
            } else {
                eyi = Calendar.getInstance().get(Calendar.YEAR);
            }

            eyi -= syi;

            s = new int[eyi + 1];
            if (sort == TtSort.AECS) {
                for (int i = 0; i <= eyi; i++) {
                    //
                    s[i] = syi + i;
                }
            } else {
                for (int i = eyi; i >= 0; i--) {
                    //
                    s[eyi - i] = syi + i;
                }
            }
            return s;
        }
        //
        return null;
    }

    public int[] getYearArrayInRangeI(Integer fromYear, Integer toYear, TtSort sort) {
        if (fromYear != null) {
            int[] s;
            if (toYear == null) {
                toYear = ParsCalendar.getInstance().getYearI();
            }
            //
            toYear -= fromYear;
            //
            s = new int[toYear + 1];
            if (sort == TtSort.AECS) {
                for (int i = 0; i <= toYear; i++) {
                    //
                    s[i] = fromYear + i;
                }
            } else {
                for (int i = toYear; i >= 0; i--) {
                    //
                    s[toYear - i] = fromYear + i;
                }
            }
            return s;
        }
        //
        return null;
    }

    public int getYearI() {
        persCal.setTime(new Date());
        return persCal.get(Calendar.YEAR);
    }

    public int getYearI(int decimalCount) {
        persCal.setTime(new Date());
        return getPersYearI(decimalCount);
    }

    public static ParsCalendar getInstance() {
        return instance;
    }

    public int[] getYearArrayInRangeI(String fromDate, String toDate) {
        return getYearArrayInRangeI(fromDate, toDate, TtSort.DESC);
    }

    public int[] getYearArrayInRangeI(String fromDate, String toDate, TtSort sort) {
        if (fromDate != null && !fromDate.isEmpty()) {
            int syi, eyi;
            int[] s;
            String y = fromDate.split("/")[0];
            syi = Integer.parseInt(y);
            if (toDate != null && !toDate.isEmpty()) {
                y = toDate.split("/")[0];
                eyi = Integer.parseInt(y);
                //
            } else {
                eyi = ParsCalendar.getInstance().getYearI();
            }
            //
            eyi -= syi;
            //
            s = new int[eyi + 1];
            if (sort == TtSort.AECS) {
                for (int i = 0; i <= eyi; i++) {
                    //
                    s[i] = syi + i;
                }
            } else {
                for (int i = eyi; i >= 0; i--) {
                    //
                    s[eyi - i] = syi + i;
                }
            }
            return s;
        }
        //
        return null;
    }

    public String[] getShortDateArrayInRangeI(String fromDate, String toDate) {
        if (fromDate != null && !fromDate.isEmpty()) {
            //
            if (toDate == null || toDate.isEmpty()) {
                toDate = getShortDate();
            }
            //

            int i = durationOnTheDayI(fromDate, toDate, false);

            String[] ds = new String[i];

            for (int j = 0; j < i; j++) {
                ds[j] = getPersDate(TtCalendarItem.Day, 1, fromDate);
            }

            return ds;
        }
        //
        return null;
    }

    public String getShortDate() {
        persCal.setTime(new Date());
        return getPersDate();
    }

    ///========================================================
    ///---PUBLIC CONVERTOR
    ///========================================================
    public String toTimeString(long startTime) {
        int d = (int) ((new Date().getTime() - startTime) / 1000);
        int ds = d % 60;
        int dm = d / 60;
        return ((dm == 0) ? "" : dm + " دقیقه")
            + ((dm == 0 || ds == 0) ? "" : " و ") + (ds == 0 ? "" : ds + " ثانیه");
    }

    public String toRemindingTimeString(long startTime, int ceilingSeconds) {
        int diffSecond = (int) ((startTime + (ceilingSeconds * 1000) - new Date().getTime()) / 1000);
        int remindSec = (diffSecond % 60);
        int remindMin = diffSecond / 60;
        return ((remindMin == 0) ? "" : remindMin + " دقیقه")
            + ((remindMin == 0 || remindSec == 60) ? "" : " و ") + (remindSec == 60 ? "" : remindSec + " ثانیه");
    }

    ///========================================================
    ///---PUBLIC CONVERTOR
    ///========================================================
    public List<String> splitTime(String startTime, String endTime, int minuteSpliter) {
        List<String> sps = null;
        while (TtCompareResult.SecondIsBigger == compareTime(startTime, endTime)) {
            startTime = getPersTime(TtCalendarItem.Minute, minuteSpliter, startTime)[1];
            sps.add(startTime);
        }
        if (TtCompareResult.Equal != compareTime(startTime, endTime)) {
            sps.remove(sps.size() - 1);
        }
        return sps;
    }

    private String[] getPersTime(TtCalendarItem calendarItem, int after, String shortTime) {

        String[] tm = shortTime.split(":");
        if (tm.length != 3) {

            return null;
        }

        int h = 0, m = 0, s = 0, d = 0;
        switch (calendarItem) {
            case Hour:
                h = Integer.parseInt(tm[0]);
                h += after;
                if (h > 23) {
                    return new String[]{(h / 24) + "", getPersTime(h % 24, Integer.parseInt(tm[1]), Integer.parseInt(tm[2]))};
                }
                return new String[]{"0", getPersTime(h, Integer.parseInt(tm[1]), Integer.parseInt(tm[2]))};

            case Minute:
                h = Integer.parseInt(tm[0]);
                m = Integer.parseInt(tm[1]);
                m += after;
                if (m > 59) {
                    h += (m / 60);
                    if (h > 23) {
                        d = h / 24;
                        h = h % 24;
                    }
                    m = m % 60;
                }
                return new String[]{d + "", getPersTime(h, m, Integer.parseInt(tm[2]))};
            case Second:
                h = Integer.parseInt(tm[0]);
                m = Integer.parseInt(tm[1]);
                s = Integer.parseInt(tm[2]);
                s += after;
                if (s > 59) {
                    m += s / 60;
                    if (m > 59) {
                        h += (m / 60);
                        if (h > 23) {
                            d = h / 24;
                            h = h % 24;
                        }
                        m = m % 60;
                    }
                    s = s % 60;
                }
                return new String[]{d + "", getPersTime(h, m, s)};
        }

        return new String[]{"0", shortTime};
    }

    private String getPersTime(int hour, int min, int sec) {
        return (hour < 10 ? "0" + hour : hour)
            + ":" + (min < 10 ? "0" + min : min)
            + ":" + (sec < 10 ? "0" + sec : sec);
    }
}
