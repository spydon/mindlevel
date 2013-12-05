package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.shared.Mission;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class MissionServiceImpl extends DBConnector implements MissionService {

    @Override
    public List<Mission> getMissions(int start, int end, boolean validated) throws IllegalArgumentException, NoSuchElementException {
        ArrayList<Mission> missions = null;

        try {
            Connection conn = getConnection();
            PreparedStatement ps =
                    conn.prepareStatement("SELECT "
                                        + "mission.id, "
                                        + "mission.name, "
                                        + "mission.adult, "
                                        + "user.username As creator, "
                                        + "mission.timestamp "
                                        + "FROM mission "
                                        + "INNER JOIN user "
                                        + "ON mission.user_id = user.id "
                                        + "ORDER BY mission.name "
                                        + "LIMIT ?,?");
            ps.setInt(1, start);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            //TODO: Get categories

            while(rs.next()) {
                if(rs.isFirst())
                    missions = new ArrayList<Mission>();
                Mission mission = new Mission();
                mission.setId(rs.getInt("id"));
                mission.setName(rs.getString("name"));
                mission.setAdult(rs.getBoolean("adult"));
                mission.setCreator(rs.getString("creator"));
                mission.setTimestamp(rs.getString("timestamp"));
                missions.add(mission);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return missions;
    }

    @Override
    public int getMissionCount(boolean validated) throws IllegalArgumentException {
        int count = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM mission");
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                count = rs.getInt("count");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Mission getMission(int id, boolean validated) throws IllegalArgumentException {
        Mission mission = null;
        try {
            Connection conn = getConnection();
            PreparedStatement ps =
                    conn.prepareStatement("SELECT "
                                        + "mission.id, "
                                        + "mission.name, "
                                        + "mission.description, "
                                        + "mission.adult, "
                                        + "user.username, "
                                        + "mission.timestamp "
                                        + "FROM mission "
                                        + "INNER JOIN user "
                                        + "ON user_id = user.id "
                                        + "WHERE id = ?");
            //TODO: Fix categories
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                mission = new Mission();
                mission.setName(rs.getString("name"));
                mission.setCategories(getCategories(rs.getInt("id")));
                mission.setId(rs.getInt("id"));
                mission.setDescription(rs.getString("description"));
                mission.setAdult(rs.getBoolean("adult"));
                mission.setCreator(rs.getString("creator"));
                mission.setTimestamp(rs.getString("timestamp"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return mission;
    }

    @Override
    public void uploadMission(Mission mission, String token) throws IllegalArgumentException {
        try {
            if(new TokenServiceImpl().validateAdminToken(token)) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT name FROM mission "
                                + "INNER JOIN user "
                                + "ON user_id = user.id WHERE "
                                + "name=? AND "
                                + "description=? AND "
                                + "username=?");
                ps.setString(1, mission.getName());
                ps.setString(2, mission.getDescription());
                ps.setString(3, mission.getCreator());
                ResultSet rs = ps.executeQuery();
                if(!rs.first()) {
                    PreparedStatement insertMission = conn.prepareStatement(
                            "INSERT INTO mission "
                            + "(name, "
                            + "description, "
                            + "adult, "
                            + "user_id, "
                            + "values "
                            + "(?, ?, ?, ?)");
                    insertMission.setString(1, mission.getName());
                    insertMission.setString(2, mission.getDescription());
                    insertMission.setBoolean(3, mission.getAdult());
                    insertMission.setString(4, mission.getCreator());

                    int result = insertMission.executeUpdate();
                    ps.close();
                    conn.close();
                    //TODO: Add categories
                    if (result != 1)
                        throw new IllegalArgumentException("Unknown error.");
                } else {
                    throw new IllegalArgumentException("The mission already exists.");
                }
            } else {
                throw new IllegalArgumentException(
                        "You are not admin, you sneaky bastard! ;)");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Something went wrong.");
        }
    }

    @Override
    public ArrayList<String> getCategories(int id) {
        ArrayList<String> categories = new ArrayList<String>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM category "
                    + "INNER JOIN mission_category ON category_id = category.id "
                    + "WHERE mission_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                categories.add(rs.getString("name"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }


    @Override
    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<String>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM category");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                categories.add(rs.getString("name"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}