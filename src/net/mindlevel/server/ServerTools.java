package net.mindlevel.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTools {
    public static final String PATH = "/opt/jetty/webapps/root/";

    public static String shortenDate(Date timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
    }
}
