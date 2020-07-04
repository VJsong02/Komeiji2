package org.komeiji.resources.UserResources;

public class Usr {
    private String id = "";

    private Boolean isAdmin = false;

    private Boolean isBlacklisted = false;

    private Boolean isEvaler = false;

    private Long timeout = 0L;

    private String home = "";

    public Usr() {
    }

    public Usr(String id, Boolean isAdmin, Boolean isBlacklisted, Boolean isEvaler, Long timeout, String home) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.isBlacklisted = isBlacklisted;
        this.isEvaler = isEvaler;
        this.timeout = timeout;
        this.home = home;
    }

    public String getId() {
        return this.id;
    }

    public Boolean isAdmin() {
        return this.isAdmin;
    }

    public Boolean isBlacklisted() {
        return this.isBlacklisted;
    }

    public Boolean isEvaler() {
        return this.isEvaler;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public String getHome() {
        return this.home;
    }
}
