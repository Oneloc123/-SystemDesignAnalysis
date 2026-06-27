package view.department;

import controller.department.CreateDepartmentController;
import view.View;

public class CreateDepartmentView extends View {
    private CreateDepartmentController controller;

    public CreateDepartmentView(CreateDepartmentController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        printWelcome("TẠO PHÒNG BAN MỚI");

        String name = handleParam("tên phòng ban");
        if (name == null || name.trim().isEmpty()) {
            showError("Tên phòng ban không được để trống.");
            return;
        }

        String code = handleParam("mã phòng ban (VD: IT, HR, ACC)");
        if (code == null || code.trim().isEmpty()) {
            showError("Mã phòng ban không được để trống.");
            return;
        }

        String managerName = handleParam("tên trưởng phòng (bỏ trống nếu chưa có)");

        // Xác nhận
        System.out.println("------------------------");
        System.out.println("Xác nhận thông tin phòng ban:");
        System.out.println("  Tên       : " + name.trim());
        System.out.println("  Mã        : " + code.trim().toUpperCase());
        System.out.println("  Trưởng phòng: " + (managerName == null || managerName.trim().isEmpty() ? "(chưa có)" : managerName.trim()));
        System.out.println("------------------------");

        printAddress();
        System.out.print("Xác nhận tạo? (Y/N): ");
        String confirm = netIn.readLine();
        if (confirm == null || !confirm.trim().equalsIgnoreCase("Y")) {
            showMessage("Đã hủy tạo phòng ban.");
            return;
        }

        String error = controller.createDepartment(name, code, managerName);
        if (error != null) {
            showError(error);
        } else {
            System.out.println("------------------------");
            System.out.println("Tạo phòng ban thành công!");
            System.out.println("------------------------");
        }
    }
}
