package view;

import controller.LoginController;
import model.User;

public class LoginView extends  View {
    private final LoginController loginController;
    public LoginView(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void show() throws Exception{
        int maxAttempts = loginController.getMaxAttempts();
        int attempts = 0;

        printWelcome("===HE THONG QUAN LY NHAN SU===");
        while(attempts < maxAttempts) {
            int remaining = maxAttempts - attempts;
            if (attempts >0){
                System.out.println("Còn " + remaining + " lần thử.");
            }
            System.out.println(" Nhập '0' để thoát.");
            System.out.println("--------------------------");

            printAddress();
            System.out.println("Tên đăng nhập: ");
            String username = netIn.readLine();
            if ("0".equals(username)) {
                showMessage("Đã thoát khỏi hệ thống");
                loginController.setLoggedIn(false);
                return ;
            }
            printAddress();
            System.out.println("Mật khẩu: ");
            String password = netIn.readLine();
            if ("0".equals(password)) {
                showMessage("Đã thoát khỏi hệ thống");
                loginController.setLoggedIn(false);
                return ;
            }
            User user = loginController.login(username,password);
            if (user != null) {
                System.out.println("--------------------------");
                System.out.println("Đăng nhập thành công!");
                System.out.println("Xin chào," + user.getUsername() +"[" + user.getRole() + "]");
                System.out.println("--------------------------");
                loginController.setLoggedIn(true);
            }else{
                attempts++;
                showError("Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        }
        System.out.println("==========================================");
        System.out.println("Tài khoản bị tạm khóa do nhập sai mật khẩu nhiều lần.");
        System.out.println("Vui lòng liên hệ quản trị viên");
        System.out.println("============================================");
        loginController.setLoggedIn(false);
    }

    @Override
    public void showError(String error) {
        System.out.println("==========================================");
        System.out.println("Lôĩ: " + error);
        System.out.println("==========================================");
    }

}
