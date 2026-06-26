package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import dao.UserDAO;
import model.Recruitment.ApplicationReview;
import model.Recruitment.Candidate;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReviewDAO implements DAO<ApplicationReview> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(ApplicationReview review) {
        String sql = "INSERT INTO application_review (candidate_id, reviewer_id, score, comment, result, review_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, review.getCandidate().getCandidateId());
            stmt.setInt(2, (int) review.getReviewer().getUserId());
            stmt.setDouble(3, review.getScore());
            stmt.setString(4, review.getComment());
            stmt.setString(5, review.getResult());
            stmt.setDate(6, new Date(System.currentTimeMillis()));
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) review.setReviewId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean update(ApplicationReview review) {
        String sql = "UPDATE application_review SET score=?, comment=?, result=? WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, review.getScore()); stmt.setString(2, review.getComment());
            stmt.setString(3, review.getResult()); stmt.setInt(4, review.getReviewId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM application_review WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { stmt.setInt(1, id); return stmt.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public ApplicationReview findById(int id) {
        String sql = "SELECT * FROM application_review WHERE review_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractReview(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<ApplicationReview> findByCandidate(int candidateId) {
        List<ApplicationReview> list = new ArrayList<>();
        String sql = "SELECT * FROM application_review WHERE candidate_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractReview(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<ApplicationReview> findAll() {
        List<ApplicationReview> list = new ArrayList<>();
        String sql = "SELECT * FROM application_review ORDER BY review_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractReview(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private ApplicationReview extractReview(ResultSet rs) throws SQLException {
        ApplicationReview review = new ApplicationReview();
        review.setReviewId(rs.getInt("review_id"));
        CandidateDAO candidateDao = new CandidateDAO();
        review.setCandidate(candidateDao.findById(rs.getInt("candidate_id")));
        UserDAO userDao = new UserDAO();
        review.setReviewer(userDao.findById(rs.getInt("reviewer_id")));
        review.setScore(rs.getDouble("score"));
        review.setComment(rs.getString("comment"));
        review.setResult(rs.getString("result"));
        review.setReviewDate(rs.getDate("review_date"));
        return review;
    }
}
