package controller;

import controller.base.Controller;
import view.AttendanceView;
import java.util.ArrayList;
import java.util.List;

public class AttendanceController extends Controller {
    AttendanceView attendanceView;
    private int selectedDeptId;
    private String currentPeriod = "06/2024";
    private boolean abnormalOnly = false;

    public AttendanceController() {
        this.attendanceView = new AttendanceView(this);
        this.view = this.attendanceView;
    }

    @Override
    public void showOn() {
        attendanceView.showDeptSelection();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public String[][] getMockDepartments() {
        return new String[][]{
            {"1", "Phòng Kỹ Thuật", "KT"},
            {"2", "Phòng Nhân Sự", "NS"},
            {"3", "Phòng Kế Toán", "KT"},
        };
    }

    public boolean selectDepartment(int id) {
        for (String[] d : getMockDepartments()) {
            if (Integer.parseInt(d[0]) == id) {
                selectedDeptId = id;
                abnormalOnly = false;
                return true;
            }
        }
        return false;
    }

    // [0]=code [1]=name [2]=workDays [3]=standard [4]=OT [5]=late [6]=early [7]=unpaid [8]=paid [9]=status [10]=deptId
    public String[][] getAllAttendance() {
        return new String[][]{
            {"NV001", "Nguyễn Văn Lộc", "22", "26", "10", "1", "0", "1", "0", "Đã chốt", "1"},
            {"NV002", "Trần Thị Ánh", "20", "26", "5", "2", "1", "2", "1", "Đã chốt", "2"},
            {"NV003", "Lê Đình Cương", "22", "26", "15", "0", "0", "0", "0", "Đã chốt", "1"},
            {"NV004", "Phạm Thị Hoa", "22", "26", "8", "0", "2", "1", "0", "Đã chốt", "3"},
            {"NV005", "Hoàng Văn Nam", "18", "26", "0", "3", "3", "0", "2", "Chưa chốt", "2"},
        };
    }

    public String getDeptName(int deptId) {
        for (String[] d : getMockDepartments()) {
            if (Integer.parseInt(d[0]) == deptId) return d[1];
        }
        return "";
    }

    public String[][] getFilteredAttendance() {
        List<String[]> result = new ArrayList<>();
        for (String[] a : getAllAttendance()) {
            if (Integer.parseInt(a[10]) != selectedDeptId) continue;
            boolean abnormal = isAbnormal(a);
            if (abnormalOnly && !abnormal) continue;
            result.add(a);
        }
        return result.toArray(new String[0][0]);
    }

    public boolean isAbnormal(String[] a) {
        int late = Integer.parseInt(a[5]);
        int early = Integer.parseInt(a[6]);
        int unpaid = Integer.parseInt(a[7]);
        int ot = Integer.parseInt(a[4]);
        return late >= 3 || early >= 3 || unpaid >= 2 || ot > 40;
    }

    public String getAttendanceTable() {
        String[][] data = getFilteredAttendance();
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== BẢNG CHẤM CÔNG ==========\n");
        sb.append("Phòng: ").append(getDeptName(selectedDeptId)).append(" | Kỳ: ").append(currentPeriod).append("\n");
        if (abnormalOnly) sb.append(" [Đang lọc: Nhân viên bất thường]\n");
        sb.append("\n");
        sb.append(String.format("%-4s %-8s %-20s %-6s %-6s %-6s %-6s %-6s %-6s %-10s",
                "STT", "Mã NV", "Họ tên", "Công", "Chuẩn", "OT", "Muộn", "Sớm", "NL", "Đánh giá"));
        sb.append("\n------------------------------------------------------------------------------\n");
        int i = 1;
        int totalWork = 0, totalOT = 0;
        for (String[] a : data) {
            boolean abnormal = isAbnormal(a);
            String label = abnormal ? "⚠ Bất thường" : "Bình thường";
            sb.append(String.format("%-4d %-8s %-20s %-6s %-6s %-6s %-6s %-6s %-6s %-10s",
                    i++, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], label));
            sb.append("\n");
            totalWork += Integer.parseInt(a[2]);
            totalOT += Integer.parseInt(a[4]);
        }
        sb.append("------------------------------------------------------------------------------\n");
        sb.append("Tổng: ").append(data.length).append(" nhân viên | Tổng công: ").append(totalWork).append(" | Tổng OT: ").append(totalOT).append(" giờ\n");
        return sb.toString();
    }

    public String getDetail(int index) {
        String[][] data = getFilteredAttendance();
        if (index < 1 || index > data.length) return "Không hợp lệ.";
        String[] a = data[index - 1];
        return "Chi tiết chấm công: " + a[1] + " (" + a[0] + ")\n"
             + "Ngày công thực tế: " + a[2] + "/" + a[3] + "\n"
             + "Giờ làm thêm: " + a[4] + " giờ\n"
             + "Đi muộn: " + a[5] + " lần\n"
             + "Về sớm: " + a[6] + " lần\n"
             + "Nghỉ không lương: " + a[7] + " ngày\n"
             + "Nghỉ phép: " + a[8] + " ngày\n"
             + "Trạng thái: " + a[9] + "\n";
    }

    public void toggleAbnormalFilter() { abnormalOnly = !abnormalOnly; }
    public boolean isAbnormalOnly() { return abnormalOnly; }
    public int getSelectedDeptId() { return selectedDeptId; }
}
