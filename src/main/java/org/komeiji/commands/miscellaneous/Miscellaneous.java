package org.komeiji.commands.miscellaneous;

import net.dv8tion.jda.api.entities.Message;
import org.komeiji.commands.commands.CustomCommands;
import org.komeiji.commands.commands.GIFs;
import org.komeiji.main.Initialization;
import org.komeiji.resources.Functions;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.komeiji.main.Main.*;

public class Miscellaneous {
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static final Map<String, String> numbers = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put("1", ":one:");
            put("2", ":two:");
            put("3", ":three:");
            put("4", ":four:");
            put("5", ":five:");
            put("6", ":six:");
            put("7", ":seven:");
            put("8", ":eight:");
            put("9", ":nine:");
            put("0", ":zero:");
        }
    };

    // ping, gets time between message creation and time received by server
    static void ping(Message m) {
        m.getChannel().sendMessage((OffsetDateTime.now().getNano() - m.getTimeCreated().getNano()) / 1000000 + " ms").queue();
    }

    // get current bot version
    static void version(Message m) {
        m.getChannel().sendMessage(VERSION).queue();
    }

    // uptime
    static void uptime(Message m) {
        Date date = new Date();
        long millis = date.getTime() - startTime;
        m.getChannel().sendMessage(String.format("%02dh %02dm %02ds",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))).queue();
    }

    // randcaps, randomizes capitalization and sends resulting text with a spongebob image
    static void randcaps(Message m) {
        if (m.getContentDisplay().length() > 9) {
            String message = m.getContentDisplay().substring(prefix.length() + 8);
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                out.append(new Random().nextBoolean() ? Character.toUpperCase(message.charAt(i))
                        : Character.toLowerCase(message.charAt(i)));
            }
            try {
                Functions.sendFileMessage(
                        m.getChannel(),
                        safe.HOMEURL + "files/botgifs/spongebob.jpg",
                        "spongebob.jpg",
                        out.toString()
                );
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // regindicate, turns a message into its emoji equivalent
    static void regindicate(Message m) {
        String input = m.getContentDisplay().substring(prefix.length() + 11).toLowerCase();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (alphabet.contains("" + input.charAt(i))) {
                out.append(":regional_indicator_").append(input.charAt(i)).append(":");
            } else if (numbers.containsKey("" + input.charAt(i))) {
                out.append((numbers.get(input.charAt(i) + "")));
            } else if (input.charAt(i) == '!') {
                out.append(":exclamation:");
            } else if (input.charAt(i) == '?') {
                out.append(":question:");
            } else {
                out.append(input.charAt(i));
            }
        }
        m.getChannel().sendMessage(out.toString()).queue();
    }

    static void reloadcustom(Message m) {
        if (m.getAuthor().getIdLong() == safe.OWNERID) {
            HashMap<String, Long> commands = new HashMap<>();
            try {
                commands = Initialization.loadCustomCommands();
            } catch (SQLException ex) {
                Functions.directMessage(safe.OWNERID, "```" + ex.toString() + "```");
            }
            CustomCommands.customcommands = commands;
            m.addReaction("\u2705").queue();
        }
    }

    static void reloadgifs(Message m) {
        if (m.getAuthor().getIdLong() == safe.OWNERID) {
            ArrayList<String> commands = new ArrayList<>();
            try {
                commands = Initialization.loadGifCommands();
            } catch (SQLException ex) {
                Functions.directMessage(safe.OWNERID, "```" + ex.toString() + "```");
            }
            GIFs.gifs = commands;
            m.addReaction("\u2705").queue();
        }
    }

    static void reloadconfig(Message m) {
        if (m.getAuthor().getIdLong() == safe.OWNERID) {
            Initialization.readConfig();
            m.addReaction("\u2705").queue();
        }
    }
}
