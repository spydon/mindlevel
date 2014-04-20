package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.NewsService;
import net.mindlevel.shared.News;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class NewsServiceImpl extends DBConnector implements NewsService {

    @Override
    public ArrayList<News> getNews(int number) {
        ArrayList<News> newsList = new ArrayList<News>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id, username, content, timestamp FROM news ORDER BY timestamp desc LIMIT ?");
            ps.setInt(1, number);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                News news = new News(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("content"),
                        rs.getTimestamp("timestamp"));
                newsList.add(news);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}