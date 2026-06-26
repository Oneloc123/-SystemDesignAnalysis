package view;

import controller.LoginController;
import controller.MainController;
import controller.recruitmentManagement.CandidateController;
import enumModel.AddressEnum;

public class LoginView extends View {
    LoginController lc;

    AddressEnum address = AddressEnum.Login;

    public LoginView(LoginController lc) {
        MainController.addresses.add(address);
        this.lc = lc;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printAddress();
            // Custom menu: 1. Login, 2. Submit CV, 3. Exit, 4. Forgot Password
            System.out.println("------------------------");
            System.out.println("Danh Sách Chức Năng");
            System.out.println("1: Đăng nhập");
            System.out.println("2: Nộp CV");
            System.out.println("3: Thoát");
            System.out.println("4: Quên mật khẩu");
            System.out.println("------------------------");
            printAddress();
            handleInput();

            switch (question) {
                case "1":
                    if (handleLogin()) {
                        lc.goToHome();
                        if (!MainController.addresses.isEmpty()
                                && MainController.addresses.get(MainController.addresses.size() - 1) != address) {
                            MainController.addresses.remove(MainController.addresses.size() - 1);
                        }
                    }
                    break;
                case "2":
                    handleSubmitCV();
                    break;
                case "3":
                    System.out.println("Thoat thanh cong");
                    break loop;
                case "4":
                    handleForgotPassword();
                    break;
                default:
                    showError("Lệnh không hợp lệ");
                    break;
            }
        }
    }

    private boolean handleLogin() throws Exception {
        String username = handleParam("tên đăng nhập");
        String password = handleParam("mật khẩu");
        return lc.login(username, password);
    }

    private void handleSubmitCV() throws Exception {
        CandidateController candidateController = new CandidateController();
        candidateController.navigate();
    }

    private void handleForgotPassword() throws Exception {
        String email = handleParam("email đã đăng ký");
        String newPassword = handleParam("mật khẩu mới");
        String confirmPassword = handleParam("xác nhận mật khẩu mới");
        lc.forgotPassword(email, newPassword, confirmPassword);
    }

    @Override
    public void showError(String error) {
        System.out.println("------------------------");
        System.out.println("LOI: " + error);
        System.out.println("------------------------");
    }
}
