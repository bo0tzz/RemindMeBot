package me.bo0tzz.remindmebot.reminder;

import com.google.common.collect.TreeMultiset;
import me.bo0tzz.remindmebot.RemindMeBot;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.ChatType;
import pro.zackpollard.telegrambot.api.chat.GroupChat;
import pro.zackpollard.telegrambot.api.chat.IndividualChat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by boet on 31-3-2016.
 */
public class ReminderManager {
    private final RemindMeBot instance;
    private final TreeMultiset<Reminder> reminderSet;

    public ReminderManager() {
        this.instance = RemindMeBot.getInstance();
        this.reminderSet = instance.getStorageHook().getReminderSet();

        //Check for new reminders every second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (reminderSet.isEmpty()) return;
                check();
            }
        }, 1000L, 1000L);
    }

    public void addReminder(Reminder reminder) {
        reminderSet.add(reminder);
    }


    private void check() {
        Reminder reminder = reminderSet.firstEntry().getElement();
        if (!(reminder.getUnixTime() <= System.currentTimeMillis())) {
            return;
        }
        Chat chat = TelegramBot.getChat(reminder.getChatID());
        StringBuilder messageBuilder = new StringBuilder("*You have a new reminder!* \n");

        if (!(chat instanceof IndividualChat)) {
            messageBuilder.append("*Reminder set by @" + reminder.getUserID() + "* \n");
        }

        messageBuilder.append("*Reminder:* ").append(reminder.getReminder());

        SendableTextMessage message = SendableTextMessage.builder()
                .parseMode(ParseMode.MARKDOWN)
                .message(messageBuilder.toString())
                .build();

        instance.getBot().sendMessage(chat, message);
        reminderSet.remove(reminder);
    }

}
