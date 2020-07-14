package org.komeiji.resources.CommandResources;

public class GifMessage {
    private final String name;

    private final String text;

    private final String[] files;

    private final boolean requiresmention;

    private final boolean nsfw;

    GifMessage(String name, String s, String[] f, boolean rm, boolean nsfw) {
        this.name = name;
        this.text = s;
        this.files = f;
        this.requiresmention = rm;
        this.nsfw = nsfw;
    }

    public String[] getGifMessageFiles() {
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
