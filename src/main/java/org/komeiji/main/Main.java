package org.komeiji.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.komeiji.commands.miscellaneous.Miscellaneous;
import org.komeiji.resources.Safe;

import javax.security.auth.login.LoginException;

public class Main {
    public static final String prefix = "t!";

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(Safe.TESTBOTKEY)
                .addEventListeners(
                        new Miscellaneous()
                )
                .setActivity(Activity.watching("the chat"))
                .build();
    }
}
