package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.exception.UserNotLoggedInException;
import net.mindlevel.client.services.CommentService;
import net.mindlevel.shared.Comment;

import com.mysql.jdbc.Statement;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class CommentServiceImpl extends DBConnector implements CommentService {

    private final TokenServiceImpl tokenService = new TokenServiceImpl();

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
    public Integer addComment(Comment comment, String token) throws UserNotLoggedInException {
        int id = 0;
        try {
            if(tokenService.validateAuth(comment.getUsername(), token)) {
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
            } else {
                throw new UserNotLoggedInException();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void editComment(Comment comment, String token) throws UserNotLoggedInException {
        try {
            if(tokenService.validateAuth(comment.getUsername(), token) || tokenService.validateAdminToken(token)) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("UPDATE comment SET comment = ? WHERE id = ? AND username = ?");

                ps.setString(1, comment.getComment());
                ps.setInt(2, comment.getId());
                ps.setString(3, comment.getUsername());
                ps.executeUpdate();

                ps.close();
                conn.close();
            } else {
                throw new UserNotLoggedInException();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteComment(Comment comment, String token) throws UserNotLoggedInException {
        comment.setComment("Comment deleted by user");
        editComment(comment, token);
    }

    @Override
    public Integer getCommentCount(int threadId) {
        int numberOfComments = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) as count FROM comment WHERE thread_id = ?");
            ps.setInt(1, threadId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                numberOfComments = rs.getInt("count");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return numberOfComments;
    }
}