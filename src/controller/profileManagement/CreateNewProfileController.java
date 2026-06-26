package controller.profileManagement;

import controller.MainController;
import dao.ProfileDao.ProfileDao;
import enumModel.RoleEnum;
import model.User;
import view.profileManagement.CreateProfileView;

import java.sql.SQLException;

public class CreateNewProfileController {
    CreateProfileView cpv;
    ProfileDao profileDao;

    public CreateNewProfileController(){
        cpv = new CreateProfileView(this);
        profileDao = new ProfileDao();
    }

    public boolean navigateTo() throws Exception{
        if(!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)){
            return false;
        }

        cpv.show();
        return true;
    }

    public void createProfile(User user) {
        try {

            profileDao.addUser(user);
            System.out.println("Tạo hồ sơ thành công");
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
