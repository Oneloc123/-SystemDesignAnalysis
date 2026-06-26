package controller.profileManagement;

import controller.MainController;


import enumModel.RoleEnum;
import model.User;
import view.profileManagement.EditProfileView;
import view.profileManagement.ProfileManagementView;
import view.profileManagement.ProfileView;

import java.util.List;

public class ProfileController {
    ProfileManagementView pmv;

    public ProfileController() throws Exception {
        this.pmv = new ProfileManagementView(this);
    };

    public boolean navigate() throws Exception {
        // xác thực quyền
        if (!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)) {
            return false;
        }
        pmv.show();
        return true;
    }

    public void excuteComent(String question) throws Exception {
        switch (question){
            case "1":
                functionEditProfile();
                break;
            case "2":
                showEmployeeList();
                break;
            case "3":
                functionCreateNewProfile();
                break;
            default:
                pmv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    private void functionCreateNewProfile() throws Exception {

            CreateNewProfileController cnpc = new CreateNewProfileController();
            boolean result = cnpc.navigateTo();
            if(!result){
                pmv.showError("không có quyền truy cập");
            }

    }

    private void functionEditProfile() throws Exception {

        EditProfileController epc = new EditProfileController();
        boolean rs = epc.navigateTo();
        if (!rs){
            pmv.showError("Không có quyền truy cập");
        }
    }

    public void showEmployeeList() throws Exception {
        ViewEmployeeProfileController vepc = new ViewEmployeeProfileController();
        boolean check = vepc.navigateTo();
        if(!check){
            pmv.showError("khong co quyen truy cap");
        }
}


}
