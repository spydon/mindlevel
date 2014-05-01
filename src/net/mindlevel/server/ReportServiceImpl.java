package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mindlevel.client.services.ReportService;
import net.mindlevel.shared.Report;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class ReportServiceImpl extends DBConnector implements ReportService {

    private final TokenServiceImpl tokenService = new TokenServiceImpl();

    @Override
    public void addReport(Report report, String token) throws IllegalArgumentException {
        try {
            if(token.equals("") || tokenService.validateAuth(report.getUsername(), token)) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO report (url, content, type, username) "
                        + "VALUES (?, ?, ?, ?)");
                ps.setString(1, report.getUrl());
                ps.setString(2, report.getContent());
                ps.setString(3, report.getType());
                ps.setString(4, report.getUsername());

                ps.executeUpdate();

                ps.close();
                conn.close();
            } else {
                throw new IllegalArgumentException("Seems like you tried to use another users username.");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}