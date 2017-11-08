package me.bo0tzz.remindmebot;

import me.bo0tzz.remindmebot.api.RemindMeBotListener;
import me.bo0tzz.remindmebot.reminder.ReminderManager;
import me.bo0tzz.remindmebot.storage.StorageHook;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

/**
 * Created by boet on 30-3-2016.
 */
public class RemindMeBot {
    private final TelegramBot bot;
    private static RemindMeBot instance;
    private final StorageHook storageHook;
    private final ReminderManager reminderManager;

    public static void main(String[] args) {
        String key = System.getenv("BOT_KEY");
        if (key == null || key.equals("")) {
            key = args[0];
        }
        new RemindMeBot(key);
    }

    private RemindMeBot(String key) {
        instance = this;
        this.bot = TelegramBot.login(key);
        bot.getEventsManager().register(new RemindMeBotListener());
        bot.startUpdates(false);
        storageHook = new StorageHook();
        reminderManager = new ReminderManager();

        //Save reminder map on shutdown to ensure persistence of reminders
        Runtime.getRuntime().addShutdownHook(new Thread(storageHook::save));

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
