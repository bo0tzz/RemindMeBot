package me.bo0tzz.remindmebot;

import me.bo0tzz.remindmebot.api.RemindMeBotListener;
import me.bo0tzz.remindmebot.reminder.ReminderManager;
import me.bo0tzz.remindmebot.storage.ESHook;
import me.bo0tzz.remindmebot.storage.StorageHook;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.net.UnknownHostException;

/**
 * Created by boet on 30-3-2016.
 */
public class RemindMeBot {
    private final TelegramBot bot;
    private static RemindMeBot instance;
    private final StorageHook storageHook;
    private ESHook elasticSearchHook; //not final because calling System#exit without assigning it makes compiler complain
    private final ReminderManager reminderManager;

    public static void main(String[] args) {
        new RemindMeBot(args[0]);
    }

    private RemindMeBot(String key) {
        instance = this;
        this.bot = TelegramBot.login(key);
        bot.getEventsManager().register(new RemindMeBotListener());
        storageHook = new StorageHook();

        try {
            elasticSearchHook = new ESHook();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        reminderManager = new ReminderManager();

        //Save reminder map on shutdown to ensure persistence of reminders
        Runtime.getRuntime().addShutdownHook(new Thread(elasticSearchHook::close));

        bot.startUpdates(false);


        this.debug("Bot started!");
    }

    public static RemindMeBot getInstance() {
        return instance;
    }

    public TelegramBot getBot() {
        return bot;
    }

    public StorageHook getStorageHook() {
        return storageHook;
    }

    public ReminderManager getReminderManager() {
        return reminderManager;
    }

    public void debug(String debug) {
        bot.getChat("97824825").sendMessage(SendableTextMessage.builder().message(debug).build());
    }
}
