package controller.profileManagement;

import controller.MainController;
import enumModel.RoleEnum;
import model.User;
import view.profileManagement.ProfileView;

import java.sql.SQLException;
import java.util.List;

public class ViewEmployeeProfileController {
    ProfileView pv;
    User u = new User();

    public ViewEmployeeProfileController(){
        pv = new ProfileView(this);
    }

    public List<User> getEmployeeList() throws SQLException {
        return u.getAllEmployee();
    }

    public boolean navigateTo() throws Exception{
        if(!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)){
            return false;
        }
        pv.show();
        return true;
    }
}
