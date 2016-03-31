package me.bo0tzz.remindmebot.reminder;

import me.bo0tzz.remindmebot.RemindMeBot;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by boet on 31-3-2016.
 */
public class ReminderManager {
    private final RemindMeBot instance;
    private final Map<Long, Reminder> reminderMap;

    public ReminderManager() {
        this.instance = RemindMeBot.getInstance();
        this.reminderMap = instance.getStorageHook().getReminderMap();

        //Check for new reminders every second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                check();
            }
        }, 1000L, 1000L);
    }

    public void addReminder(Reminder reminder) {
        reminderMap.put(reminder.getUnixTime(), reminder);
    }


    private void check() {
        Long minReminder = Collections.min(reminderMap.keySet());
        if (!(minReminder <= System.currentTimeMillis())) {
            return;
        }
        Reminder reminder = reminderMap.get(minReminder);
        Chat chat = TelegramBot.getChat(reminder.getChatID());
        StringBuilder messageBuilder = new StringBuilder("*You have a new reminder!* \n");

        /* Not supported by API
        if (!reminder.getChatID().equals(reminder.getUserID())) {
            String username = ((IndividualChat)TelegramBot.getChat(reminder.getUserID())).getPartner().getUsername();
            messageBuilder.append("Reminder set by @" + username);
        }
        */

        messageBuilder.append("_Reminder:_ " + reminder.getReminder());

        SendableTextMessage message = SendableTextMessage.builder()
                .parseMode(ParseMode.MARKDOWN)
                .message(messageBuilder.toString())
                .build();

        instance.getBot().sendMessage(chat, message);
    }

}
