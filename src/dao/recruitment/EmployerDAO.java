package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import dao.UserDAO;
import model.Recruitment.Employer;
import model.Recruitment.JobPosting;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployerDAO implements DAO<Employer> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    private UserDAO userDAO = new UserDAO();

    @Override
    public boolean save(Employer employer) {
        // Employer là User, lưu vào bảng users trước
        return userDAO.save(employer);
    }

    @Override
    public boolean update(Employer employer) {
        return userDAO.update(employer);
    }

    @Override
    public boolean delete(int id) {
        return userDAO.delete(id);
    }

    @Override
    public Employer findById(int id) {
        User user = userDAO.findById(id);
        if (user != null && "EMPLOYER".equals(user.getRole())) {
            Employer emp = new Employer();
            emp.setUserId(user.getUserId());
            emp.setUsername(user.getUsername());
            emp.setPassword(user.getPassword());
            emp.setEmail(user.getEmail());
            emp.setRole(user.getRole());
            return emp;
        }
        return null;
    }

    @Override
    public List<Employer> findAll() {
        List<Employer> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='EMPLOYER'";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Employer emp = new Employer();
                emp.setUserId(rs.getInt("user_id"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                emp.setEmail(rs.getString("email"));
                emp.setRole(rs.getString("role"));
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy bản nháp tin tuyển dụng của Employer
    public JobPosting getDraftPosting(int employerId) {
        String sql = "SELECT * FROM job_posting WHERE employer_id=? AND status='DRAFT' ORDER BY created_date DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JobPosting jp = new JobPosting();
                jp.setPostId(rs.getInt("post_id"));
                jp.setTitle(rs.getString("title"));
                jp.setDescription(rs.getString("description"));
                jp.setRequiment(rs.getString("requirement"));
                jp.setDayEnd(rs.getDate("day_end"));
                jp.setSalary(rs.getDouble("salary"));
                jp.setStatus(rs.getString("status"));
                jp.setCreatedDate(rs.getDate("created_date"));
                return jp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}