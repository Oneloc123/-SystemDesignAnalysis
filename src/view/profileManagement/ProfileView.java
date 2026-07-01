package view.profileManagement;

import controller.profileManagement.ViewEmployeeProfileController;
import model.profile.Profile;
import view.View;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileView extends View {

    ViewEmployeeProfileController vepc;

    public ProfileView(ViewEmployeeProfileController vepc) {
        this.vepc = vepc;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            System.out.println("1. Xem danh sách nhân viên");
            System.out.println("2. Tìm kiếm nhân viên");
            System.out.println("0. Quay lại");
            printAddress();
            handleInput();

            if(question.equals("0")) {
                System.out.println("Thoat thanh cong");
                break loop;
            }

            if (question.equals("1")) {
                List<Profile> profiles = vepc.getEmployeeList();

                if (profiles == null || profiles.isEmpty()) {
                    System.out.println("Hệ thống chưa có nhân viên nào.");
                } else {
                    System.out.printf("%-5s | %-20s | %-12s | %-10s | %-12s | %-12s\n",
                            "ID", "Họ tên", "Vai trò", "Giới tính", "SĐT", "Trạng thái");
                    System.out.println("-------------------------------------------------------------------");
                    for (Profile p : profiles) {
                        String status = p.getStatus() != null ? p.getStatus().name() : "WORKING";
                        System.out.printf("%-5d | %-20s | %-12s | %-10s | %-12s | %-12s\n",
                                p.getId(),
                                p.getFullName(),
                                p.getRole(),
                                p.getGender(),
                                p.getPhone(),
                                status);
                    }
                }
                System.out.println("Nhấn 0 để quay lại");
                printAddress();
                handleInput();
                if(question.equals("0")) {
                    System.out.println("Thoat thanh cong");
                    break loop;
                }
            } else if (question.equals("2")) {
                String keyword = handleParam("từ khóa tìm kiếm:");
                List<Profile> results = vepc.searchProfiles(keyword);

                if (results == null || results.isEmpty()) {
                    System.out.println("Không tìm thấy kết quả nào.");
                } else {
                    System.out.printf("%-5s | %-20s | %-12s | %-10s | %-12s\n",
                            "ID", "Họ tên", "Vai trò", "Giới tính", "SĐT");
                    System.out.println("-----------------------------------------------------------");
                    for (Profile p : results) {
                        System.out.printf("%-5d | %-20s | %-12s | %-10s | %-12s\n",
                                p.getId(),
                                p.getFullName(),
                                p.getRole(),
                                p.getGender(),
                                p.getPhone());
                    }
                }
            } else {
                showError("Lệnh không hợp lệ");
            }
        }
    }
}
