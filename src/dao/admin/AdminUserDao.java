package dao.admin;

import model.admin.AdminUser;
import dao.DatabaseConnection;
import model.admin.Attendance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class AdminUserDao {
    private final Connection connection;

    public AdminUserDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<AdminUser> findAll() throws SQLException {
        List<AdminUser> users = new ArrayList<>();
        String sql = """
            SELECT u.user_id, u.username, u.password_hash, u.role_id,
                   r.role_code, r.role_name, u.is_active, u.last_login,
                   u.created_at, u.updated_at
            FROM users u
            LEFT JOIN roles r ON u.role_id = r.role_id
            ORDER BY u.user_id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }
    public AdminUser findById(Long id) throws SQLException {
        String sql = """
            SELECT u.user_id, u.username, u.password_hash, u.role_id,
                   r.role_code, r.role_name, u.is_active, u.last_login,
                   u.created_at, u.updated_at
            FROM users u
            LEFT JOIN roles r ON u.role_id = r.role_id
            WHERE u.user_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }
    public List<AdminUser> searchByUsername(String keyword) throws SQLException {
        List<AdminUser> users = new ArrayList<>();
        String sql = """
            SELECT u.user_id, u.username, u.password_hash, u.role_id,
                   r.role_code, r.role_name, u.is_active, u.last_login,
                   u.created_at, u.updated_at
            FROM users u
            LEFT JOIN roles r ON u.role_id = r.role_id
            WHERE u.username LIKE ?
            ORDER BY u.user_id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        }
        return users;
    }
    public boolean create(AdminUser user) throws SQLException {
        String sql = """
            INSERT INTO users (username, password_hash, role_id, is_active)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setInt(3, user.getRoleId());
            ps.setBoolean(4, user.isActive());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getLong(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean update(AdminUser user) throws SQLException {
        String sql = """
            UPDATE users
            SET username = ?, password_hash = ?, role_id = ?, is_active = ?
            WHERE user_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setInt(3, user.getRoleId());
            ps.setBoolean(4, user.isActive());
            ps.setLong(5, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean assignRole(Long id, int roleId) throws SQLException {
        String sql = "UPDATE users SET role_id = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deactivate(Long id) throws SQLException {
        String sql = "UPDATE users SET is_active = false WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public Map<String, Integer> getStatistics() throws SQLException {
        List<AdminUser> users = findAll();
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("tổng số", users.size());
        stats.put("hoạt động", countActive(users));
        stats.put("bất hoạt", countInactive(users));
        stats.put("admin", countByRole(users, "ADMIN"));
        stats.put("hr", countByRole(users, "HR"));
        stats.put("employee", countByRole(users, "EMPLOYEE"));
        return stats;
    }
    private int countActive(List<AdminUser> users) {
        int count = 0;
        for (AdminUser user : users) {
            if (user.isActive()) {
                count++;
            }
        }
        return count;
    }
    private int countInactive(List<AdminUser> users) {
        int count = 0;
        for (AdminUser user : users) {
            if (!user.isActive()) {
                count++;
            }
        }
        return count;
    }
    private int countByRole(List<AdminUser> users, String roleCode) {
        int count = 0;
        for (AdminUser user : users) {
            if (roleCode.equalsIgnoreCase(user.getRoleCode())) {
                count++;
            }
        }
        return count;
    }
    private AdminUser mapRow(ResultSet rs) throws SQLException {
        AdminUser user = new AdminUser();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleCode(rs.getString("role_code"));
        user.setRoleName(rs.getString("role_name"));
        user.setActive(rs.getBoolean("is_active"));
        user.setLastLogin(rs.getString("last_login"));
        user.setCreatedAt(rs.getString("created_at"));
        user.setUpdatedAt(rs.getString("updated_at"));
        return user;
    }
    public boolean changePassword(long id, String password) throws SQLException {
        String sql = """
            UPDATE users
            SET password_hash = ?
            WHERE user_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashPassword(password));
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Attendance> ATT_findByUserId(long id) throws SQLException
    {
        List<Attendance> attendances = new ArrayList<>();
        String sql = """
            SELECT a.employee_id, a.attendance_date, check_in, check_out
            FROM attendances a
            WHERE a.employee_id = ?
            ORDER BY a.attendance_date ASC;
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance att = new Attendance();
                    att.setEmp_id(rs.getLong("employee_id"));
                    att.setDate(rs.getDate("attendance_date"));
                    att.setCheck_in(rs.getDate("check_in"));
                    att.setCheck_out(rs.getDate("check_out"));
                    attendances.add(att);
                }
            }
        }
        return attendances;
    }
}