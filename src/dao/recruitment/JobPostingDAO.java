package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.JobPosting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobPostingDAO implements DAO<JobPosting> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(JobPosting jobPosting) {
        String sql = "INSERT INTO job_posting (title, description, requirement, day_end, salary, employer_id, created_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, jobPosting.getTitle());
            stmt.setString(2, jobPosting.getDescription());
            stmt.setString(3, jobPosting.getRequiment());
            stmt.setDate(4, jobPosting.getDayEnd());
            stmt.setDouble(5, jobPosting.getSalary());
            stmt.setInt(6, jobPosting.getEmployer().getUserId());
            stmt.setDate(7, new Date(System.currentTimeMillis()));
            stmt.setString(8, jobPosting.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    jobPosting.setPostId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveDraft(JobPosting draft) {
        if (draft.getPostId() == 0) {
            return save(draft);
        } else {
            return update(draft);
        }
    }

    @Override
    public boolean update(JobPosting jobPosting) {
        String sql = "UPDATE job_posting SET title=?, description=?, requirement=?, day_end=?, salary=?, status=? WHERE post_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jobPosting.getTitle());
            stmt.setString(2, jobPosting.getDescription());
            stmt.setString(3, jobPosting.getRequiment());
            stmt.setDate(4, jobPosting.getDayEnd());
            stmt.setDouble(5, jobPosting.getSalary());
            stmt.setString(6, jobPosting.getStatus());
            stmt.setInt(7, jobPosting.getPostId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM job_posting WHERE post_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JobPosting findById(int id) {
        String sql = "SELECT * FROM job_posting WHERE post_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractJobPosting(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JobPosting> findAll() {
        List<JobPosting> list = new ArrayList<>();
        String sql = "SELECT * FROM job_posting";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractJobPosting(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<JobPosting> findByEmployer(int employerId) {
        List<JobPosting> list = new ArrayList<>();
        String sql = "SELECT * FROM job_posting WHERE employer_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractJobPosting(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private JobPosting extractJobPosting(ResultSet rs) throws SQLException {
        JobPosting jp = new JobPosting();
        jp.setPostId(rs.getInt("post_id"));
        jp.setTitle(rs.getString("title"));
        jp.setDescription(rs.getString("description"));
        jp.setRequiment(rs.getString("requirement"));
        jp.setDayEnd(rs.getDate("day_end"));
        jp.setSalary(rs.getDouble("salary"));
        jp.setStatus(rs.getString("status"));
        jp.setCreatedDate(rs.getDate("created_date"));
        // Lấy Employer từ employer_id (có thể set sau)
        EmployerDAO empDao = new EmployerDAO();
        jp.setEmployer(empDao.findById(rs.getInt("employer_id")));
        return jp;
    }
}