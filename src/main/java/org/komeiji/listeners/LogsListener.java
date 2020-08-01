package org.komeiji.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.main.DataSource;
import org.komeiji.main.Main;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.main.Main.safe;

abstract class CachedMessage implements Message {

}

public class LogsListener extends ListenerAdapter {
    public static HashMap<Long, Message> messages = new HashMap<>();
    public static Queue<Long> messageids = new LinkedList<>();
    public static HashMap<Long, Long> guilds = new HashMap<>();

    public boolean log(long guild, long channel) {
        guilds.put(guild, channel);
        String query = "INSERT INTO logs (guild, channel) VALUES (?, ?)";

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setLong(1, guild);
            p.setLong(2, channel);
            p.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean unlog(long guild) {
        guilds.remove(guild);
        String query = "DELETE FROM logs WHERE guild = ?";

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setLong(1, guild);
            p.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getMember().hasPermission(Permission.MANAGE_SERVER) || e.getAuthor().getIdLong() == safe.OWNERID) {
                if (e.getMessage().getContentDisplay().startsWith(prefix + "log")) {
                    if (log(e.getGuild().getIdLong(), e.getChannel().getIdLong()))
                        e.getMessage().addReaction("\u2705").queue();
                } else if (e.getMessage().getContentDisplay().startsWith(prefix + "unlog")) {
                    if (unlog(e.getGuild().getIdLong()))
                        e.getMessage().addReaction("\u2705").queue();
                }
                if (guilds.containsKey(e.getGuild().getIdLong())) {
                    messages.put(e.getMessageIdLong(), e.getMessage());
                    messageids.add(e.getMessageIdLong());

                    if (messageids.size() > 5000)
                        messages.remove(messageids.poll());
                }
            }
        }
    }

    public void onMessageUpdate(MessageUpdateEvent e) {
        if (messages.containsKey(e.getMessageIdLong())) {
            Message a = messages.get(e.getMessageIdLong()),
                    b = e.getMessage();

            if (!a.getContentRaw().equals(b.getContentRaw())) {
                EmbedBuilder suki = new EmbedBuilder().setColor(new Color(255, 255, 0));
                suki.setTitle("Message edited", e.getMessage().getJumpUrl());
                suki.addField("Old text", a.getContentDisplay(), false);
                suki.addField("New text", b.getContentDisplay(), false);
                suki.setFooter(e.getAuthor().getAsTag(), e.getAuthor().getAvatarUrl());
                Main.jda.getTextChannelById(guilds.get(e.getGuild().getIdLong())).sendMessage(suki.build()).queue();
            }
        }
    }

    public void onMessageDelete(MessageDeleteEvent e) {
        if (messages.containsKey(e.getMessageIdLong())) {
            Message deleted = messages.get(e.getMessageIdLong());

            EmbedBuilder suki = new EmbedBuilder().setColor(new Color(255, 0, 0));
            suki.setTitle("Message deleted");
            suki.addField("Text", deleted.getContentDisplay(), false);
            suki.setFooter(deleted.getAuthor().getAsTag(), deleted.getAuthor().getAvatarUrl());
            Main.jda.getTextChannelById(guilds.get(e.getGuild().getIdLong())).sendMessage(suki.build()).queue();

            messages.remove(e.getMessageIdLong());
            messageids.remove(e.getMessageIdLong());
        }
    }
}
