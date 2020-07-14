package org.komeiji.resources;

public class Safe {
    public Safe(long a, String b, String c, String d, String e, String f, String g, String h, String i) {
        this.OWNERID = a;
        this.MAINBOTKEY = b;
        this.TESTBOTKEY = c;
        this.WOLFRAMALPHAKEY = d;
        this.SAUCENAOKEY = e;
        this.DBURL = f;
        this.DBUSERNAME = g;
        this.DBPASSWORD = h;
        this.HOMEURL = i;
    }

    public final long OWNERID;

    public final String MAINBOTKEY;
    public final String TESTBOTKEY;

    public final String WOLFRAMALPHAKEY;
    public final String SAUCENAOKEY;

    public final String DBURL;
    public final String DBUSERNAME;
    public final String DBPASSWORD;

    public final String HOMEURL;
}