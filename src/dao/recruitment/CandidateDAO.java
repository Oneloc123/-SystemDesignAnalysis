package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO implements DAO<Candidate> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(Candidate candidate) {
        String sql = "INSERT INTO candidate (full_name, gender, birthday, phone, email, address, education, experience, cv_file, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, candidate.getFullName());
            stmt.setString(2, candidate.getGender());
            stmt.setDate(3, candidate.getBirthday());
            stmt.setString(4, candidate.getPhone());
            stmt.setString(5, candidate.getEmail());
            stmt.setString(6, candidate.getAddress());
            stmt.setString(7, candidate.getEducation());
            stmt.setString(8, candidate.getExperience());
            stmt.setString(9, candidate.getCvFile());
            stmt.setString(10, candidate.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) candidate.setCandidateId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean update(Candidate candidate) {
        String sql = "UPDATE candidate SET full_name=?, gender=?, birthday=?, phone=?, email=?, address=?, education=?, experience=?, cv_file=?, status=? WHERE candidate_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, candidate.getFullName());
            stmt.setString(2, candidate.getGender());
            stmt.setDate(3, candidate.getBirthday());
            stmt.setString(4, candidate.getPhone());
            stmt.setString(5, candidate.getEmail());
            stmt.setString(6, candidate.getAddress());
            stmt.setString(7, candidate.getEducation());
            stmt.setString(8, candidate.getExperience());
            stmt.setString(9, candidate.getCvFile());
            stmt.setString(10, candidate.getStatus());
            stmt.setInt(11, candidate.getCandidateId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM candidate WHERE candidate_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Candidate findById(int id) {
        String sql = "SELECT * FROM candidate WHERE candidate_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractCandidate(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Candidate> findByStatus(String status) {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM candidate WHERE status=? ORDER BY created_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractCandidate(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Candidate> findAll() {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM candidate ORDER BY created_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractCandidate(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Candidate extractCandidate(ResultSet rs) throws SQLException {
        Candidate candidate = new Candidate();
        candidate.setCandidateId(rs.getInt("candidate_id"));
        candidate.setFullName(rs.getString("full_name"));
        candidate.setGender(rs.getString("gender"));
        candidate.setBirthday(rs.getDate("birthday"));
        candidate.setPhone(rs.getString("phone"));
        candidate.setEmail(rs.getString("email"));
        candidate.setAddress(rs.getString("address"));
        candidate.setEducation(rs.getString("education"));
        candidate.setExperience(rs.getString("experience"));
        candidate.setCvFile(rs.getString("cv_file"));
        candidate.setStatus(rs.getString("status"));
        return candidate;
    }
}
