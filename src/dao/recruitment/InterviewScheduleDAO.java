package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.Candidate;
import model.Recruitment.InterviewSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewScheduleDAO implements DAO<InterviewSchedule> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(InterviewSchedule schedule) {
        String sql = "INSERT INTO interview_schedule (candidate_id, interviewer, interview_date, location, note, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getCandidate().getCandidateId());
            stmt.setString(2, schedule.getInterviewer());
            stmt.setTimestamp(3, schedule.getInterviewDate());
            stmt.setString(4, schedule.getLocation());
            stmt.setString(5, schedule.getNote());
            stmt.setString(6, schedule.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) schedule.setScheduleId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean update(InterviewSchedule schedule) {
        String sql = "UPDATE interview_schedule SET interviewer=?, interview_date=?, location=?, note=?, status=? WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schedule.getInterviewer());
            stmt.setTimestamp(2, schedule.getInterviewDate());
            stmt.setString(3, schedule.getLocation());
            stmt.setString(4, schedule.getNote());
            stmt.setString(5, schedule.getStatus());
            stmt.setInt(6, schedule.getScheduleId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM interview_schedule WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { stmt.setInt(1, id); return stmt.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public InterviewSchedule findById(int id) {
        String sql = "SELECT * FROM interview_schedule WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractSchedule(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<InterviewSchedule> findAll() {
        List<InterviewSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_schedule ORDER BY interview_date DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractSchedule(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<InterviewSchedule> findByStatus(String status) {
        List<InterviewSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_schedule WHERE status=? ORDER BY interview_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractSchedule(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<InterviewSchedule> findByCandidate(int candidateId) {
        List<InterviewSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_schedule WHERE candidate_id=? ORDER BY interview_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, candidateId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractSchedule(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private InterviewSchedule extractSchedule(ResultSet rs) throws SQLException {
        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        CandidateDAO candidateDao = new CandidateDAO();
        schedule.setCandidate(candidateDao.findById(rs.getInt("candidate_id")));
        schedule.setInterviewer(rs.getString("interviewer"));
        schedule.setInterviewDate(rs.getTimestamp("interview_date"));
        schedule.setLocation(rs.getString("location"));
        schedule.setNote(rs.getString("note"));
        schedule.setStatus(rs.getString("status"));
        return schedule;
    }
}
