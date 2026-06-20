package model.calcSalary;

import dao.AttendanceDAO;
import java.util.ArrayList;
import java.util.List;

public class AttendancePeriod {
    private long id;
    private int month;
    private int year;

    private List<AttendanceDetail> attendanceDetails;

    private static AttendanceDAO dao = new AttendanceDAO();

    public AttendancePeriod() {
        attendanceDetails = new ArrayList<>();
    }


    public List<AttendanceDetail> getAbnormalDetails() {
        List<AttendanceDetail> result = new ArrayList<>();
        for (AttendanceDetail d : attendanceDetails) {
            if (d.isAbnormal()) result.add(d);
        }
        return result;
    }

    public int getTotalWorkingDays() {
        int total = 0;
        for (AttendanceDetail d : attendanceDetails) {
            total += d.getActualWorkingDays();
        }
        return total;
    }

    public int getTotalOvertime() {
        int total = 0;
        for (AttendanceDetail d : attendanceDetails) {
            total += d.getOvertimeHours();
        }
        return total;
    }

    public String getPeriodLabel() {
        return String.format("%02d/%d", month, year);
    }


    public boolean save() { return dao.save(this); }
    public boolean update() { return dao.update(this); }

    public static AttendancePeriod findByMonth(int month, int year) {
        return dao.findByMonth(month, year);
    }

    public static AttendancePeriod getByDepartment(int deptId, int month, int year) {
        return dao.getByDepartment(deptId, month, year);
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public List<AttendanceDetail> getAttendanceDetails() { return attendanceDetails; }
    public void setAttendanceDetails(List<AttendanceDetail> attendanceDetails) { this.attendanceDetails = attendanceDetails; }
    public void addDetail(AttendanceDetail detail) {
        if (detail != null) {
            attendanceDetails.add(detail);
            detail.setPeriodId(this.id);
        }
    }
}
