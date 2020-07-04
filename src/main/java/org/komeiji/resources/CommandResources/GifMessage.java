package org.komeiji.resources.CommandResources;

import java.util.ArrayList;

public class GifMessage {
    private final String name;

    private final String text;

    private final ArrayList<String> files;

    private final boolean requiresmention;

    private final boolean nsfw;

    GifMessage(String name, String ins, ArrayList<String> inf, boolean rm, boolean nsfw) {
        this.name = name;
        this.text = ins;
        this.files = inf;
        this.requiresmention = rm;
        this.nsfw = nsfw;
    }

    public ArrayList<String> getGifMessageFiles() {
        return this.files;
    }

    public String getGifMessageText() {
        return this.text;
    }

    public boolean isNsfw() {
        return this.nsfw;
    }

    public boolean requiresMention() {
        return this.requiresmention;
    }
}
