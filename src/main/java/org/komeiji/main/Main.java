package org.komeiji.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.komeiji.commands.commands.CustomCommands;
import org.komeiji.commands.commands.GIFs;
import org.komeiji.resources.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    public static final String prefix = "!";
    public static final Color clr = new Color(118, 131, 41);
    public static final Logger logger = LoggerFactory.getLogger("Komeiji");

    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException {
        logger.info("Version 3 beta 1");

        logger.info("Initializing...");
        Initialization.loadCommands();
        logger.info(CommandListener.commands.keySet().size() + " commands loaded.");
        CustomCommands.customcommands = Initialization.loadCustomCommands();
        logger.info(CustomCommands.customcommands.size() + " custom commands loaded.");
        GIFs.gifs = Initialization.loadGifCommands();
        logger.info(GIFs.gifs.size() + " gifs found.");

        jda = JDABuilder.createDefault(Safe.MAINBOTKEY)
                .addEventListeners(
                        new CommandListener(),

                        new CustomCommands(),
                        new GIFs()
                )
                .setActivity(Activity.watching("the chat"))
                .build().awaitReady();

        logger.info("Initialization complete.");

//        org.komeiji.commands.miscellaneous.SourceFinder
//                .findSource("https://cdn.discordapp.com/attachments/541701809148788736/730253240804966430/" +
//                        "mofuringu-artist-futa-elf-futa-exotic-type-_01D5MYSYTXV8JEYW3KF6FD90QW2.jpeg");
    }
}
