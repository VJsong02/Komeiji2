package org.komeiji.commands.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.komeiji.main.Main;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.komeiji.main.Main.safe;

public class WolframAlpha extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot() && e.getMessage().getMentionedUsers().contains(Main.jda.getSelfUser())) {
            String query = e.getMessage().getContentRaw()
                    .replaceAll("<@!" + e.getJDA().getSelfUser().getId() + ">", "");
            try {
                e.getChannel().sendFile((new URL("http://api.wolframalpha.com/v1/simple?appid="
                        + safe.WOLFRAMALPHAKEY + "&units=metric&i="
                        + URLEncoder.encode(query, StandardCharsets.UTF_8))).openStream(), "result.png").queue();
            } catch (IOException ex) {
                e.getChannel().sendMessage("I can't answer that.").queue();
            }
        }
    }
}