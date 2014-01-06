package net.mindlevel.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.shared.Mission;

@SuppressWarnings("serial")
public class MissionServiceImpl extends DBConnector implements MissionService {

    private final CategoryServiceImpl categoryService = new CategoryServiceImpl();

    @Override
    public List<Mission> getMissions(int start, int end, boolean validated)
            throws IllegalArgumentException, NoSuchElementException {
        ArrayList<Mission> missions = new ArrayList<Mission>();

        try {
            Connection conn = getConnection();
            PreparedStatement ps =
                    conn.prepareStatement("SELECT "
                                        + "mission.id, "
                                        + "mission.name, "
                                        + "mission.description, "
                                        + "mission.adult, "
                                        + "mission.creator, "
                                        + "mission.timestamp "
                                        + "FROM mission "
                                        + "WHERE validated = ? "
                                        + "ORDER BY mission.name "
                                        + "LIMIT ?,?");
            ps.setBoolean(1, validated);
            ps.setInt(2, start);
            ps.setInt(3, end);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Mission mission = new Mission(
                        rs.getString("name"),
                        categoryService.getCategories(rs.getInt("id")),
                        rs.getString("description"),
                        rs.getString("creator"),
                        rs.getBoolean("adult"));
                mission.setId(rs.getInt("id"));
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
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS count FROM mission WHERE validated = ?");
            ps.setBoolean(1, validated);
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
                                        + "mission.creator, "
                                        + "mission.timestamp "
                                        + "FROM mission "
                                        + "WHERE id = ? AND validated = ?");
            ps.setInt(1, id);
            ps.setBoolean(2, validated);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                mission = new Mission(
                        rs.getString("name"),
                        categoryService.getCategories(rs.getInt("id")),
                        rs.getString("description"),
                        rs.getString("creator"),
                        rs.getBoolean("adult"));
                mission.setId(rs.getInt("id"));
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
            if(new TokenServiceImpl().validateAuth(mission.getCreator(), token)) {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(
                                "SELECT id "
                                + "FROM mission "
                                + "WHERE "
                                + "name = ? AND "
                                + "description = ? AND "
                                + "creator = ?");
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
                            + "creator) "
                            + "values "
                            + "(?, ?, ?, ?)");
                    insertMission.setString(1, mission.getName());
                    insertMission.setString(2, mission.getDescription());
                    insertMission.setBoolean(3, mission.getAdult());
                    insertMission.setString(4, mission.getCreator());

                    int result = insertMission.executeUpdate();
                    insertMission.close();
                    ps.close();

                    if (result != 1)
                        throw new IllegalArgumentException("Unknown error.");
                    //TODO: get mission id after execution of last insert
                    PreparedStatement checkId = conn.prepareStatement(
                            "SELECT id "
                            + "FROM mission "
                            + "WHERE "
                            + "name = ? AND "
                            + "description = ? AND "
                            + "creator = ?");
                    checkId.setString(1, mission.getName());
                    checkId.setString(2, mission.getDescription());
                    checkId.setString(3, mission.getCreator());
                    ResultSet rsCheck = checkId.executeQuery();
                    if(rsCheck.next())
                        categoryService.connectCategories(rsCheck.getInt("id"), mission.getCategories());
                    conn.close();
                } else {
                    throw new IllegalArgumentException("The mission already exists or is already suggested by you.");
                }
            } else {
                throw new IllegalArgumentException(
                        "You are not logged in, you sneaky bastard! ;)");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Something went wrong.");
        }
    }

    @Override
    public void validateMission(int missionId, String username, String token)
            throws IllegalArgumentException {
        try {
            if(new TokenServiceImpl().validateAdminToken(token) &&
               new TokenServiceImpl().validateAuth(username, token)) {
                Connection conn = getConnection();
                PreparedStatement validateMission = conn.prepareStatement(
                        "UPDATE mission "
                        + "SET "
                        + "validated = ?, "
                        + "validator = ? "
                        + "WHERE "
                        + "id = ?");
                validateMission.setBoolean(1, true);
                validateMission.setString(2, username);
                validateMission.setInt(3, missionId);

                int result = validateMission.executeUpdate();
                validateMission.close();
                conn.close();
                if (result != 1)
                    throw new IllegalArgumentException("Unknown error.");
            } else {
                throw new IllegalArgumentException(
                        "You are not logged in, you sneaky bastard! ;)");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Something went wrong.");
        }
    }
}