package me.bo0tzz.remindmebot.api;

import com.joestelmach.natty.DateGroup;
import me.bo0tzz.remindmebot.RemindMeBot;
import me.bo0tzz.remindmebot.reminder.Reminder;
import me.bo0tzz.remindmebot.util.TimeParser;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by boet on 30-3-2016.
 */
public class RemindMeBotListener implements Listener {
    private final Map<String, Consumer<CommandMessageReceivedEvent>> commandMap;
    private final RemindMeBot instance;

    public RemindMeBotListener() {
        commandMap = new HashMap<String, Consumer<CommandMessageReceivedEvent>>(){{
            RemindMeBotListener that = RemindMeBotListener.this;
            put("about", that::about);
            put("help", that::help);
            put("remindme", that::remindMe);
        }};

        this.instance = RemindMeBot.getInstance();
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
     //Do inline shit
    }

    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        commandMap.getOrDefault(event.getCommand(), (e) -> {}).accept(event);
    }

    private void about(CommandMessageReceivedEvent event) {
        event.getChat().sendMessage(SendableTextMessage.builder()
                        .message("This bot was created by @bo0tzz using the @JavaTelegramBotAPI. The source is available on [GitHub](https://github.com/bo0tzz/RemindMeBot)")
                        .parseMode(ParseMode.MARKDOWN)
                        .build()
                );
    }

    private void help(CommandMessageReceivedEvent event) {
        event.getChat().sendMessage(SendableTextMessage.builder()
                        .message("This bot allows you to set reminders. The bot will send you a message on the time you specified, with your reminder. You can specify everything in a natural sentence, like so:\n" +
                                "/remindme on the 2nd of april to call mom\n" +
                                "/remindme in three hours that I need to start making dinner\n" +
                                "\n" +
                                "Make sure to separate the time and the reminder with either \"that\" or \"to\", to make sure the bot can understand which is which. For more information, type /about.")
                        .parseMode(ParseMode.MARKDOWN)
                        .build()
                );
    }

    private void remindMe(CommandMessageReceivedEvent event) {
        if (event.getArgsString().equals("")) {
            event.getChat().sendMessage("Give me ");
            return;
        }
        String[] args = event.getArgsString().replace(" to ", " that ").split("that", 2);

        if (args.length != 2) {
            event.getChat().sendMessage("Something went wrong while processing your reminder! Please try again");
            return;
        }

        DateGroup date = TimeParser.parse(args[0]);
        if (date == null) {
            event.getChat().sendMessage("It seems that the time you entered doesn't make sense. Please try again!");
            return;
        }

        Reminder reminder = new Reminder(
                date.getDates().get(0).getTime(),
                event.getChat().getId(),
                args[1],
                event.getMessage().getSender().getUsername());
        instance.getReminderManager().addReminder(reminder);

        event.getChat().sendMessage(SendableTextMessage.builder()
            .message(String.format("*New reminder added!* \n*Reminded at:* _%s_ \n*Reminder:* _%s_",
                    TimeParser.asString(reminder.getUnixTime()),
                    reminder.getReminder()))
            .replyTo(event.getMessage())
            .parseMode(ParseMode.MARKDOWN)
            .build());
    }
}
