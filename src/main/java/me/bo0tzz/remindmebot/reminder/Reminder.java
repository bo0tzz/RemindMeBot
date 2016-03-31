package me.bo0tzz.remindmebot.reminder;

/**
 * Created by boet on 30-3-2016.
 */
public class Reminder implements Comparable<Reminder> {
    private String chatID;
    private String reminder;
    private Long unixTime;
    private Long userID;

    public Reminder(Long unixTime, String chatID, String reminder, Long userID) {
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

    public Long getUnixTime() {
        return unixTime;
    }

    public Long getUserID() {
        return userID;
    }

    @Override
    public int compareTo(Reminder o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return unixTime.compareTo(o.getUnixTime());
    }
}
