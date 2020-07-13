package org.komeiji.main;

import org.codehaus.groovy.antlr.SourceInfo;
import org.komeiji.commands.miscellaneous.SourceFinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static final String prefix = "t!";

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, IOException {
        System.out.println("Initializing...");
        Initialization.initialize();

//        JDA jda = JDABuilder.createDefault(Safe.TESTBOTKEY)
//                .addEventListeners(
//                        new Miscellaneous()
//                )
//                .setActivity(Activity.watching("the chat"))
//                .build();

        System.out.println("Finished loading.");

        org.komeiji.commands.miscellaneous.SourceFinder.findSource("https://cdn.discordapp.com/attachments/541701809148788736/730253240804966430/mofuringu-artist-futa-elf-futa-exotic-type-_01D5MYSYTXV8JEYW3KF6FD90QW2.jpeg");
    }
}
