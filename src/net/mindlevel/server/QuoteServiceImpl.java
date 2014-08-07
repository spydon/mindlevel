package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.QuoteService;
import net.mindlevel.shared.Quote;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class QuoteServiceImpl extends DBConnector implements QuoteService {

    @Override
    public Quote getQuote() {
        Quote quote = new Quote("spydon", "No quotes added");
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, quote "
                    + "FROM `quote` "
                    + "WHERE id >= (SELECT FLOOR(MAX(id) * RAND()) FROM `quote` ) "
                    + "ORDER BY id LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                quote = new Quote(rs.getString("username"), rs.getString("quote"));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return quote;
    }

    @Override
    public Quote getNotFound() {
        Quote quote = new Quote("Animal", "No quotes added");
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username, quote "
                    + "FROM `not_found` "
                    + "WHERE id >= (SELECT FLOOR(MAX(id+1) * RAND()) FROM `not_found` ) "
                    + "ORDER BY id LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                quote = new Quote(rs.getString("username"), rs.getString("quote"));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return quote;
    }
}