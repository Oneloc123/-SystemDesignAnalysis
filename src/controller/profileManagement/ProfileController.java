package controller.profileManagement;

import controller.ScreenManager;
import controller.base.Controller;
import model.hr.Employee;
import view.profileManagement.ProfileView;

public class ProfileController extends Controller {
    ProfileView profileView;

    public ProfileController() {
        this.profileView = new ProfileView(this);
        this.view = this.profileView;
    }

    @Override
    public void showOn() {
        try {
            if (!checkAuth()) {
                showError("Không có quyền truy cập");
                return;
            }

            Employee emp = Employee.findByUserId(ScreenManager.getCurrentUser().getUserId());

            if (emp == null) {
                showError("Không tìm thấy thông tin cá nhân. Vui lòng liên hệ HR để được hỗ trợ.");
                return;
            }

            profileView.showProfile(emp);

        } catch (Exception e) {
            showError("Hệ thống đang bận, vui lòng thử lại sau ít phút.");
        }
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public Employee getProfile(int userId) {
        return Employee.findByUserId(userId);
    }
}
