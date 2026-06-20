package controller;

import controller.base.Controller;
import view.ChangePasswordView;

public class ChangePasswordController extends Controller {
    ChangePasswordView changePasswordView;

    public ChangePasswordController() {
        this.changePasswordView = new ChangePasswordView(this);
        this.view = this.changePasswordView;
    }

    @Override
    public void showOn() {
        changePasswordView.showForm();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public String changePassword(String oldPw, String newPw, String confirmPw) {
        return ScreenManager.getCurrentUser().changePassword(oldPw, newPw, confirmPw);
    }
}
