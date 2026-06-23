package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, password_hash, role_id, is_active) VALUES (?, ?, ?, ?)";
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            int roleId = findOrCreateRoleId(user.getRole());
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setInt(3, roleId);
                stmt.setBoolean(4, user.getActive() == null || user.getActive());
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    }
                    saveOrUpdateEmployeeProfile(user);
                    connection.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            rollbackQuietly();
            e.printStackTrace();
        } finally {
            restoreAutoCommit(originalAutoCommit);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username=?, password_hash=?, role_id=?, is_active=? WHERE id=?";
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            int roleId = findOrCreateRoleId(user.getRole());
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setInt(3, roleId);
                stmt.setBoolean(4, user.getActive() == null || user.getActive());
                stmt.setInt(5, user.getUserId());
                boolean updated = stmt.executeUpdate() > 0;
                if (updated) {
                    saveOrUpdateEmployeeProfile(user);
                }
                connection.commit();
                return updated;
            }
        } catch (SQLException e) {
            rollbackQuietly();
            e.printStackTrace();
            return false;
        } finally {
            restoreAutoCommit(originalAutoCommit);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE users SET is_active = false WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findById(int id) {
        String sql = baseUserSelect() + " WHERE u.id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = baseUserSelect() + " WHERE u.is_active = true ORDER BY u.id";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> searchByUsername(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = baseUserSelect() +
                " WHERE u.is_active = true AND (u.username LIKE ? OR e.full_name LIKE ? OR e.employee_code LIKE ?) " +
                "ORDER BY u.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + normalizeSearchKeyword(keyword) + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE LOWER(username) = LOWER(?) LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, normalizeSearchKeyword(username));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role_code"));
        user.setActive(rs.getBoolean("is_active"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setEmployeeCode(rs.getString("employee_code"));
        user.setFullName(rs.getString("full_name"));
        user.setGender(rs.getString("gender"));
        user.setDateOfBirth(rs.getDate("date_of_birth"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setHireDate(rs.getDate("hire_date"));
        user.setDepartmentId(getNullableInt(rs, "department_id"));
        user.setPositionId(getNullableInt(rs, "position_id"));
        user.setManagerId(getNullableInt(rs, "manager_id"));
        user.setBaseSalary(rs.getBigDecimal("base_salary"));
        user.setStatus(rs.getString("status"));
        return user;
    }

    private String baseUserSelect() {
        return "SELECT u.id, u.username, u.password_hash, u.is_active, u.last_login, r.role_code, " +
                "e.employee_code, e.full_name, e.gender, e.date_of_birth, e.email, e.phone, e.address, " +
                "e.hire_date, e.department_id, e.position_id, e.manager_id, e.base_salary, e.status " +
                "FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "LEFT JOIN employees e ON e.user_id = u.id";
    }

    private Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private int findOrCreateRoleId(String roleCode) throws SQLException {
        String normalizedRole = normalizeRole(roleCode);
        Integer existingId = findRoleId(normalizedRole);
        if (existingId != null) {
            return existingId;
        }

        String sql = "INSERT INTO roles (role_code, role_name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, normalizedRole);
            stmt.setString(2, normalizedRole);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Khong tao duoc role: " + normalizedRole);
    }

    private Integer findRoleId(String roleCode) throws SQLException {
        String sql = "SELECT id FROM roles WHERE role_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return null;
    }

    private String normalizeRole(String roleCode) {
        if (roleCode == null || roleCode.trim().isEmpty()) {
            return "CANDIDATE";
        }
        return roleCode.trim().toUpperCase();
    }

    private String normalizeSearchKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }
        return keyword.trim();
    }

    private void saveOrUpdateEmployeeProfile(User user) throws SQLException {
        if (employeeExists(user.getUserId())) {
            updateEmployeeProfile(user);
            return;
        }
        if (hasRequiredEmployeeProfile(user)) {
            insertEmployeeProfile(user);
        }
    }

    private boolean employeeExists(int userId) throws SQLException {
        String sql = "SELECT 1 FROM employees WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean hasRequiredEmployeeProfile(User user) {
        return user.getEmployeeCode() != null && !user.getEmployeeCode().trim().isEmpty()
                && user.getFullName() != null && !user.getFullName().trim().isEmpty();
    }

    private void insertEmployeeProfile(User user) throws SQLException {
        String sql = "INSERT INTO employees (" +
                "employee_code, user_id, full_name, gender, date_of_birth, email, phone, address, " +
                "hire_date, department_id, position_id, manager_id, base_salary, status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            fillEmployeeStatement(stmt, user, false);
            stmt.executeUpdate();
        }
    }

    private void updateEmployeeProfile(User user) throws SQLException {
        String sql = "UPDATE employees SET " +
                "employee_code=?, full_name=?, gender=?, date_of_birth=?, email=?, phone=?, address=?, " +
                "hire_date=?, department_id=?, position_id=?, manager_id=?, base_salary=?, status=? " +
                "WHERE user_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            fillEmployeeStatement(stmt, user, true);
            stmt.executeUpdate();
        }
    }

    private void fillEmployeeStatement(PreparedStatement stmt, User user, boolean update) throws SQLException {
        stmt.setString(1, trimToNull(user.getEmployeeCode()));
        if (update) {
            stmt.setString(2, trimToNull(user.getFullName()));
            stmt.setString(3, trimToNull(user.getGender()));
            setSqlDate(stmt, 4, user.getDateOfBirth());
            stmt.setString(5, trimToNull(user.getEmail()));
            stmt.setString(6, trimToNull(user.getPhone()));
            stmt.setString(7, trimToNull(user.getAddress()));
            setSqlDate(stmt, 8, user.getHireDate());
            setNullableInt(stmt, 9, user.getDepartmentId());
            setNullableInt(stmt, 10, user.getPositionId());
            setNullableInt(stmt, 11, user.getManagerId());
            stmt.setBigDecimal(12, user.getBaseSalary());
            stmt.setString(13, trimToNull(user.getStatus()));
            stmt.setInt(14, user.getUserId());
            return;
        }
        stmt.setInt(2, user.getUserId());
        stmt.setString(3, trimToNull(user.getFullName()));
        stmt.setString(4, trimToNull(user.getGender()));
        setSqlDate(stmt, 5, user.getDateOfBirth());
        stmt.setString(6, trimToNull(user.getEmail()));
        stmt.setString(7, trimToNull(user.getPhone()));
        stmt.setString(8, trimToNull(user.getAddress()));
        setSqlDate(stmt, 9, user.getHireDate());
        setNullableInt(stmt, 10, user.getDepartmentId());
        setNullableInt(stmt, 11, user.getPositionId());
        setNullableInt(stmt, 12, user.getManagerId());
        stmt.setBigDecimal(13, user.getBaseSalary());
        stmt.setString(14, trimToNull(user.getStatus()));
    }

    private void setSqlDate(PreparedStatement stmt, int index, java.util.Date value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.DATE);
            return;
        }
        stmt.setDate(index, new java.sql.Date(value.getTime()));
    }

    private void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.INTEGER);
            return;
        }
        stmt.setInt(index, value);
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private void rollbackQuietly() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void restoreAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException ignored) {
        }
    }
}
