package org.komeiji.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.komeiji.resources.Safe;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(Safe.TESTBOTKEY);
        builder.setActivity(Activity.watching("the chat"));
        JDA jda = builder.build();
    }
}
