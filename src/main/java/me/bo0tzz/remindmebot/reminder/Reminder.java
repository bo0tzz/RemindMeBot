package me.bo0tzz.remindmebot.reminder;

/**
 * Created by boet on 30-3-2016.
 */
public class Reminder {
    private String chatID;
    private String reminder;
    private long unixTime;
    private long userID;

    public Reminder(long unixTime, String chatID, String reminder, long userID) {
        this.unixTime = unixTime;
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
