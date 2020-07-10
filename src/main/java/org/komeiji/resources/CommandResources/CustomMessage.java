package org.komeiji.resources.CommandResources;

public class CustomMessage {
    private final String link;

    private final String author;

    public CustomMessage(String link, String author) {
        this.link = link;
        this.author = author;
    }

    public String getLink() {
        return this.link;
    }

    public String getAuthor() {
        return this.author;
    }
}
