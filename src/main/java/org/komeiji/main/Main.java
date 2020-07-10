package org.komeiji.main;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static final String prefix = "t!";

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("Initializing...");
        Initialization.initialize();

//        JDA jda = JDABuilder.createDefault(Safe.TESTBOTKEY)
//                .addEventListeners(
//                        new Miscellaneous()
//                )
//                .setActivity(Activity.watching("the chat"))
//                .build();

        System.out.println("Finished loading.");
    }
}
