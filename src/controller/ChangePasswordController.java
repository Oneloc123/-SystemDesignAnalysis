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
        if (!newPw.equals(confirmPw)) return "Xác nhận mật khẩu không khớp.";
        if (oldPw.equals(newPw)) return "Mật khẩu mới không được trùng mật khẩu hiện tại.";
        if (newPw.length() < 8) return "Mật khẩu phải có ít nhất 8 ký tự.";
        if (!newPw.matches(".*[A-Z].*")) return "Mật khẩu phải có chữ hoa.";
        if (!newPw.matches(".*[a-z].*")) return "Mật khẩu phải có chữ thường.";
        if (!newPw.matches(".*[0-9].*")) return "Mật khẩu phải có chữ số.";
        if (!newPw.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return "Mật khẩu phải có ký tự đặc biệt.";
        return "SUCCESS";
    }
}
