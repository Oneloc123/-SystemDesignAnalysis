package controller.profileManagement;

import controller.MainController;
import enumModel.RoleEnum;
import view.profileManagement.CreateProfileView;

public class CreateNewProfileController {
    CreateProfileView cpv;

    public CreateNewProfileController(){ cpv = new CreateProfileView(this); }

    public boolean navigateTo() throws Exception{
        if(!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)){
            return false;
        }

        cpv.show();
        return true;
    }
}
