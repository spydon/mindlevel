package net.mindlevel.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public abstract class DBConnector extends RemoteServiceServlet {
    protected String url = "jdbc:mysql://localhost/mindleveö";
    protected String dbUser = "root";
    protected String pass = "YouWillNeverRealizeHowFuckingLongThisPasswordIs!";

    protected Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}