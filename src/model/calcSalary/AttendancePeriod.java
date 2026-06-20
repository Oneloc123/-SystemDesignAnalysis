package model.calcSalary;

import java.util.ArrayList;
import java.util.List;

public class AttendancePeriod {
    private long id;
    private int month, year;

    private List<AttendanceDetail> attendanceDetails;

    public AttendancePeriod() {
        attendanceDetails = new ArrayList<AttendanceDetail>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<AttendanceDetail> getAttendanceDetails() {
        return attendanceDetails;
    }

    public void setAttendanceDetails(List<AttendanceDetail> attendanceDetails) {
        this.attendanceDetails = attendanceDetails;
    }

    public void addAttendanceDetail(AttendanceDetail detail) {
        this.attendanceDetails.add(detail);
    }
}
