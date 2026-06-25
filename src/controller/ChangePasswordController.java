package controller;

import controller.base.Controller;
import dao.UserDAO;
import model.User;
import view.ChangePasswordView;
import java.util.regex.Pattern;

public class ChangePasswordController extends Controller {
    ChangePasswordView changePasswordView;
    private UserDAO userDAO;

    public ChangePasswordController() {
        this.changePasswordView = new ChangePasswordView(this);
        this.view = this.changePasswordView;
        this.userDAO = new UserDAO();
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
        User currentUser = ScreenManager.getCurrentUser();
        if (currentUser == null) {
            return "Người dùng chưa đăng nhập";
        }

        if (!currentUser.getPassword().equals(oldPw)) {
            return "Mật khẩu hiện tại không đúng";
        }

        if (newPw.equals(oldPw)) {
            return "Mật khẩu mới không được trùng mật khẩu hiện tại";
        }

        if (!newPw.equals(confirmPw)) {
            return "Xác nhận mật khẩu không khớp";
        }

        String pwError = validatePasswordStrength(newPw);
        if (pwError != null) {
            return pwError;
        }

        if (userDAO.updatePassword(currentUser.getUserId(), newPw)) {
            currentUser.setPassword(newPw);
            return "SUCCESS";
        }
        return "Lỗi hệ thống. Vui lòng thử lại sau.";
    }

    private String validatePasswordStrength(String password) {
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ hoa";
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ thường";
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ số";
        }
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 ký tự đặc biệt";
        }
        return null;
    }
}
