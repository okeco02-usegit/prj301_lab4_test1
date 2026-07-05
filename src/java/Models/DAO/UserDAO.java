package Models.DAO;

import Models.DTO.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Registration table.
 * Edit DB_USER and DB_PASSWORD to match your SQL Server account.
 */
public class UserDAO {

    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // Nếu máy bạn dùng SQLEXPRESS, có thể đổi thành:
    // jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=SampleDB;encrypt=true;trustServerCertificate=true;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SampleDB;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "12345";

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public User checkLogin(String userName, String password) throws Exception {
        String sql = "SELECT UserName, Password, LastName, isAdmin "
                + "FROM Registration WHERE UserName = ? AND Password = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public User getUserByUserName(String userName) throws Exception {
        String sql = "SELECT UserName, Password, LastName, isAdmin "
                + "FROM Registration WHERE UserName = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> searchUsersByLastName(String searchValue) throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT UserName, Password, LastName, isAdmin "
                + "FROM Registration WHERE LastName LIKE ? ORDER BY UserName";

        if (searchValue == null) {
            searchValue = "";
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }
        }
        return users;
    }

    public boolean addUser(User user) throws Exception {
        String sql = "INSERT INTO Registration(UserName, Password, LastName, isAdmin) "
                + "VALUES(?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getLastName());
            ps.setBoolean(4, user.isAdmin());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(String userName) throws Exception {
        String sql = "DELETE FROM Registration WHERE UserName = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUser(User user) throws Exception {
        String sql = "UPDATE Registration SET Password = ?, LastName = ?, isAdmin = ? "
                + "WHERE UserName = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getLastName());
            ps.setBoolean(3, user.isAdmin());
            ps.setString(4, user.getUserName());

            return ps.executeUpdate() > 0;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("UserName"),
                rs.getString("Password"),
                rs.getString("LastName"),
                rs.getBoolean("isAdmin")
        );
    }
}
