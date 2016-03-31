package me.bo0tzz.remindmebot.util;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by boet on 30-3-2016.
 */
public class TimeParser {

    public static DateGroup parse(String value) {
        new Parser(TimeZone.getTimeZone("UTC"));
        List<DateGroup> groups = new Parser().parse(value);
        if (groups.isEmpty()) {
            return null;
        }
        return groups.get(0);
    }

    public static String asString(long unixTime) {
        Date date = new Date(unixTime);
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd-MMM-yyyy z");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}
