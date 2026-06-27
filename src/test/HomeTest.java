package test;

import controller.HomeController;
import controller.MainController;
import enumModel.RoleEnum;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeTest {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("============================================");
        System.out.println("  TEST THỦ CÔNG HomeView");
        System.out.println("  Bạn sẽ tự tay thao tác nhập để kiểm tra");
        System.out.println("============================================\n");

        // ---- Bước 1: Chọn Role ----
        System.out.println("--- Chọn Role để test ---");
        System.out.println("1. ADMIN    (có quyền tính lương)");
        System.out.println("2. ACCOUNT  (có quyền tính lương)");
        System.out.println("3. HR       (không có quyền)");
        System.out.println("4. EMPLOYEE (không có quyền)");
        System.out.println("5. NULL     (chưa set role - test lỗi)");
        System.out.print("Nhập lựa chọn (1-5): ");
        String choice = reader.readLine();

        User testUser = new User();
        String roleName;
        switch (choice.trim()) {
            case "1":
                testUser.setRole(RoleEnum.ADMIN);
                roleName = "ADMIN";
                AdminTest.main(args);
                break;
            case "2":
                testUser.setRole(RoleEnum.ACCOUNTANT);
                roleName = "ACCOUNT";
                break;
            case "3":
                testUser.setRole(RoleEnum.HR);
                roleName = "HR";
                break;
            case "4":
                testUser.setRole(RoleEnum.EMPLOYER);
                roleName = "EMPLOYER";
                break;
            case "5":
                // Không set role -> null
                roleName = "NULL (chưa set role)";
                break;
            default:
                testUser.setRole(RoleEnum.ADMIN);
                roleName = "ADMIN (mặc định)";
        }

        // ---- Bước 2: Thiết lập MainController ----
        MainController.addresses = new ArrayList<>();
        MainController.currentUser = testUser;

        // ---- Bước 3: Hướng dẫn ----
        System.out.println("\n============================================");
        System.out.println("  THIẾT LẬP: User role = " + roleName);
        System.out.println("============================================");
        System.out.println("\n--- HƯỚNG DẪN THAO TÁC ---");
        System.out.println("  o  Nhập số 1 -> Chọn chức năng \"Tính Lương\"");
        System.out.println("     + Nếu role = ADMIN hoặc ACCOUNT: vào được màn chỉnh tham số");
        System.out.println("     + Nếu role = HR/EMPLOYER/null: báo lỗi \"Khong co quyen\"");
        System.out.println("  o  Nhập số 2 -> Chọn chức năng \"Quản lý tuyển dụng\"");
        System.out.println("  o  Nhập số 0 -> Thoát chương trình");
        System.out.println("  o  Nhập số khác -> Báo lỗi \"Lệnh không hợp lệ\"");
        System.out.println("  o  Trong màn chỉnh tham số: nhập Y để sửa, N để quay lại");
        System.out.println("\n--- BẮT ĐẦU ---\n");

        // ---- Bước 4: Chạy HomeView ----
        try {
            HomeController hc = new HomeController();
            hc.show();
        } catch (NullPointerException e) {
            System.out.println("\n=== LỖI: NullPointerException ===");
            System.out.println("  => Có thể do MainController.currentUser chưa được set");
            System.out.println("  => Hoặc Parameter.toString() gọi taxBraket.toString() khi taxBraket = null");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n=== LỖI: " + e.getClass().getSimpleName() + " ===");
            System.out.println("  Message: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== KẾT THÚC TEST ===");
    }
}
