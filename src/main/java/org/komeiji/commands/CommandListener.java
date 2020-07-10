package org.komeiji.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class CommandListener extends ListenerAdapter {
    public static HashMap<String, Runnable> commands = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            // assign commonly used variables for brevity
            Message m = e.getMessage();
            MessageChannel c = e.getChannel();
            User u = e.getAuthor();


        }
    }
}
