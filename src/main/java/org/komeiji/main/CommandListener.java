package org.komeiji.main;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.commands.commands.CustomCommands;
import org.komeiji.commands.commands.GIFs;
import org.komeiji.resources.Functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.main.Main.safe;

public class CommandListener extends ListenerAdapter {
    public static HashMap<String, Method> commands = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot() && e.getMessage().getContentDisplay().startsWith(prefix)) {
            String potential = e.getMessage().getContentDisplay().split(" ")[0].replace(prefix, "");
            if (commands.containsKey(potential)) {
                try {
                    commands.get(potential).invoke(null, e.getMessage());
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            } else if (e.getMessage().getContentDisplay().startsWith(prefix + "eval") && e.getAuthor().getIdLong() == safe.OWNERID) {
                GroovyShell g = new GroovyShell();

                g.setProperty("e", e);
                g.setProperty("c", e.getChannel());
                g.setProperty("g", e.getGuild());

                g.setProperty("commands", commands);
                g.setProperty("customCommands", CustomCommands.customcommands);
                g.setProperty("GIFs", GIFs.gifs);

                try {
                    Object o = g.evaluate(e.getMessage().getContentRaw().substring(prefix.length() + 4));
                    if (o instanceof Message) e.getChannel().sendMessage((Message) o).queue();
                    else if (o instanceof MessageEmbed) e.getChannel().sendMessage((MessageEmbed) o).queue();
                    else e.getChannel().sendMessage(o.toString()).queue();

                    e.getMessage().addReaction("\u2705").queue();
                } catch (NullPointerException ignored) {
                } catch (Exception ex) {
                    Functions.directMessage(safe.OWNERID, "```" + ex.toString() + "```");
                }
            }
        }
    }
}
