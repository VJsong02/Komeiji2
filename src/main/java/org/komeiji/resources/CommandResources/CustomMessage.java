package org.komeiji.resources.CommandResources;

import java.util.Date;

public class CustomMessage {
    private final String link;
    private final Date date;
    private final String author;
    private final long id;

    public CustomMessage(String link, Date date, String author, long id) {
        this.link = link;
        this.date = date;
        this.author = author;
        this.id = id;
    }

    public String getLink() {
        return this.link;
    }

    public Date getDate() {
        return this.date;
    }

    public String getAuthor() {
        return this.author;
    }

    public long getId() {
        return this.id;
    }
}
