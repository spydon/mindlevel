package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.RatingService;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class RatingServiceImpl extends DBConnector implements RatingService {
    @Override
    public int getVoteValue(String username, int pictureId) throws IllegalArgumentException{
        int value = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT score FROM rating "
                    + "WHERE username = ? AND picture_id = ?");
            ps.setString(1, username);
            ps.setInt(2, pictureId);
            ResultSet rs = ps.executeQuery();
            if(rs.first())
                value = rs.getInt("score");
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void setVoteValue(String token, int pictureId, int value) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            User user = new UserServiceImpl().getUserFromToken(token);
            PreparedStatement precheck = conn.prepareStatement("SELECT picture_id FROM rating WHERE username = ? AND picture_id = ?");
            precheck.setString(1, user.getUsername());
            precheck.setInt(2, pictureId);
            ResultSet rs = precheck.executeQuery();
            if(!rs.first()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO rating "
                        + "(username, picture_id, score) values (?,?,?) "
                        + "on duplicate key update score=values(score)");
                ps.setString(1, user.getUsername());
                ps.setInt(2, pictureId);
                ps.setInt(3, value);
                ps.executeUpdate();
                ps.close();
            } else {
                throw new IllegalArgumentException("You've already voted on this picture...");
            }
            precheck.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getScore(int id) throws IllegalArgumentException {
//        int total = 0;
//        int voteNum = 0;
        double voteAvg = 0.0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT AVG(score) As vote_score "
                    + "FROM rating WHERE picture_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first())
                voteAvg = rs.getDouble("vote_score");
//            while(rs.next()) {
//                total+=rs.getInt("score");
//                voteNum++;
//            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
//        if(voteNum!=0)
//            return Math.round((double)total/(double)voteNum * 100.0)/100.0;
//        return (double)voteNum;
        return voteAvg;
    }

    @Override
    public int getVoteNumber(int id) throws IllegalArgumentException {
        int total = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) AS vote_number "
                    + "FROM rating WHERE picture_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first())
                total = rs.getInt("vote_number");
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void deleteRatings(int pictureId, String token) throws IllegalArgumentException {
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement("DELETE FROM rating WHERE picture_id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not delete ratings.");
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }
}