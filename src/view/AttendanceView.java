package view;

import controller.AttendanceController;
import controller.MainController;
import enumModel.AddressEnum;

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

    public void showDeptSelection() {
        while (true) {
            System.out.println("\n========== CHỌN PHÒNG BAN (CHẤM CÔNG) ==========");
            for (String[] d : controller.getMockDepartments()) {
                System.out.println(d[0] + ". " + d[1] + " (" + d[2] + ")");
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
            System.out.println("3. Đổi phòng ban");
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
