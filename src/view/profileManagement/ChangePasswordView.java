package view.profileManagement;

import controller.MainController;
import controller.profileManagement.ChangePasswordController;
import enumModel.AddressEnum;
import view.View;

public class ChangePasswordView extends View {
    ChangePasswordController controller;
    AddressEnum address = AddressEnum.ChangePassword;

    public ChangePasswordView(ChangePasswordController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showForm() {
        System.out.println("\n========== ĐỔI MẬT KHẨU ==========");
        try {
            System.out.print("Mật khẩu hiện tại: ");
            String oldPw = netIn.readLine();
            System.out.print("Mật khẩu mới: ");
            String newPw = netIn.readLine();
            System.out.print("Xác nhận mật khẩu mới: ");
            String confirmPw = netIn.readLine();

            String result = controller.changePassword(oldPw, newPw, confirmPw);
            if ("SUCCESS".equals(result)) {
                System.out.println("Đổi mật khẩu thành công.");
            } else {
                showError(result);
            }
        } catch (Exception e) {
            showError("Lỗi nhập liệu.");
        } finally {
            MainController.addresses.remove(address);
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        try { netIn.readLine(); } catch (Exception e) {}
    }
}
