package controller.contract;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import view.contract.ContractManagementView;

public class ContractManagementController {
    private ContractManagementView view;
    private AddressEnum address = AddressEnum.ContractManagement;

    public ContractManagementController() {
        this.view = new ContractManagementView(this);
    }


    public boolean navigate() throws Exception {
        RoleEnum role = MainController.currentUser.getRole();
        if (role != RoleEnum.HR && role != RoleEnum.ADMIN && role != RoleEnum.EMPLOYER) {
            return false;
        }
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void navigateToCreate() throws Exception {
        RoleEnum role = MainController.currentUser.getRole();
        if (role == RoleEnum.HR || role == RoleEnum.ADMIN) {
            CreateContractController ccc = new CreateContractController();
            ccc.show();
        } else {
            view.showError("Bạn không có quyền tạo hợp đồng. Chỉ HR hoặc ADMIN mới được phép.");
        }
    }

    public void navigateToView() throws Exception {
        ViewContractController vcc = new ViewContractController();
        vcc.show();
    }
}
