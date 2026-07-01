package controller.profileManagement;

import controller.MainController;
import dao.ProfileDao.ProfileDao;
import enumModel.RoleEnum;
import model.profile.Profile;
import enumModel.ProfileStatus;
import view.profileManagement.EditProfileView;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileController {
    EditProfileView epp;
    ProfileDao profileDao;

    public EditProfileController(){
        epp = new EditProfileView(this);
        profileDao = new ProfileDao();
    }

    public Profile getProfileById(long id) throws SQLException {
        return profileDao.getById(id);
    }

    public Map<String, String> updateProfile(Map<String, Object> newData) {
        Map<String, String> errors = new HashMap<>();

        try {
            Map<String, String> validationErrors = Profile.validateUpdate(newData);
            if (!validationErrors.isEmpty()) {
                return validationErrors;
            }

            long id = Long.parseLong(newData.get("id").toString());
            String citizenIdentificationCard = (String) newData.get("citizenIdentificationCard");
            String phone = (String) newData.get("phone");

            if (profileDao.checkDuplicateForUpdate(id, citizenIdentificationCard, phone)) {
                errors.put("duplicate", "CCCD hoặc số điện thoại đã tồn tại trong hệ thống");
                return errors;
            }

            Date dateOfBirth = (Date) newData.get("dateOfBirth");
            Profile profile = new Profile(
                id,
                (String) newData.get("fullName"),
                dateOfBirth,
                (String) newData.get("gender"),
                phone,
                citizenIdentificationCard,
                (String) newData.get("address"),
                (String) newData.get("role"),
                ProfileStatus.WORKING
            );

            profileDao.updateUser(profile);
        } catch (Exception e) {
            errors.put("system", "Lỗi hệ thống: " + e.getMessage());
        }

        return errors;
    }

    public boolean navigateTo() throws Exception{
        if(MainController.currentUser.getRole().equals(RoleEnum.HR)){
            epp.show();
            return true;
        }
        return false;
    }
}
