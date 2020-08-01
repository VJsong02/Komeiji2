package org.komeiji.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

import static org.komeiji.main.Main.prefix;

public class LogsListener extends ListenerAdapter {
    public static HashMap<Long, Message> messages = new HashMap<>();
    public static HashMap<Long, Long> guilds = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getAuthor().getIdLong() == e.getGuild().getOwnerIdLong()) {
                if (e.getMessage().getContentDisplay().startsWith(prefix + "log"))
                    guilds.put(e.getGuild().getIdLong(), e.getChannel().getIdLong());
                else if (e.getMessage().getContentDisplay().startsWith(prefix + "unlog"))
                    guilds.remove(e.getGuild().getIdLong());

                messages.put(e.getMessageIdLong(), e.getMessage());
            }
        }
    }
}
