package view.profileManagement;

import controller.profileManagement.ProfileController;
import controller.profileManagement.ViewEmployeeProfileController;
import model.User;
import view.View;

import java.util.List;

public class ProfileView extends View {

    ViewEmployeeProfileController vepc;
    User user;

    public ProfileView(ViewEmployeeProfileController vepc) {
        this.vepc = vepc;
    }

    @Override
    public void show() throws Exception {

        loop:
        while(true) {

            List<User> users = vepc.getEmployeeList();

            if (users == null || users.isEmpty()) {
                System.out.println("Hệ thống chưa có nhân viên nào.");
            } else {
                for (User u : users) {
                    System.out.printf("%-5d | %-20s | %-12s | %-10s | %-12s\n",
                            u.getUserId(),
                            u.getFullName(),
                            u.getRole(),
                            u.getGender(),
                            u.getPhone());
                }
            }
            System.out.println("nhập '0' để quay lại");
            printAddress();
            handleInput();

            //exit
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            // thuc thi func

        }
    }

}



