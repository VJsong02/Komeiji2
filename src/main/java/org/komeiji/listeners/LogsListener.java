package org.komeiji.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.komeiji.main.DataSource;
import org.komeiji.main.Main;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.*;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.main.Main.safe;

class CachedMessage {
    private final Message message;
    private final List<File> attachments;

    public CachedMessage(Message m) {
        this.message = m;

        ArrayList<File> files = new ArrayList<>();
        for (Message.Attachment a : m.getAttachments())
            if (a.isImage())
                files.add(new File(a.getFileName(), fileToBytes(a.getUrl())));
        this.attachments = files;
    }

    public Message getMessage() {
        return message;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    private byte[] fileToBytes(String in) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             InputStream i = new URL(in).openStream()) {
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;
            while ((n = i.read(byteChunk)) > 0)
                b.write(byteChunk, 0, n);
            return b.toByteArray();
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", in, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    static class File {
        private final String filename;
        private final byte[] file;
        private final String hash;

        public File(String name, byte[] file) {
            this.filename = name;
            this.file = file;
            this.hash = bytesToHex(file);
        }

        private static String bytesToHex(byte[] hash) {
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }

        public String getFilename() {
            return filename;
        }

        public byte[] getFile() {
            return file;
        }

        public String getHash() {
            return hash;
        }
    }
}

public class LogsListener extends ListenerAdapter {
    public static HashMap<Long, CachedMessage> messages = new HashMap<>();
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
                    if (log(e.getGuild().getIdLong(), e.getChannel().getIdLong())) {
                        e.getMessage().addReaction("\u2705").queue();
                        Main.logger.info("Added " + e.getChannel() + " to logging");
                    }
                } else if (e.getMessage().getContentDisplay().startsWith(prefix + "unlog")) {
                    if (unlog(e.getGuild().getIdLong())) {
                        e.getMessage().addReaction("\u2705").queue();
                        Main.logger.info("Removed " + e.getChannel() + " to logging");
                    }
                }
                if (guilds.containsKey(e.getGuild().getIdLong())) {
                    messages.put(e.getMessageIdLong(), new CachedMessage(e.getMessage()));
                    messageids.add(e.getMessageIdLong());

                    if (messageids.size() > 5000)
                        messages.remove(messageids.poll());
                }
            }
        }
    }

    public void onMessageUpdate(MessageUpdateEvent e) {
        if (messages.containsKey(e.getMessageIdLong())) {
            CachedMessage a = messages.get(e.getMessageIdLong());
            Message b = e.getMessage();

            if (!a.getMessage().getContentRaw().equals(b.getContentRaw())) {
                EmbedBuilder suki = new EmbedBuilder().setColor(new Color(255, 255, 0));
                suki.setTitle("Message edited", e.getMessage().getJumpUrl().replaceAll("discord.com", "discordapp.com"));
                suki.addField("Old text", a.getMessage().getContentDisplay(), false);
                suki.addField("New text", b.getContentDisplay(), false);
                suki.setFooter(e.getAuthor().getAsTag(), e.getAuthor().getAvatarUrl());
                MessageAction out = Main.jda.getTextChannelById(guilds.get(e.getGuild().getIdLong())).sendMessage(suki.build());
                if (!a.getMessage().getAttachments().isEmpty())
                    out.addFile(a.getAttachments().get(0).getFile(), a.getAttachments().get(0).getFilename());
                out.queue();
            }
        }
    }

    public void onMessageDelete(MessageDeleteEvent e) {
        if (messages.containsKey(e.getMessageIdLong())) {
            CachedMessage deleted = messages.get(e.getMessageIdLong());

            EmbedBuilder suki = new EmbedBuilder().setColor(new Color(255, 0, 0));
            suki.setTitle("Message deleted");
            suki.addField("Text", deleted.getMessage().getContentDisplay(), false);
            suki.setFooter(deleted.getMessage().getAuthor().getAsTag(), deleted.getMessage().getAuthor().getAvatarUrl());
            MessageAction out = Main.jda.getTextChannelById(guilds.get(e.getGuild().getIdLong())).sendMessage(suki.build());
            if (!deleted.getMessage().getAttachments().isEmpty())
                out.addFile(deleted.getAttachments().get(0).getFile(), deleted.getAttachments().get(0).getFilename());
            out.queue();

            messages.remove(e.getMessageIdLong());
            messageids.remove(e.getMessageIdLong());
        }
    }
}
