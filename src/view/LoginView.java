package view;

import controller.LoginController;
import controller.MainController;
import enumModel.AddressEnum;

import static controller.MainController.printList;

public class LoginView extends View {
    LoginController lc;

    AddressEnum address = AddressEnum.Login;

    public LoginView(LoginController lc) {
        MainController.addresses.add(address);
        this.lc = lc;
    }

    public String[] funcs = {"Đăng nhập", "Quên mật khẩu"};

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printAddress();
            printList(funcs);
            printAddress();
            handleInput();

            if (question.equals("0")) {
                System.out.println("Thoat thanh cong");
                break loop;
            }

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
