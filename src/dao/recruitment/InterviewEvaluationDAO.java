package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.InterviewEvaluation;
import model.Recruitment.InterviewSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewEvaluationDAO implements DAO<InterviewEvaluation> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(InterviewEvaluation evaluation) {
        String sql = "INSERT INTO interview_evaluation (schedule_id, score, comment, result, evaluation_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, evaluation.getSchedule().getScheduleId());
            stmt.setDouble(2, evaluation.getScore());
            stmt.setString(3, evaluation.getComment());
            stmt.setString(4, evaluation.getResult());
            stmt.setDate(5, new Date(System.currentTimeMillis()));
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    evaluation.setEvaluationId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(InterviewEvaluation evaluation) {
        String sql = "UPDATE interview_evaluation SET score=?, comment=?, result=? WHERE evaluation_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, evaluation.getScore());
            stmt.setString(2, evaluation.getComment());
            stmt.setString(3, evaluation.getResult());
            stmt.setInt(4, evaluation.getEvaluationId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM interview_evaluation WHERE evaluation_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public InterviewEvaluation findById(int id) {
        String sql = "SELECT * FROM interview_evaluation WHERE evaluation_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEvaluation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InterviewEvaluation> findBySchedule(int scheduleId) {
        List<InterviewEvaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_evaluation WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractEvaluation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<InterviewEvaluation> findAll() {
        List<InterviewEvaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_evaluation ORDER BY evaluation_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractEvaluation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private InterviewEvaluation extractEvaluation(ResultSet rs) throws SQLException {
        InterviewEvaluation evaluation = new InterviewEvaluation();
        evaluation.setEvaluationId(rs.getInt("evaluation_id"));

        InterviewScheduleDAO scheduleDao = new InterviewScheduleDAO();
        InterviewSchedule schedule = scheduleDao.findById(rs.getInt("schedule_id"));
        evaluation.setSchedule(schedule);

        evaluation.setScore(rs.getDouble("score"));
        evaluation.setComment(rs.getString("comment"));
        evaluation.setResult(rs.getString("result"));
        evaluation.setEvaluationDate(rs.getDate("evaluation_date"));
        return evaluation;
    }
}
