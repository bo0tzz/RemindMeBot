package me.bo0tzz.remindmebot.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.bo0tzz.remindmebot.RemindMeBot;
import me.bo0tzz.remindmebot.Reminder;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by boet on 30-3-2016.
 */
public class StorageHook {
    private final File file;
    private final RemindMeBot instance;

    //Map<Unix time, Reminder>
    private Map<Long, Reminder> reminderMap;

    public StorageHook() {
        this.instance = RemindMeBot.getInstance();
        File file1 = new File(".", "reminders.json");
        if (file1.exists()) {
            this.file = file1;
            String json = null;
            try {
                json = IOUtils.toString(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
                instance.debug("Something went wrong while loading the reminder data!");
                System.exit(1);
            }
            Gson gson = new Gson();
            reminderMap = gson.fromJson(json, new TypeToken<Map<Long, Reminder>>(){}.getType());
        } else {
            this.file = new File(".", "reminders.json");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                instance.debug("Error creating new reminder data file!");
                System.exit(1);
            }
            reminderMap = new HashMap<>();
            save();
        }
        //Create new save thread
    }

    public void addReminder(Reminder reminder) {
        reminderMap.put(reminder.getUnixTime(), reminder);
    }

    public void save() {
        //Save data to file
        //THREADSAFE
    }
}
