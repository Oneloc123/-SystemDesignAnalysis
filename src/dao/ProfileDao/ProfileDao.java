package dao.ProfileDao;

import dao.DatabaseConnection;
import model.profile.Profile;
import enumModel.ProfileStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDao {


    private Connection connection = DatabaseConnection.getInstance().getConnection();


    public List<Profile> getAllUsers() throws SQLException {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role, status FROM profile";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                profiles.add(mapProfile(rs));
            }
        }
        return profiles;
    }

    public List<Profile> getAllActive() throws SQLException {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role, status FROM profile WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ProfileStatus.WORKING.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                profiles.add(mapProfile(rs));
            }
        }
        return profiles;
    }

    public List<Profile> search(String keyword) throws SQLException {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role, status FROM profile WHERE fullName LIKE ? OR citizenIdentificationCard LIKE ? OR phone LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                profiles.add(mapProfile(rs));
            }
        }
        return profiles;
    }

    public Profile getById(long id) throws SQLException {
        String sql = "SELECT id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role, status FROM profile WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapProfile(rs);
            }
        }
        return null;
    }


        public void addUser(Profile profile) throws SQLException {
        String sql = "INSERT INTO profile (id, fullName, dateOfBirth, gender, phone, citizenIdentificationCard, address, role, status) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, profile.getId());
            ps.setString(2, profile.getFullName());
            if (profile.getDateOfBirth() != null) {
                ps.setDate(3, new java.sql.Date(profile.getDateOfBirth().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setString(4, profile.getGender());
            ps.setString(5, profile.getPhone());
            ps.setString(6, profile.getCitizenIdentificationCard());
            ps.setString(7, profile.getAddress());
            ps.setString(8, profile.getRole());
            ps.setString(9, profile.getStatus() != null ? profile.getStatus().name() : ProfileStatus.WORKING.name());
            ps.executeUpdate();
        }
    }

    public void updateUser(Profile profile) throws SQLException {
        String sql = "UPDATE profile SET fullName = ?, dateOfBirth = ?, gender = ?, phone = ?, citizenIdentificationCard = ?, address = ?, role = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, profile.getFullName());
            if (profile.getDateOfBirth() != null) {
                ps.setDate(2, new java.sql.Date(profile.getDateOfBirth().getTime()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setString(3, profile.getGender());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getCitizenIdentificationCard());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getRole());
            ps.setString(8, profile.getStatus() != null ? profile.getStatus().name() : ProfileStatus.WORKING.name());
            ps.setLong(9, profile.getId());
            ps.executeUpdate();
        }
    }

    public boolean checkDuplicate(String citizenIdentificationCard, String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM profile WHERE citizenIdentificationCard = ? OR phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, citizenIdentificationCard);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean checkDuplicateForUpdate(long id, String citizenIdentificationCard, String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM profile WHERE (citizenIdentificationCard = ? OR phone = ?) AND id != ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, citizenIdentificationCard);
            ps.setString(2, phone);
            ps.setLong(3, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public long getMaxId() throws SQLException {
        String sql = "SELECT MAX(id) FROM profile";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    public void updateStatus(long id, ProfileStatus status) throws SQLException {
        String sql = "UPDATE profile SET status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private Profile mapProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getLong("id"));
        profile.setFullName(rs.getString("fullName"));
        profile.setDateOfBirth(rs.getDate("dateOfBirth"));
        profile.setGender(rs.getString("gender"));
        profile.setPhone(rs.getString("phone"));
        profile.setCitizenIdentificationCard(rs.getString("citizenIdentificationCard"));
        profile.setAddress(rs.getString("address"));
        profile.setRole(rs.getString("role"));
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            profile.setStatus(ProfileStatus.valueOf(statusStr));
        } else {
            profile.setStatus(ProfileStatus.WORKING);
        }
        return profile;
    }
}