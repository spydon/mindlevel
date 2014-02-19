package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.CommentService;
import net.mindlevel.shared.Comment;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class CommentServiceImpl extends DBConnector implements CommentService {

    @Override
    public ArrayList<Comment> getComments(int threadId) throws IllegalArgumentException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id, username, comment, parent_id, timestamp FROM comment "
                    + "WHERE thread_id = ? ORDER BY parent_id, timestamp");
            ps.setInt(1, threadId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("comment"),
                        rs.getInt("partent_id"),
                        rs.getInt("timestamp"));
                comments.add(comment);

            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}