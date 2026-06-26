package dao.ProfileDao;

import dao.DatabaseConnection;
import enumModel.RoleEnum;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDao {


    private Connection connection = DatabaseConnection.getInstance().getConnection();


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("address"),
                        rs.getString("citizenIdentificationCard"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getDate("dateOfBirth"),
                        rs.getString("fullName"),
                        RoleEnum.valueOf(rs.getString("role")),
                        rs.getInt("id")
                );
                users.add(user);
            }
        }
        return users;
    }


    public void addUser(User user) throws SQLException {

        String sql = "INSERT INTO users (id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFullName());

            if (user.getDateOfBirth() != null) {
                ps.setDate(3, new Date(user.getDateOfBirth().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, user.getGender());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getCitizenIdentificationCard());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getRole().name());


            ps.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {

        String sql = "UPDATE users SET fullName = ?, dateOfBirth = ?, gender = ?, phone = ?, citizenIdentificationCard = ?, address = ?, role = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());

            if (user.getDateOfBirth() != null) {
                ps.setDate(2, new java.sql.Date(user.getDateOfBirth().getTime()));
            } else {
                ps.setNull(2, Types.DATE);
            }

            ps.setString(3, user.getGender());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getCitizenIdentificationCard());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole().name());

            ps.setLong(8, user.getId());

            int rowsAffected = ps.executeUpdate();


            if (rowsAffected > 0) {
                System.out.println("Cập nhật thành công user có ID: " + user.getId());
            } else {
                System.out.println("Không tìm thấy user nào có ID: " + user.getId() + " để cập nhật.");
            }
        }
    }
}