package dao;

import model.calcSalary.AttendanceDetail;
import model.calcSalary.AttendancePeriod;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public boolean save(AttendancePeriod period) {
        String sql = "INSERT INTO attendance_periods (month, year) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, period.getMonth());
            stmt.setInt(2, period.getYear());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    period.setId(rs.getLong(1));
                    for (AttendanceDetail d : period.getAttendanceDetails()) {
                        d.setPeriodId(period.getId());
                        saveDetail(d);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(AttendancePeriod period) {
        String sql = "UPDATE attendance_periods SET month=?, year=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, period.getMonth());
            stmt.setInt(2, period.getYear());
            stmt.setLong(3, period.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveDetail(AttendanceDetail detail) {
        String sql = "INSERT INTO attendance_details (period_id, employee_id, employee_code, employee_name, " +
                "actual_working_days, standard_days, overtime_hours, late_count, early_count, " +
                "unpaid_leave, paid_leave, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, detail.getPeriodId());
            stmt.setLong(2, detail.getEmployeeId());
            stmt.setString(3, detail.getEmployeeCode());
            stmt.setString(4, detail.getEmployeeName());
            stmt.setInt(5, detail.getActualWorkingDays());
            stmt.setInt(6, detail.getStandardDays());
            stmt.setInt(7, detail.getOvertimeHours());
            stmt.setInt(8, detail.getLateCount());
            stmt.setInt(9, detail.getEarlyCount());
            stmt.setInt(10, detail.getUnpaidLeave());
            stmt.setInt(11, detail.getPaidLeave());
            stmt.setString(12, detail.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) detail.setId(rs.getLong(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public AttendancePeriod findByMonth(int month, int year) {
        String sql = "SELECT * FROM attendance_periods WHERE month=? AND year=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AttendancePeriod period = extractPeriod(rs);
                period.setAttendanceDetails(findDetailsByPeriod(period.getId()));
                return period;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttendancePeriod getByDepartment(int deptId, int month, int year) {
        AttendancePeriod period = findByMonth(month, year);
        if (period == null) {
            period = createDefaultPeriod(month, year, deptId);
        }
        return period;
    }

    private AttendancePeriod createDefaultPeriod(int month, int year, int deptId) {
        AttendancePeriod period = new AttendancePeriod();
        period.setMonth(month);
        period.setYear(year);

        if (deptId == 1) {
            period.addDetail(createDetail(1, "NV001", "Nguyễn Văn Lộc", 22, 26, 10, 1, 0, 1, 0, "Đã chốt"));
            period.addDetail(createDetail(3, "NV003", "Lê Đình Cương", 22, 26, 15, 0, 0, 0, 0, "Đã chốt"));
        } else if (deptId == 2) {
            period.addDetail(createDetail(2, "NV002", "Trần Thị Ánh", 20, 26, 5, 2, 1, 2, 1, "Đã chốt"));
            period.addDetail(createDetail(5, "NV005", "Hoàng Văn Nam", 18, 26, 0, 3, 3, 0, 2, "Chưa chốt"));
        } else if (deptId == 3) {
            period.addDetail(createDetail(4, "NV004", "Phạm Thị Hoa", 22, 26, 8, 0, 2, 1, 0, "Đã chốt"));
        }

        return period;
    }

    private AttendanceDetail createDetail(long empId, String code, String name, int workDays, int standard,
                                          int ot, int late, int early, int unpaid, int paid, String status) {
        AttendanceDetail d = new AttendanceDetail();
        d.setEmployeeId(empId);
        d.setEmployeeCode(code);
        d.setEmployeeName(name);
        d.setActualWorkingDays(workDays);
        d.setStandardDays(standard);
        d.setOvertimeHours(ot);
        d.setLateCount(late);
        d.setEarlyCount(early);
        d.setUnpaidLeave(unpaid);
        d.setPaidLeave(paid);
        d.setStatus(status);
        return d;
    }

    private List<AttendanceDetail> findDetailsByPeriod(long periodId) {
        List<AttendanceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance_details WHERE period_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, periodId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractDetail(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private AttendancePeriod extractPeriod(ResultSet rs) throws SQLException {
        AttendancePeriod period = new AttendancePeriod();
        period.setId(rs.getLong("id"));
        period.setMonth(rs.getInt("month"));
        period.setYear(rs.getInt("year"));
        return period;
    }

    private AttendanceDetail extractDetail(ResultSet rs) throws SQLException {
        AttendanceDetail d = new AttendanceDetail();
        d.setId(rs.getLong("id"));
        d.setPeriodId(rs.getLong("period_id"));
        d.setEmployeeId(rs.getLong("employee_id"));
        d.setEmployeeCode(rs.getString("employee_code"));
        d.setEmployeeName(rs.getString("employee_name"));
        d.setActualWorkingDays(rs.getInt("actual_working_days"));
        d.setStandardDays(rs.getInt("standard_days"));
        d.setOvertimeHours(rs.getInt("overtime_hours"));
        d.setLateCount(rs.getInt("late_count"));
        d.setEarlyCount(rs.getInt("early_count"));
        d.setUnpaidLeave(rs.getInt("unpaid_leave"));
        d.setPaidLeave(rs.getInt("paid_leave"));
        d.setStatus(rs.getString("status"));
        return d;
    }
}
