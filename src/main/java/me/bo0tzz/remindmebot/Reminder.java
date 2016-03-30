package me.bo0tzz.remindmebot;

import com.joestelmach.natty.DateGroup;

import java.util.Date;

/**
 * Created by boet on 30-3-2016.
 */
public class Reminder {
    private final String chatID;
    private final String reminder;
    private final long unixTime;
    private final long userID;

    public Reminder(long unixTime, String chatID, String reminder, long userID) {
        this.unixTime = unixTime/1000;
        this.chatID = chatID;
        this.reminder = reminder;
        this.userID = userID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getReminder() {
        return reminder;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public long getUserID() {
        return userID;
    }
}
