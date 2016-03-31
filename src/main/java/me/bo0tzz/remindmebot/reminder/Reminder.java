package me.bo0tzz.remindmebot.reminder;

/**
 * Created by boet on 30-3-2016.
 */
public class Reminder implements Comparable {
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
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!(o instanceof Reminder)) {
            throw new ClassCastException();
        }
        return unixTime.compareTo(((Reminder) o).getUnixTime());
    }
}
