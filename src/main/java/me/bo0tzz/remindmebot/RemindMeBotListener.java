package me.bo0tzz.remindmebot;

import com.joestelmach.natty.DateGroup;
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
            put("remindme", that::remindMe);
        }};

        this.instance = RemindMeBot.getInstance();
        instance.debug("Listener instantiated");
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
     //Do inline shit
    }

    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        instance.debug("Command received: " + event.getCommand());
        commandMap.getOrDefault(event.getCommand(), (e) -> {}).accept(event);
    }

    private void remindMe(CommandMessageReceivedEvent event) {
        instance.debug("New reminder received: " + event.getArgsString());
        String[] args = event.getArgsString().replace("that", "to").split("to", 2);
        if (args.length != 2) {
            event.getChat().sendMessage("Something went wrong while processing your reminder! Please try again", instance.getBot());
            instance.debug("args length didn't equal 2 on reminder " + event.getArgsString());
            return;
        }

        DateGroup date = TimeParser.parse(args[0]);
        if (date == null) {
            event.getChat().sendMessage("It seems that the time you entered doesn't make sense. Please try again!", instance.getBot());
            return;
        }
        instance.debug("Time entered: " + args[0]);
        instance.debug("Time parsed: " + TimeParser.asString(date.getDates().get(0).getTime()));


        Reminder reminder = new Reminder(
                date.getDates().get(0).getTime(),
                event.getChat().getId(),
                args[1],
                event.getMessage().getSender().getId());
        instance.getStorageHook().addReminder(reminder);
        instance.debug("New reminder created");

        event.getChat().sendMessage(SendableTextMessage.builder()
            .message(String.format("*New reminder added!* \n *Reminded at:* _%s_ \n *Reminder:* _%s_",
                    TimeParser.asString(reminder.getUnixTime()),
                    reminder.getReminder()))
            .replyTo(event.getMessage())
            .parseMode(ParseMode.MARKDOWN)
            .build(), instance.getBot());
    }
}
