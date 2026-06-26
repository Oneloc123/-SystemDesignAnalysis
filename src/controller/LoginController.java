package controller;

import dao.UserDAO;
import model.User;
import view.LoginView;

public class LoginController {
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    private final UserDAO userDAO;
    private final LoginView lv;
    private int failedAttempts = 0;

    public LoginController() {
        this.userDAO = new UserDAO();
        this.lv = new LoginView(this);
    }

    public void show() throws Exception {
        lv.show();
    }


    public boolean login(String username, String password) {
        if (failedAttempts >= MAX_LOGIN_ATTEMPTS) {
            lv.showError("Bạn đã nhập sai quá " + MAX_LOGIN_ATTEMPTS + " lần. Vui lòng thử lại sau hoặc dùng chức năng Quên mật khẩu.");
            return false;
        }

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            lv.showError("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            return false;
        }

        User user = userDAO.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            failedAttempts++;
            int remaining = MAX_LOGIN_ATTEMPTS - failedAttempts;
            if (remaining > 0) {
                lv.showError("Sai tên đăng nhập hoặc mật khẩu. Còn " + remaining + " lần thử.");
            } else {
                lv.showError("Sai tên đăng nhập hoặc mật khẩu. Bạn đã hết số lần thử cho phép.");
            }
            MainController.currentUser = null;
            return false;
        }

        failedAttempts = 0;
        MainController.currentUser = user;
        lv.showMessage("Đăng nhập thành công! Xin chào " + user.getUsername() + " (" + user.getRole() + ")");
        return true;
    }


    public boolean forgotPassword(String email, String newPassword, String confirmPassword) {
        if (email == null || email.isEmpty()) {
            lv.showError("Vui lòng nhập email");
            return false;
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            lv.showError("Không tìm thấy tài khoản với email này");
            return false;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            lv.showError("Mật khẩu mới không được để trống");
            return false;
        }

        if (newPassword.length() < 6) {
            lv.showError("Mật khẩu mới phải có ít nhất 6 ký tự");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            lv.showError("Mật khẩu xác nhận không khớp");
            return false;
        }

        boolean updated = userDAO.updatePassword(user.getUserId(), newPassword);
        if (!updated) {
            lv.showError("Cập nhật mật khẩu thất bại, vui lòng thử lại");
            return false;
        }

        lv.showMessage("Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại với mật khẩu mới.");
        return true;
    }

    public void goToHome() throws Exception {
        HomeController hc = new HomeController();
        hc.show();
    }
}