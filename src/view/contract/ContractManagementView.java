package view.contract;

import controller.MainController;
import controller.contract.ContractManagementController;
import enumModel.AddressEnum;
import view.View;

public class ContractManagementView extends View {
    private ContractManagementController controller;

    public ContractManagementView(ContractManagementController controller) {
        this.controller = controller;
    }

    public String[] funcs = {
            "Tạo hợp đồng mới",
            "Xem / Tìm kiếm hợp đồng"
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
                    controller.navigateToView();
                    break;
                default:
                    showError("Lệnh không hợp lệ");
            }
        }
    }
}
