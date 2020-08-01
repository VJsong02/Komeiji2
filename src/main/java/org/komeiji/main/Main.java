package org.komeiji.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.komeiji.commands.commands.CustomCommands;
import org.komeiji.commands.commands.GIFs;
import org.komeiji.commands.commands.WolframAlpha;
import org.komeiji.listeners.CommandListener;
import org.komeiji.listeners.LogsListener;
import org.komeiji.resources.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    public static final String VERSION = "Version 3.2";
    public static final String prefix = "!";
    public static final Color clr = new Color(118, 131, 41);
    public static final Logger logger = LoggerFactory.getLogger("Komeiji");

    public static Safe safe;
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException {
        logger.info(VERSION);
        logger.info("Prefix: " + prefix);

        logger.info("Initializing...");
        Initialization.readConfig();
        logger.info("Loaded configuration file.");
        Initialization.loadCommands();
        logger.info(CommandListener.commands.keySet().size() + " commands loaded.");
        CustomCommands.customcommands = Initialization.loadCustomCommands();
        logger.info(CustomCommands.customcommands.size() + " custom commands loaded.");
        GIFs.gifs = Initialization.loadGifCommands();
        logger.info(GIFs.gifs.size() + " gifs found.");

        Initialization.getLogChannels();
        Initialization.loadWeatherLocations();

        jda = JDABuilder.createDefault(safe.MAINBOTKEY)
                .addEventListeners(
                        new CommandListener(),
                        new LogsListener(),

                        new WolframAlpha(),
                        new CustomCommands(),
                        new GIFs()
                )
                .setActivity(Activity.watching("the chat"))
                .build().awaitReady();

        logger.info("Initialization complete.");
    }
}

