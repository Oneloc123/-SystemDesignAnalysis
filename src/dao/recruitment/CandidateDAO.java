package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import dao.UserDAO;
import model.Recruitment.Candidate;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO implements DAO<Candidate> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    private UserDAO userDAO = new UserDAO();

    @Override
    public boolean save(Candidate candidate) {
        return userDAO.save(candidate);
    }

    @Override
    public boolean update(Candidate candidate) {
        return userDAO.update(candidate);
    }

    @Override
    public boolean delete(int id) {
        return userDAO.delete(id);
    }

    @Override
    public Candidate findById(int id) {
        User user = userDAO.findById(id);
        if (user != null && "CANDIDATE".equals(user.getRole())) {
            Candidate cand = new Candidate();
            cand.setUserId(user.getUserId());
            cand.setUsername(user.getUsername());
            cand.setPassword(user.getPassword());
            cand.setEmail(user.getEmail());
            cand.setRole(user.getRole());
            return cand;
        }
        return null;
    }

    @Override
    public List<Candidate> findAll() {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='CANDIDATE'";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Candidate cand = new Candidate();
                cand.setUserId(rs.getInt("user_id"));
                cand.setUsername(rs.getString("username"));
                cand.setPassword(rs.getString("password"));
                cand.setEmail(rs.getString("email"));
                cand.setRole(rs.getString("role"));
                list.add(cand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}