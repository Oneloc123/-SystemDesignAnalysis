package controller.profileManagement;

import controller.MainController;
import dao.ProfileDao.ProfileDao;
import enumModel.RoleEnum;
import model.profile.Profile;
import view.profileManagement.ProfileView;

import java.sql.SQLException;
import java.util.List;

public class ViewEmployeeProfileController {
    ProfileView pv;
    ProfileDao profileDao;

    public ViewEmployeeProfileController(){
        pv = new ProfileView(this);
        profileDao = new ProfileDao();
    }

    public List<Profile> getEmployeeList() throws SQLException {
        return profileDao.getAllActive();
    }

    public List<Profile> searchProfiles(String keyword) throws SQLException {
        return profileDao.search(keyword);
    }

    public Profile getProfileDetail(long id) throws SQLException {
        return profileDao.getById(id);
    }

    public boolean navigateTo() throws Exception{
        if(!MainController.currentUser.getRole().equals(RoleEnum.HR)){
            return false;
        }
        pv.show();
        return true;
    }
}
