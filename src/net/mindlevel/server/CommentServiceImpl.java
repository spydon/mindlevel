package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.CommentService;
import net.mindlevel.shared.Comment;

import com.mysql.jdbc.Statement;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class CommentServiceImpl extends DBConnector implements CommentService {

    @Override
    public ArrayList<Comment> getComments(int threadId) throws IllegalArgumentException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id, thread_id, username, comment, parent_id, timestamp FROM comment "
                    + "WHERE thread_id = ? ORDER BY parent_id, timestamp");
            ps.setInt(1, threadId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("id"),
                        rs.getInt("thread_id"),
                        rs.getString("username"),
                        rs.getString("comment"),
                        rs.getInt("parent_id"),
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

    @Override
    public Integer addComment(Comment comment) {
        int id = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO comment (thread_id, parent_id, username, comment) "
                    + "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, comment.getThreadId());
            ps.setInt(2, comment.getParentId());
            ps.setString(3, comment.getUsername());
            ps.setString(4, comment.getComment());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            keys.first();
            id = keys.getInt(1);

            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}