package org.komeiji.resources;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Functions {
    public static ArrayList<String> getArgs(String in) {
        ArrayList<String> args = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            while (in.charAt(i) != ' ') {
                temp.append(in.charAt(i));
                if (i < in.length() - 1)
                    i++;
            }
            args.add(temp.toString());
            temp.delete(0, temp.length());
        }
        return args;
    }

    public static boolean isDigit(String in) {
        for (int i = 0; i < in.length(); i++) {
            if (!Character.isDigit(in.charAt(i)))
                return false;
        }
        return true;
    }

    public static String toString(String url) throws IOException {
        Scanner in = new Scanner((new URL(url)).openStream(), StandardCharsets.UTF_8);
        String out = in.useDelimiter("\\A").next();
        in.close();
        return out;
    }

    public static void sendFileMessage(MessageChannel c, String url, String filename, String message) throws IOException {
        c.sendMessage(message).addFile(new URL(url).openStream(), filename).queue();
    }
}
