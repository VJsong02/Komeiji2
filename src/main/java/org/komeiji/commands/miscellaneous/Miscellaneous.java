package org.komeiji.commands.miscellaneous;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.resources.Functions;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Random;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.resources.Safe.OWNERID;
import static org.komeiji.resources.Safe.homeurl;

public class Miscellaneous extends ListenerAdapter {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    HashMap<String, String> numbers = new HashMap<>() {
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

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            // assign commonly used variables for brevity
            Message m = e.getMessage();
            MessageChannel c = e.getChannel();
            User u = e.getAuthor();

            // ping, time between own server and discord
            if (m.getContentDisplay().equals(prefix + "ping"))
                c.sendMessage((OffsetDateTime.now().getNano() - m.getTimeCreated().getNano()) / 1000000 + " ms").queue();

            // randcaps, randomizes capitalization and sends resulting text with a spongebob image
            else if (m.getContentDisplay().startsWith(prefix + "randcaps")) {
                if (m.getContentDisplay().length() > 9) {
                    String message = m.getContentDisplay().substring(prefix.length() + 8);
                    StringBuilder out = new StringBuilder();
                    for (int i = 0; i < message.length(); i++) {
                        out.append(new Random().nextBoolean() ? Character.toUpperCase(message.charAt(i))
                                : Character.toLowerCase(message.charAt(i)));
                    }
                    try {
                        Functions.sendFileMessage(
                                c,
                                homeurl + "files/botgifs/spongebob.jpg",
                                "spongebob.jpg",
                                out.toString()
                        );
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

            // regindicate, turns a message into its emoji equivalent
            else if (m.getContentDisplay().startsWith(prefix + "regindicate")) {
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
                c.sendMessage(out.toString()).queue();
            }

            // groovy parser
            else if (m.getContentDisplay().startsWith(prefix + "eval") && u.getIdLong() == OWNERID) {
                GroovyShell g = new GroovyShell();

                g.setProperty("c", c);
                g.setProperty("e", e);
                g.setProperty("m", m);

                try {
                    Object result = g.evaluate(m.getContentRaw().substring(prefix.length() + 4));
                    if (result instanceof Message) c.sendMessage((Message) result).queue();
                    else if (result instanceof MessageEmbed) c.sendMessage((MessageEmbed) result).queue();
                    else c.sendMessage(result.toString()).queue();

                    m.addReaction("\u2705").queue();
                } catch (Exception ex) {
                    u.openPrivateChannel().complete().sendMessage("```" + ex.toString() + "```").queue();
                }
            }
        }
    }
}
