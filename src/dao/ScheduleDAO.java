package dao;

import model.ScheduleEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO implements DAO<ScheduleEntry> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(ScheduleEntry entry) {
        String sql = "INSERT INTO schedules (employee_id, date, shift_type, start_time, end_time, status, event_name, location) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entry.getEmployeeId());
            stmt.setString(2, entry.getDate());
            stmt.setString(3, entry.getShiftType());
            stmt.setString(4, entry.getStartTime());
            stmt.setString(5, entry.getEndTime());
            stmt.setString(6, entry.getStatus());
            stmt.setString(7, entry.getEventName());
            stmt.setString(8, entry.getLocation());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) entry.setScheduleId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ScheduleEntry entry) {
        String sql = "UPDATE schedules SET date=?, shift_type=?, start_time=?, end_time=?, status=?, event_name=?, location=? WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entry.getDate());
            stmt.setString(2, entry.getShiftType());
            stmt.setString(3, entry.getStartTime());
            stmt.setString(4, entry.getEndTime());
            stmt.setString(5, entry.getStatus());
            stmt.setString(6, entry.getEventName());
            stmt.setString(7, entry.getLocation());
            stmt.setInt(8, entry.getScheduleId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM schedules WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ScheduleEntry findById(int id) {
        String sql = "SELECT * FROM schedules WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractEntry(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ScheduleEntry> findAll() {
        List<ScheduleEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM schedules";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractEntry(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ScheduleEntry> findByEmployeeAndMonth(int employeeId, int month, int year) {
        List<ScheduleEntry> list = new ArrayList<>();
        // date stored as dd/MM/yyyy — use SUBSTRING for efficient month/year extraction
        String sql = "SELECT * FROM schedules WHERE employee_id=? AND SUBSTRING(date, 4, 2)=? AND SUBSTRING(date, 7, 4)=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractEntry(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ScheduleEntry extractEntry(ResultSet rs) throws SQLException {
        ScheduleEntry entry = new ScheduleEntry();
        entry.setScheduleId(rs.getInt("schedule_id"));
        entry.setEmployeeId(rs.getInt("employee_id"));
        entry.setDate(rs.getString("date"));
        entry.setShiftType(rs.getString("shift_type"));
        entry.setStartTime(rs.getString("start_time"));
        entry.setEndTime(rs.getString("end_time"));
        entry.setStatus(rs.getString("status"));
        entry.setEventName(rs.getString("event_name"));
        entry.setLocation(rs.getString("location"));
        return entry;
    }
}
