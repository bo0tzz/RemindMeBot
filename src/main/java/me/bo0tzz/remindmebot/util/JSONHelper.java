package me.bo0tzz.remindmebot.util;

import me.bo0tzz.remindmebot.reminder.Reminder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by boet on 8/12/17.
 */
public class JSONHelper {
    public static String JSONFromReminder(Reminder r) {
        String json;
        try {
            json = XContentFactory.jsonBuilder().startObject()
                    .field("date", r.getUnixTime())
                    .field("chat", r.getChatID())
                    .field("user", r.getUserName())
                    .field("reminder", r.getReminder())
                    .endObject().string();
        } catch (IOException e) {
            json = null;
        }
        return json;
    }
}
