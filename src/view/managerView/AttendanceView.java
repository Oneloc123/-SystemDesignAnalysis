package view.managerView;

import controller.MainController;
import controller.profileManagement.AttendanceController;
import enumModel.AddressEnum;
import model.Department;
import view.View;
import java.util.List;

public class AttendanceView extends View {
    AttendanceController controller;
    AddressEnum address = AddressEnum.Attendance;

    public AttendanceView(AttendanceController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    private void handleChangeMonth() {
        try {
            System.out.print("Nhập tháng (1-12): ");
            String inputMonth = netIn.readLine();
            System.out.print("Nhập năm (yyyy): ");
            String inputYear = netIn.readLine();
            if (inputMonth == null || inputYear == null) return;
            int month = Integer.parseInt(inputMonth.trim());
            int year = Integer.parseInt(inputYear.trim());
            if (!controller.setMonth(month, year)) {
                showError("Tháng không hợp lệ (tối đa 12 tháng trước).");
            }
        } catch (NumberFormatException e) {
            showError("Vui lòng nhập số.");
        } catch (Exception e) {
            showError("Lỗi xử lý.");
        }
    }

    public void showDeptSelection() {
        while (true) {
            System.out.println("\n========== CHỌN PHÒNG BAN (CHẤM CÔNG) ==========");
            List<Department> depts = controller.getDepartments();
            for (Department d : depts) {
                System.out.println(d.getDepartmentId() + ". " + d.getName() + " (" + d.getCode() + ")");
            }
            System.out.println("0. Quay lại");
            System.out.print("Chọn phòng ban: ");
            try {
                String input = netIn.readLine();
                if (input == null || input.equals("0")) break;
                int id = Integer.parseInt(input);
                if (controller.selectDepartment(id)) {
                    showAttendanceTable();
                } else {
                    showError("Phòng ban không hợp lệ");
                }
            } catch (Exception e) {
                showError("Vui lòng nhập số");
            }
        }
        MainController.addresses.remove(address);
    }

    private void showAttendanceTable() {
        while (true) {
            String table = controller.getAttendanceTable();
            System.out.println(table);
            System.out.println("1. " + (controller.isAbnormalOnly() ? "Bỏ lọc bất thường" : "Lọc nhân viên bất thường"));
            System.out.println("2. Xem chi tiết");
            System.out.println("3. Chọn tháng khác");
            System.out.println("4. Đổi phòng ban");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                String input = netIn.readLine();
                if (input == null || input.equals("0")) break;
                switch (input) {
                    case "1":
                        controller.toggleAbnormalFilter();
                        break;
                    case "2":
                        System.out.print("Nhập STT nhân viên: ");
                        try {
                            int idx = Integer.parseInt(netIn.readLine());
                            System.out.println("\n--- CHI TIẾT CHẤM CÔNG ---");
                            System.out.println(controller.getDetail(idx));
                        } catch (NumberFormatException e) {
                            showError("Số không hợp lệ");
                        }
                        System.out.println("Nhấn Enter để tiếp tục...");
                        netIn.readLine();
                        break;
                    case "3":
                        handleChangeMonth();
                        break;
                    case "4":
                        return;
                    default:
                        showError("Lệnh không hợp lệ");
                }
            } catch (Exception e) {
                showError("Lỗi nhập liệu");
            }
        }
    }
}
