package org.komeiji.commands.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.resources.CommandResources.GifFunctions;
import org.komeiji.resources.CommandResources.GifMessage;
import org.komeiji.resources.Functions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.komeiji.main.Main.prefix;

public class GIFs extends ListenerAdapter {
    public static ArrayList<String> gifs = new ArrayList<>();

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            // assign commonly used variables for brevity
            Message m = e.getMessage();
            String[] message = m.getContentDisplay().split(" ");
            TextChannel c = e.getTextChannel();
            User u = e.getAuthor();

            if (gifs.contains(message[0].substring(prefix.length()))) {
                try {
                    GifMessage gm = GifFunctions.fetchGifMessage(message[0].substring(prefix.length()));
                    String link = gm.getGifMessageFiles()[ThreadLocalRandom.current().nextInt(gm.getGifMessageFiles().length)];
                    String text = gm.getGifMessageText().replace("[self]", u.getAsMention());

                    boolean send = false;
                    if (!gm.isNsfw())
                        if (!gm.requiresMention())
                            send = true;
                        else if (!m.getMentionedMembers().isEmpty()) {
                            text = text.replace("[mention]", m.getMentionedMembers().get(0).getAsMention());
                            send = true;
                        } else c.sendMessage("Ping someone after the command next time you try.").queue();
                    else if (c.isNSFW())
                        send = true;
                    else c.sendMessage("You pervert.").queue();

                    if (send)
                        Functions.sendFileMessage(c, link, link.split("/")[link.split("/").length - 1], text);

                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
