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
}
