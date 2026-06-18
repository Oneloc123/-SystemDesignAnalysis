package controller;

import enumModel.AddressEnum;
import enumModel.RoleEnum;
import view.RecruitmentManagement.RecruitmentManagementView;

public class RecruitmentManagementController {
    private RecruitmentManagementView rmv;
    private AddressEnum address = AddressEnum.RecruitmentManagement;

    public RecruitmentManagementController(){
        MainController.addresses.add(address);
        rmv = new RecruitmentManagementView(this);
    }

    public boolean navigate() throws Exception {
        // xác thực quyền
        if(!MainController.currentUser.equals(RoleEnum.EMPLOYER)){
            return false;
        }
        // xác thực thành công
        rmv.show();
        // khi thoát
        MainController.addresses.remove(AddressEnum.RecruitmentManagement);
        return true;
    }

}
