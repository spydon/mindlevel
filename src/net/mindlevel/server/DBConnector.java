package net.mindlevel.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public abstract class DBConnector extends RemoteServiceServlet {
	protected String url = "jdbc:mysql://localhost/mindlevel";
	protected String dbUser = "root";
	protected String pass = "L1e2A3";

//	@Deprecated
//	protected void connect() {
//		try {
//			//Class.forName("com.mysql.jdbc.Driver").newInstance();
//			conn = DriverManager.getConnection(url, dbUser, pass);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Deprecated
//	protected void disconnect() {
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	protected Connection getConnection() {
		Connection conn = null;
		try {
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, dbUser, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}