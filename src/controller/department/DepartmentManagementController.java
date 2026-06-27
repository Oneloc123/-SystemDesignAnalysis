package controller.department;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import view.department.DepartmentManagementView;

public class DepartmentManagementController {
    private DepartmentManagementView view;
    private AddressEnum address = AddressEnum.DepartmentManagement;

    public DepartmentManagementController() {
        this.view = new DepartmentManagementView(this);
    }

    public boolean navigate() throws Exception {
        RoleEnum role = MainController.currentUser.getRole();
        if (role != RoleEnum.HR && role != RoleEnum.ADMIN) {
            return false;
        }
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void navigateToCreate() throws Exception {
        CreateDepartmentController cdc = new CreateDepartmentController();
        cdc.show();
    }

    public void navigateToManage() throws Exception {
        ManageDepartmentController mdc = new ManageDepartmentController();
        mdc.show();
    }
}
