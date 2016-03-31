package me.bo0tzz.remindmebot.storage;

import com.google.common.collect.TreeMultiset;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.bo0tzz.remindmebot.RemindMeBot;
import me.bo0tzz.remindmebot.reminder.Reminder;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by boet on 30-3-2016.
 */
public class StorageHook {
    private final File file;
    private final RemindMeBot instance;
    private boolean retry = false;

    private TreeMultiset<Reminder> reminderSet;

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
            reminderSet = gson.fromJson(json, new TypeToken<TreeMultiset<Reminder>>(){}.getType());
        } else {
            this.file = new File(".", "reminders.json");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                instance.debug("Error creating new reminder data file!");
                System.exit(1);
            }
        }

        if (reminderSet == null) {
            reminderSet = TreeMultiset.create();
        }

        //Create new timer to save reminders to file
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, 10000L, 10000L);
    }

    public TreeMultiset<Reminder> getReminderSet() {
        return reminderSet;
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
        String json = gson.toJson(reminderSet);
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
