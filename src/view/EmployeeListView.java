package view;

import controller.EmployeeListController;
import controller.MainController;
import enumModel.AddressEnum;
import model.Department;
import model.hr.Employee;
import java.util.List;

public class EmployeeListView extends View {
    EmployeeListController controller;
    AddressEnum address = AddressEnum.EmployeeList;

    public EmployeeListView(EmployeeListController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showDeptSelection() {
        while (true) {
            System.out.println("\n========== DANH SÁCH PHÒNG BAN ==========");
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
                    showEmployeeList();
                } else {
                    showError("Phòng ban không hợp lệ");
                }
            } catch (Exception e) {
                showError("Vui lòng nhập số");
            }
        }
        MainController.addresses.remove(address);
    }

    private void showEmployeeList() {
        while (true) {
            List<Employee> employees = controller.getFilteredEmployees();
            System.out.println("\n========== NHÂN VIÊN PHÒNG BAN ==========");
            System.out.println(String.format("%-4s %-8s %-22s %-15s %-12s %-10s",
                    "STT", "Mã NV", "Họ tên", "Chức danh", "SĐT", "Trạng thái"));
            System.out.println("------------------------------------------------------------");
            int i = 1;
            for (Employee emp : employees) {
                System.out.println(String.format("%-4d %-8s %-22s %-15s %-12s %-10s",
                        i++, emp.getEmployeeCode(), emp.getFullName(),
                        emp.getPosition(), emp.getPhone(), emp.getEmployeeStatus()));
            }
            System.out.println("Tổng: " + employees.size() + " nhân viên");
            System.out.println("\n1. Tìm kiếm");
            System.out.println("2. Lọc trạng thái");
            System.out.println("3. Xem chi tiết");
            System.out.println("4. Đổi phòng ban");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                String input = netIn.readLine();
                if (input == null || input.equals("0")) break;
                switch (input) {
                    case "1":
                        System.out.print("Nhập từ khóa (tên, mã NV, email): ");
                        controller.setSearchKeyword(netIn.readLine());
                        break;
                    case "2":
                        System.out.println("1. Đang làm");
                        System.out.println("2. Đã nghỉ");
                        System.out.println("3. Tất cả");
                        System.out.print("Chọn: ");
                        String f = netIn.readLine();
                        if ("1".equals(f)) controller.setFilterStatus("Đang làm");
                        else if ("2".equals(f)) controller.setFilterStatus("Đã nghỉ");
                        else if ("3".equals(f)) controller.setFilterStatus("");
                        else showError("Không hợp lệ");
                        break;
                    case "3":
                        System.out.print("Nhập STT nhân viên: ");
                        try {
                            int idx = Integer.parseInt(netIn.readLine());
                            System.out.println("\n--- CHI TIẾT NHÂN VIÊN ---");
                            System.out.println(controller.getEmployeeDetail(idx));
                        } catch (NumberFormatException e) {
                            showError("Số không hợp lệ");
                        }
                        System.out.println("Nhấn Enter để tiếp tục...");
                        netIn.readLine();
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
