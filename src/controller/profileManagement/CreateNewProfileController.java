package controller.profileManagement;

import controller.MainController;
import dao.ProfileDao.ProfileDao;
import enumModel.RoleEnum;
import model.profile.Profile;
import enumModel.ProfileStatus;
import view.profileManagement.CreateProfileView;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNewProfileController {
    CreateProfileView cpv;
    ProfileDao profileDao;

    public CreateNewProfileController(){
        cpv = new CreateProfileView(this);
        profileDao = new ProfileDao();
    }

    public boolean navigateTo() throws Exception{
        if(!MainController.currentUser.getRole().equals(RoleEnum.HR)){
            return false;
        }
        cpv.show();
        return true;
    }

    public Map<String, String> createProfile(Map<String, Object> rawData) {
        Map<String, String> errors = new HashMap<>();

        try {
            Map<String, String> validationErrors = Profile.validateBasic(rawData);
            if (!validationErrors.isEmpty()) {
                return validationErrors;
            }

            String citizenIdentificationCard = (String) rawData.get("citizenIdentificationCard");
            String phone = (String) rawData.get("phone");

            if (profileDao.checkDuplicate(citizenIdentificationCard, phone)) {
                errors.put("duplicate", "CCCD hoặc số điện thoại đã tồn tại trong hệ thống");
                return errors;
            }

            long maxId = profileDao.getMaxId();
            long newId = maxId + 1;

            Date dateOfBirth = (Date) rawData.get("dateOfBirth");
            Profile profile = new Profile(
                newId,
                (String) rawData.get("fullName"),
                dateOfBirth,
                (String) rawData.get("gender"),
                phone,
                citizenIdentificationCard,
                (String) rawData.get("address"),
                (String) rawData.get("role"),
                ProfileStatus.WORKING
            );

            profileDao.addUser(profile);
        } catch (SQLException e) {
            errors.put("system", "Lỗi hệ thống: " + e.getMessage());
        }

        return errors;
    }
}
