package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import model.Recruitment.Candidate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAO implements DAO<JobApplication> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(JobApplication application) {
        String sql = "INSERT INTO job_application (job_posting_id, candidate_id, full_name, email, phone, cover_letter, cv_file_path, status, submitted_date, is_draft) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, application.getJobPosting().getPostId());
            stmt.setInt(2, application.getCandidate().getUserId());
            stmt.setString(3, application.getFullName());
            stmt.setString(4, application.getEmail());
            stmt.setString(5, application.getPhone());
            stmt.setString(6, application.getCoverLetter());
            stmt.setString(7, application.getCvFilePath());
            stmt.setString(8, application.getStatus());
            stmt.setDate(9, application.getSubmittedDate() != null ? Date.valueOf(application.getSubmittedDate()) : null);
            stmt.setBoolean(10, application.isDraft());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    application.setApplicationId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveDraft(JobApplication draft) {
        if (draft.getApplicationId() == 0) {
            return save(draft);
        } else {
            return update(draft);
        }
    }

    @Override
    public boolean update(JobApplication application) {
        String sql = "UPDATE job_application SET job_posting_id=?, candidate_id=?, full_name=?, email=?, phone=?, cover_letter=?, cv_file_path=?, status=?, submitted_date=?, is_draft=? WHERE application_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, application.getJobPosting().getPostId());
            stmt.setInt(2, application.getCandidate().getUserId());
            stmt.setString(3, application.getFullName());
            stmt.setString(4, application.getEmail());
            stmt.setString(5, application.getPhone());
            stmt.setString(6, application.getCoverLetter());
            stmt.setString(7, application.getCvFilePath());
            stmt.setString(8, application.getStatus());
            stmt.setDate(9, application.getSubmittedDate() != null ? Date.valueOf(application.getSubmittedDate()) : null);
            stmt.setBoolean(10, application.isDraft());
            stmt.setInt(11, application.getApplicationId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM job_application WHERE application_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JobApplication findById(int id) {
        String sql = "SELECT * FROM job_application WHERE application_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractApplication(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JobApplication> findAll() {
        List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM job_application";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<JobApplication> findByJobPosting(int jobPostingId) {
        List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM job_application WHERE job_posting_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobPostingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<JobApplication> findByCandidate(int candidateId) {
        List<JobApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM job_application WHERE candidate_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private JobApplication extractApplication(ResultSet rs) throws SQLException {
        JobApplication app = new JobApplication();
        app.setApplicationId(rs.getInt("application_id"));
        // Lấy JobPosting
        JobPostingDAO jpDao = new JobPostingDAO();
        JobPosting jp = jpDao.findById(rs.getInt("job_posting_id"));
        app.setJobPosting(jp);
        // Lấy Candidate
        CandidateDAO candDao = new CandidateDAO();
        Candidate cand = candDao.findById(rs.getInt("candidate_id"));
        app.setCandidate(cand);
        app.setFullName(rs.getString("full_name"));
        app.setEmail(rs.getString("email"));
        app.setPhone(rs.getString("phone"));
        app.setCoverLetter(rs.getString("cover_letter"));
        app.setCvFilePath(rs.getString("cv_file_path"));
        app.setStatus(rs.getString("status"));
        app.setSubmittedDate(rs.getDate("submitted_date") != null ? rs.getDate("submitted_date").toLocalDate() : null);
        app.setDraft(rs.getBoolean("is_draft"));
        return app;
    }
}