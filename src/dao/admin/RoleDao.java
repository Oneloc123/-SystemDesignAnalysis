package dao.admin;

import model.admin.Role;
import dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    private final Connection connection;

    public RoleDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_code, role_name FROM roles ORDER BY role_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRoleCode(rs.getString("role_code"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
            }
        }
        return roles;
    }
}