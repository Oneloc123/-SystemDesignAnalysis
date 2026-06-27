package controller.profileManagement;

import controller.ScreenManager;
import view.profileManagement.ChangePasswordView;

public class ChangePasswordController {
    private ChangePasswordView changePasswordView;

    public ChangePasswordController() {
        this.changePasswordView = new ChangePasswordView(this);
    }

    public void showOn() {
        changePasswordView.showForm();
    }

    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public String changePassword(String oldPw, String newPw, String confirmPw) {
        return ScreenManager.getCurrentUser().changePassword(oldPw, newPw, confirmPw);
    }
}
