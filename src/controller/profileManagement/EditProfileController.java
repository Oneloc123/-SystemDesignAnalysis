package controller.profileManagement;

import controller.MainController;
import enumModel.RoleEnum;
import model.User;
import view.profileManagement.EditProfileView;

import java.sql.SQLException;

public class EditProfileController {
    EditProfileView epp;
    User u;

    public EditProfileController(){ epp = new EditProfileView(this); }

    public void editProfile(User u) throws SQLException {
        u.editProfile(u);
    }

    public boolean navigateTo() throws Exception{
        if(MainController.currentUser.getRole().equals(RoleEnum.HR)){
            epp.show();
            return true;
        }
        return false;
    }
}
