package controller.profileManagement;

import controller.ScreenManager;
import enumModel.RoleEnum;
import model.Department;
import model.calcSalary.AttendanceDetail;
import model.calcSalary.AttendancePeriod;
import view.managerView.AttendanceView;
import java.util.List;

public class AttendanceController {
    private AttendanceView attendanceView;
    private int selectedDeptId;
    private int currentMonth;
    private int currentYear;
    private boolean abnormalOnly = false;

    public AttendanceController() {
        this.attendanceView = new AttendanceView(this);
        java.time.LocalDate now = java.time.LocalDate.now();
        this.currentMonth = now.getMonthValue();
        this.currentYear = now.getYear();
    }

    public void showOn() {
        attendanceView.showDeptSelection();
    }

    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null
            && ScreenManager.getCurrentUser().getRole() == RoleEnum.MANAGER;
    }

    public List<Department> getDepartments() {
        return Department.findAll();
    }

    public boolean selectDepartment(int id) {
        Department dept = Department.findById(id);
        if (dept != null) {
            selectedDeptId = id;
            abnormalOnly = false;
            return true;
        }
        return false;
    }

    public AttendancePeriod getAttendanceData() {
        return AttendancePeriod.getByDepartment(selectedDeptId, currentMonth, currentYear);
    }

    public List<AttendanceDetail> getFilteredDetails() {
        AttendancePeriod period = getAttendanceData();
        if (period == null) return java.util.Collections.emptyList();
        if (abnormalOnly) return period.getAbnormalDetails();
        return period.getAttendanceDetails();
    }

    public String getAttendanceTable() {
        List<AttendanceDetail> details = getFilteredDetails();
        AttendancePeriod period = getAttendanceData();
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== BẢNG CHẤM CÔNG ==========\n");
        Department dept = Department.findById(selectedDeptId);
        String deptName = (dept != null) ? dept.getName() : "";
        sb.append("Phòng: ").append(deptName).append(" | Kỳ: ").append(period != null ? period.getPeriodLabel() : "").append("\n");
        if (abnormalOnly) sb.append(" [Đang lọc: Nhân viên bất thường]\n");
        sb.append("\n");                sb.append(String.format("%-4s %-8s %-20s %-6s %-6s %-6s %-6s %-6s %-6s %-6s %-10s",
                "STT", "Mã NV", "Họ tên", "Công", "Chuẩn", "OT", "Muộn", "Sớm", "Nghỉ PL", "Nghỉ KL", "Đánh giá"));
        sb.append("\n----------------------------------------------------------------------------------------------------\n");
        int i = 1;
        int totalWork = 0, totalOT = 0, totalUnpaid = 0, totalPaid = 0;
        for (AttendanceDetail d : details) {
            String label = d.getAssessment();
            sb.append(String.format("%-4d %-8s %-20s %-6d %-6d %-6d %-6d %-6d %-6d %-6d %-10s",
                    i++, d.getEmployeeCode(), d.getEmployeeName(),
                    d.getActualWorkingDays(), d.getStandardDays(), d.getOvertimeHours(),
                    d.getLateCount(), d.getEarlyCount(),
                    d.getPaidLeave(), d.getUnpaidLeave(), label));
            sb.append("\n");
            totalWork += d.getActualWorkingDays();
            totalOT += d.getOvertimeHours();
            totalUnpaid += d.getUnpaidLeave();
            totalPaid += d.getPaidLeave();
        }
        sb.append("----------------------------------------------------------------------------------------------------\n");
        sb.append("Tổng: ").append(details.size()).append(" nhân viên | Công: ").append(totalWork)
                .append(" | OT: ").append(totalOT).append("h")
                .append(" | NP: ").append(totalPaid).append(" | NK: ").append(totalUnpaid).append("\n");
        return sb.toString();
    }

    public String getDetail(int index) {
        List<AttendanceDetail> details = getFilteredDetails();
        if (index < 1 || index > details.size()) return "Không hợp lệ.";
        AttendanceDetail d = details.get(index - 1);
        return "Chi tiết chấm công: " + d.getEmployeeName() + " (" + d.getEmployeeCode() + ")\n"
             + "Ngày công thực tế: " + d.getActualWorkingDays() + "/" + d.getStandardDays() + "\n"
             + "Giờ làm thêm: " + d.getOvertimeHours() + " giờ\n"
             + "Đi muộn: " + d.getLateCount() + " lần\n"
             + "Về sớm: " + d.getEarlyCount() + " lần\n"
             + "Nghỉ không lương: " + d.getUnpaidLeave() + " ngày\n"
             + "Nghỉ phép: " + d.getPaidLeave() + " ngày\n"
             + "Trạng thái: " + d.getStatus() + "\n";
    }

    public boolean setMonth(int month, int year) {
        if (month < 1 || month > 12) return false;
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate selected = java.time.LocalDate.of(year, month, 1);
        java.time.LocalDate minDate = now.minusMonths(12).withDayOfMonth(1);
        if (selected.isBefore(minDate) || selected.isAfter(now)) return false;
        this.currentMonth = month;
        this.currentYear = year;
        return true;
    }

    public void toggleAbnormalFilter() { abnormalOnly = !abnormalOnly; }
    public boolean isAbnormalOnly() { return abnormalOnly; }
    public int getSelectedDeptId() { return selectedDeptId; }
    public int getCurrentMonth() { return currentMonth; }
    public int getCurrentYear() { return currentYear; }
}
