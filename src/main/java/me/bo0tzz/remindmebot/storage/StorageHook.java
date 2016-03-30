package me.bo0tzz.remindmebot.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.bo0tzz.remindmebot.RemindMeBot;
import me.bo0tzz.remindmebot.Reminder;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by boet on 30-3-2016.
 */
public class StorageHook {
    private final File file;
    private final RemindMeBot instance;
    private boolean retry = false;

    //Map<Unix time, Reminder>
    private final Map<Long, Reminder> reminderMap;

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
            reminderMap = gson.fromJson(json, new TypeToken<ConcurrentHashMap<Long, Reminder>>(){}.getType());
        } else {
            this.file = new File(".", "reminders.json");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                instance.debug("Error creating new reminder data file!");
                System.exit(1);
            }
            reminderMap = new ConcurrentHashMap<>();
            save();
        }

        //Create new timer to save reminders to file
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, 600000L, 600000L);
    }

    public void addReminder(Reminder reminder) {
        reminderMap.put(reminder.getUnixTime(), reminder);
    }

    public void save() {
        if (!file.exists()) {
            instance.debug("Reminders file not found! Creating file.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                instance.debug("Error creating file. Exiting bot.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(reminderMap);
        try {
            FileUtils.writeStringToFile(file, json, Charsets.UTF_8);
        } catch (IOException e) {
            if (!retry) {
                instance.debug("Error saving to file. Retrying.");
                save();
            } else {
                instance.debug("Retried - failed again. Shutting down.");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
