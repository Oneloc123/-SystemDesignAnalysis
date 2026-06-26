package view.department;

import controller.MainController;
import controller.department.DepartmentManagementController;
import view.View;

public class DepartmentManagementView extends View {
    private DepartmentManagementController controller;

    public DepartmentManagementView(DepartmentManagementController controller) {
        this.controller = controller;
    }

    public String[] funcs = {
            "Tạo phòng ban",
            "Quản lý phòng ban"
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
                    controller.navigateToCreate();
                    break;
                case "2":
                    controller.navigateToManage();
                    break;
                default:
                    showError("Lệnh không hợp lệ");
            }
        }
    }
}

