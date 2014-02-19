package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.mindlevel.client.services.CategoryService;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class CategoryServiceImpl extends DBConnector implements CategoryService {

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

    //TODO: Mission id is not fixed
    //TODO: Get a better name for this
    //Already check authentication and this is not visible outside server
    public void connectCategories(int missionId, ArrayList<String> categories) {
        ArrayList<String> knownCategories = getCategories();
        for(String category : categories) {
            category = category.toLowerCase();
            for(String knownCategory : knownCategories) {
                if(knownCategory.equals(category)) {
                    Connection conn = getConnection();
                    PreparedStatement ps;
                    int result = 0;
                    try {
                        ps = conn.prepareStatement(
                                "INSERT INTO mission_category "
                                        + "(mission_id, "
                                        + "category_id) "
                                        + "values "
                                        + "(?, ?)");
                        ps.setInt(1, missionId);
                        ps.setInt(2, getCategoryId(category));

                        result = ps.executeUpdate();
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException(
                                "Something went wrong on the server.");
                    }

                    if (result != 1)
                        throw new IllegalArgumentException("Unknown error.");
                }
            }
        }
    }

    private int getCategoryId(String category) {
        Connection conn = getConnection();
        PreparedStatement ps;
        int result = -1;
        try {
            ps = conn.prepareStatement("SELECT id FROM category WHERE name = ?");
            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                result = rs.getInt("id");
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Something went wrong on the server.");
        }

        if (result == -1)
            throw new IllegalArgumentException("Category does not exist.");
        return result;
    }

    //Gets the categories for a specific mission
    //@Override
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
}