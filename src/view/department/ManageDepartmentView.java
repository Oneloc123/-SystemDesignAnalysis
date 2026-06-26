package view.department;

import controller.MainController;
import controller.department.ManageDepartmentController;
import model.Department;
import view.View;

import java.util.List;

public class ManageDepartmentView extends View {
    private ManageDepartmentController controller;

    public ManageDepartmentView(ManageDepartmentController controller) {
        this.controller = controller;
    }

    private String[] funcs = {
            "Xem danh sách phòng ban",
            "Cập nhật phòng ban",
            "Xóa phòng ban"
    };

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printAddress();
            MainController.printList(funcs);
            printAddress();
            handleInput();

            switch (question) {
                case "0":
                    break loop;
                case "1":
                    showAllDepartments();
                    break;
                case "2":
                    handleUpdate();
                    break;
                case "3":
                    handleDelete();
                    break;
                default:
                    showError("Lệnh không hợp lệ");
            }
        }
    }


    private void showAllDepartments() {
        List<Department> list = controller.getAllDepartments();
        System.out.println("------------------------");
        System.out.println("DANH SÁCH PHÒNG BAN");
        System.out.println("------------------------");
        if (list == null || list.isEmpty()) {
            System.out.println("(Chưa có phòng ban nào)");
        } else {
            System.out.printf("%-6s %-10s %-30s %-20s%n", "ID", "Mã PB", "Tên phòng ban", "Trưởng phòng");
            System.out.println("----------------------------------------------------------------------");
            for (Department d : list) {
                System.out.printf("%-6d %-10s %-30s %-20s%n",
                        d.getDepartmentId(),
                        d.getCode(),
                        d.getName(),
                        d.getManagerName() == null || d.getManagerName().isEmpty() ? "(chưa có)" : d.getManagerName());
            }
        }
        System.out.println("------------------------");
    }


    private void handleUpdate() throws Exception {
        showAllDepartments();

        String idStr = handleParam("ID phòng ban cần cập nhật (0 để hủy)");
        if (idStr == null || idStr.trim().equals("0")) return;

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            showError("ID không hợp lệ.");
            return;
        }

        Department existing = controller.getDepartmentById(id);
        if (existing == null) {
            showError("Không tìm thấy phòng ban với ID = " + id);
            return;
        }

        System.out.println("Thông tin hiện tại: " + existing.getName() + " [" + existing.getCode() + "] - " + existing.getManagerName());
        System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

        String name = handleParam("tên phòng ban mới [" + existing.getName() + "]");
        if (name == null || name.trim().isEmpty()) name = existing.getName();

        String code = handleParam("mã phòng ban mới [" + existing.getCode() + "]");
        if (code == null || code.trim().isEmpty()) code = existing.getCode();

        String mgr = handleParam("trưởng phòng mới [" + existing.getManagerName() + "]");
        if (mgr == null || mgr.trim().isEmpty()) mgr = existing.getManagerName();

        String error = controller.updateDepartment(id, name, code, mgr);
        if (error != null) {
            showError(error);
        } else {
            System.out.println("------------------------");
            System.out.println("Cập nhật phòng ban thành công!");
            System.out.println("------------------------");
        }
    }

    private void handleDelete() throws Exception {
        showAllDepartments();

        String idStr = handleParam("ID phòng ban cần xóa (0 để hủy)");
        if (idStr == null || idStr.trim().equals("0")) return;

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            showError("ID không hợp lệ.");
            return;
        }

        Department existing = controller.getDepartmentById(id);
        if (existing == null) {
            showError("Không tìm thấy phòng ban với ID = " + id);
            return;
        }

        System.out.println("Bạn chắc chắn muốn xóa phòng ban: " + existing.getName() + " [" + existing.getCode() + "] ?");
        printAddress();
        System.out.print("Xác nhận xóa? (Y/N): ");
        String confirm = netIn.readLine();
        if (confirm == null || !confirm.trim().equalsIgnoreCase("Y")) {
            showMessage("Đã hủy thao tác xóa.");
            return;
        }

        String error = controller.deleteDepartment(id);
        if (error != null) {
            showError(error);
        } else {
            System.out.println("------------------------");
            System.out.println("Xóa phòng ban thành công!");
            System.out.println("------------------------");
        }
    }
}