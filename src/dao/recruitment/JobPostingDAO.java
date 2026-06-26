package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import dao.UserDAO;
import model.Recruitment.JobPosting;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobPostingDAO implements DAO<JobPosting> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(JobPosting jobPosting) {
        String sql = "INSERT INTO job_posting (title, description, requirement, quantity, salary, deadline, status, created_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, jobPosting.getTitle());
            stmt.setString(2, jobPosting.getDescription());
            stmt.setString(3, jobPosting.getRequirement());
            stmt.setInt(4, jobPosting.getQuantity());
            stmt.setDouble(5, jobPosting.getSalary());
            stmt.setDate(6, jobPosting.getDeadline());
            stmt.setString(7, jobPosting.getStatus());
            stmt.setDate(8, new Date(System.currentTimeMillis()));
            stmt.setInt(9, jobPosting.getCreatedBy().getUserId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) { jobPosting.setJobId(rs.getInt(1)); }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean insert(JobPosting jp) { return save(jp); }

    @Override
    public boolean update(JobPosting jp) {
        String sql = "UPDATE job_posting SET title=?, description=?, requirement=?, quantity=?, salary=?, deadline=?, status=? WHERE job_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jp.getTitle()); stmt.setString(2, jp.getDescription());
            stmt.setString(3, jp.getRequirement()); stmt.setInt(4, jp.getQuantity());
            stmt.setDouble(5, jp.getSalary()); stmt.setDate(6, jp.getDeadline());
            stmt.setString(7, jp.getStatus()); stmt.setInt(8, jp.getJobId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM job_posting WHERE job_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { stmt.setInt(1, id); return stmt.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public JobPosting findById(int id) {
        String sql = "SELECT * FROM job_posting WHERE job_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractJobPosting(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<JobPosting> findAll() {
        List<JobPosting> list = new ArrayList<>();
        String sql = "SELECT * FROM job_posting ORDER BY created_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractJobPosting(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<JobPosting> findOpeningJobs() {
        List<JobPosting> list = new ArrayList<>();
        String sql = "SELECT * FROM job_posting WHERE status='OPEN' AND (deadline IS NULL OR deadline >= CURDATE()) ORDER BY created_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractJobPosting(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private JobPosting extractJobPosting(ResultSet rs) throws SQLException {
        JobPosting jp = new JobPosting();
        jp.setJobId(rs.getInt("job_id")); jp.setTitle(rs.getString("title"));
        jp.setDescription(rs.getString("description")); jp.setRequirement(rs.getString("requirement"));
        jp.setQuantity(rs.getInt("quantity")); jp.setSalary(rs.getDouble("salary"));
        jp.setDeadline(rs.getDate("deadline")); jp.setStatus(rs.getString("status"));
        jp.setCreatedDate(rs.getDate("created_date"));
        UserDAO userDao = new UserDAO();
        jp.setCreatedBy(userDao.findById(rs.getInt("created_by")));
        return jp;
    }
}
