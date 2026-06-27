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
                "actual_working_days, standard_days, overtime_hours, " +
                "unpaid_leave, paid_leave, status, basic_salary, allowance, dependent_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, detail.getPeriodId());
            stmt.setLong(2, detail.getEmployeeId());
            stmt.setString(3, detail.getEmployeeCode());
            stmt.setString(4, detail.getEmployeeName());
            stmt.setInt(5, detail.getActualWorkingDays());
            stmt.setInt(6, detail.getStandardDays());
            stmt.setInt(7, detail.getOvertimeHours());
            stmt.setInt(8, detail.getUnpaidLeave());
            stmt.setInt(9, detail.getPaidLeave());
            stmt.setString(10, detail.getStatus());
            stmt.setDouble(11, detail.getBasicSalary());
            stmt.setDouble(12, detail.getAllowance());
            stmt.setInt(13, detail.getDependentNumber());
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
        // NOTE: Không còn fallback createDefaultPeriod — dữ liệu phải có thật trong DB
        return findByMonth(month, year);
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

    public List<AttendancePeriod> findAll() {
        List<AttendancePeriod> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance_periods ORDER BY year DESC, month DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                AttendancePeriod period = extractPeriod(rs);
                period.setAttendanceDetails(findDetailsByPeriod(period.getId()));
                list.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean saveDetailOnly(AttendanceDetail detail) {
        String sql = "INSERT INTO attendance_details (period_id, employee_id, employee_code, employee_name, " +
                "actual_working_days, standard_days, overtime_hours, " +
                "unpaid_leave, paid_leave, status, basic_salary, allowance, dependent_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, detail.getPeriodId());
            stmt.setLong(2, detail.getEmployeeId());
            stmt.setString(3, detail.getEmployeeCode());
            stmt.setString(4, detail.getEmployeeName());
            stmt.setInt(5, detail.getActualWorkingDays());
            stmt.setInt(6, detail.getStandardDays());
            stmt.setInt(7, detail.getOvertimeHours());
            stmt.setInt(8, detail.getUnpaidLeave());
            stmt.setInt(9, detail.getPaidLeave());
            stmt.setString(10, detail.getStatus());
            stmt.setDouble(11, detail.getBasicSalary());
            stmt.setDouble(12, detail.getAllowance());
            stmt.setInt(13, detail.getDependentNumber());
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

        d.setUnpaidLeave(rs.getInt("unpaid_leave"));
        d.setPaidLeave(rs.getInt("paid_leave"));
        d.setStatus(rs.getString("status"));
        d.setBasicSalary(rs.getDouble("basic_salary"));
        d.setAllowance(rs.getDouble("allowance"));
        d.setDependentNumber(rs.getInt("dependent_number"));
        return d;
    }
}
