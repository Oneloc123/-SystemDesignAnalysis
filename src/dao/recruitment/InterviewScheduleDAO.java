package dao.recruitment;

import dao.DAO;
import dao.DatabaseConnection;
import model.Recruitment.InterviewSchedule;
import model.Recruitment.JobApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewScheduleDAO implements DAO<InterviewSchedule> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(InterviewSchedule schedule) {
        String sql = "INSERT INTO interview_schedule (application_id, interview_time, location, format, interviewer, note, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getApplication().getApplicationId());
            stmt.setTimestamp(2, Timestamp.valueOf(schedule.getInterviewTime()));
            stmt.setString(3, schedule.getLocation());
            stmt.setString(4, schedule.getFormat());
            stmt.setString(5, schedule.getInterviewer());
            stmt.setString(6, schedule.getNote());
            stmt.setString(7, schedule.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    schedule.setInterviewId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveDraft(InterviewSchedule draft) {
        if (draft.getInterviewId() == 0) {
            return save(draft);
        } else {
            return update(draft);
        }
    }

    @Override
    public boolean update(InterviewSchedule schedule) {
        String sql = "UPDATE interview_schedule SET interview_time=?, location=?, format=?, interviewer=?, note=?, status=? WHERE interview_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(schedule.getInterviewTime()));
            stmt.setString(2, schedule.getLocation());
            stmt.setString(3, schedule.getFormat());
            stmt.setString(4, schedule.getInterviewer());
            stmt.setString(5, schedule.getNote());
            stmt.setString(6, schedule.getStatus());
            stmt.setInt(7, schedule.getInterviewId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM interview_schedule WHERE interview_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public InterviewSchedule findById(int id) {
        String sql = "SELECT * FROM interview_schedule WHERE interview_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractSchedule(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InterviewSchedule> findAll() {
        List<InterviewSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_schedule";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractSchedule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public InterviewSchedule findDraftByApplication(int applicationId) {
        String sql = "SELECT * FROM interview_schedule WHERE application_id=? AND status='DRAFT' ORDER BY interview_id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractSchedule(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InterviewSchedule> findByApplication(int applicationId) {
        List<InterviewSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM interview_schedule WHERE application_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractSchedule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private InterviewSchedule extractSchedule(ResultSet rs) throws SQLException {
        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setInterviewId(rs.getInt("interview_id"));
        // Lấy JobApplication
        JobApplicationDAO appDao = new JobApplicationDAO();
        JobApplication app = appDao.findById(rs.getInt("application_id"));
        schedule.setApplication(app);
        schedule.setInterviewTime(rs.getTimestamp("interview_time").toLocalDateTime());
        schedule.setLocation(rs.getString("location"));
        schedule.setFormat(rs.getString("format"));
        schedule.setInterviewer(rs.getString("interviewer"));
        schedule.setNote(rs.getString("note"));
        schedule.setStatus(rs.getString("status"));
        return schedule;
    }
}