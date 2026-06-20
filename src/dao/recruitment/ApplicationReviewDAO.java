package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import dao.UserDAO;
import model.Recruitment.ApplicationReview;
import model.Recruitment.JobApplication;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReviewDAO implements DAO<ApplicationReview> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(ApplicationReview review) {
        String sql = "INSERT INTO application_review (application_id, reviewer_id, status, note, review_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, review.getApplication().getApplicationId());
            stmt.setInt(2, review.getReviewer().getUserId());
            stmt.setString(3, review.getStatus());
            stmt.setString(4, review.getNote());
            stmt.setDate(5, review.getReviewDate() != null ? Date.valueOf(review.getReviewDate()) : Date.valueOf(java.time.LocalDate.now()));
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ApplicationReview review) {
        String sql = "UPDATE application_review SET status=?, note=?, review_date=? WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, review.getStatus());
            stmt.setString(2, review.getNote());
            stmt.setDate(3, review.getReviewDate() != null ? Date.valueOf(review.getReviewDate()) : Date.valueOf(java.time.LocalDate.now()));
            stmt.setInt(4, review.getReviewId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM application_review WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ApplicationReview findById(int id) {
        String sql = "SELECT * FROM application_review WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractReview(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ApplicationReview> findAll() {
        List<ApplicationReview> list = new ArrayList<>();
        String sql = "SELECT * FROM application_review";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ApplicationReview findByApplication(int applicationId) {
        String sql = "SELECT * FROM application_review WHERE application_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractReview(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ApplicationReview extractReview(ResultSet rs) throws SQLException {
        ApplicationReview review = new ApplicationReview();
        review.setReviewId(rs.getInt("review_id"));
        // Lấy JobApplication
        JobApplicationDAO appDao = new JobApplicationDAO();
        JobApplication app = appDao.findById(rs.getInt("application_id"));
        review.setApplication(app);
        // Lấy reviewer (User)
        UserDAO userDao = new UserDAO();
        User reviewer = userDao.findById(rs.getInt("reviewer_id"));
        review.setReviewer(reviewer);
        review.setStatus(rs.getString("status"));
        review.setNote(rs.getString("note"));
        review.setReviewDate(rs.getDate("review_date").toLocalDate());
        return review;
    }
}