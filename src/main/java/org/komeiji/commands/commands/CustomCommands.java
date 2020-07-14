package org.komeiji.commands.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.main.Initialization;
import org.komeiji.main.Main;
import org.komeiji.resources.CommandResources.CustomFunctions;
import org.komeiji.resources.CommandResources.CustomMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;

import static org.komeiji.main.Main.logger;
import static org.komeiji.main.Main.prefix;

public class CustomCommands extends ListenerAdapter {
    public static HashMap<String, Long> customcommands = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            Message m = e.getMessage();
            String[] message = m.getContentDisplay().split(" ");
            MessageChannel c = e.getChannel();
            User u = e.getAuthor();

            if (m.getContentDisplay().startsWith(prefix + "addcommand"))
                if (m.getContentDisplay().split(" ").length == 3)
                    if (!customcommands.containsKey(message[1]))
                        try {
                            new URL(message[2]);
                            CustomFunctions.insertCustomCommand(message[1], message[2], u.getName(), u.getIdLong());
                            customcommands = Initialization.loadCustomCommands();
                            logger.info(u.getName() + "(" + u.getId() + ") added " + message[1]);
                            e.getMessage().addReaction("\u2705").queue();
                        } catch (MalformedURLException ex) {
                            c.sendMessage("Your url doesn't work. Try again.").queue();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    else
                        c.sendMessage("That command's taken, tough luck.").queue();
                else
                    c.sendMessage("You're supposed to write `!addcommand` then its name and then a link to the gif or image or whatever you want to send...").queue();


            else if (m.getContentDisplay().startsWith(prefix + "removecommand"))
                if (message.length == 2)
                    if (customcommands.containsKey(message[1]))
                        if (customcommands.get(message[1]) == u.getIdLong()) {
                            try {
                                CustomFunctions.removeCustomCommand(message[1]);
                                customcommands = Initialization.loadCustomCommands();
                                logger.info(u.getName() + "(" + u.getId() + ") removed " + message[1]);
                                e.getMessage().addReaction("\u2705").queue();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        } else
                            c.sendMessage("Are you trying to remove someone else's command?").queue();
                    else
                        c.sendMessage("Are you trying to remove a command that doesn't exist?").queue();
                else
                    c.sendMessage("You're supposed to write `!removecommand` then its name...").queue();


            else if (customcommands.containsKey(m.getContentDisplay().substring(1))) {
                try {
                    CustomMessage cm = CustomFunctions.fetchCustomCommand(m.getContentDisplay().substring(1));

                    EmbedBuilder suki = new EmbedBuilder();
                    suki.setColor(Main.clr);
                    suki.setImage(cm.getLink());
                    suki.setTitle(cm.getAuthor());

                    c.sendMessage(suki.build()).queue();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
