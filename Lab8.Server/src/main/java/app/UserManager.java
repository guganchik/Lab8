package app;

import collections.User;
import dao.DataAccessDriver;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.*;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }            
    
    
    private TreeSet<User> users = new TreeSet(); 

    public UserManager() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            pst = con.prepareStatement("SELECT \"login\", \"hash\" FROM \"users\"");
            rs = pst.executeQuery();
            User user;
            while (rs.next()) {
                user = User.of(
                        rs.getString("login"), 
                        rs.getString("hash")
                        );
                users.add(user);
            }
            rs.close(); rs = null;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while selecting users");
        } finally {
            if (rs != null) { try { rs.close(); } catch (Exception e) { } rs = null; }
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
    }
    
    public User get(String login) {
        for (User user: users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }    
    
    public User get(String login, String hash) {
        for (User user: users) {
            if (user.getLogin().equals(login) && user.getHash().equals(hash)) {
                return user;
            }
        }
        return null;
    }    
    
    
    
    public boolean register(User user) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "INSERT INTO users (login, hash) VALUES(?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, user.getLogin());
            pst.setString(2, user.getHash());
            pst.executeUpdate();
            success = true;
            users.add(user);
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while inserting");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
}
