package view.admin;

import dao.admin.AdminUserDao;
import model.admin.AdminUser;
import model.admin.Attendance;
import model.admin.Role;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminConsoleView {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminUserDao aud = new AdminUserDao();

    public void showBanner() {
        System.out.println("========================================");
        System.out.println("            ADMIN SITE");
        System.out.println("========================================");
    }

    public void showMenu() {
        System.out.println();
        System.out.println("ADMIN MENU");
        System.out.println("1. Xem thông tin cá nhân");
        System.out.println("2. Xem lịch ca làm việc");
        System.out.println("3. Đổi mật khẩu");
        System.out.println("4. Cấp tài khoản");
        System.out.println("5. Xem danh sách tài khoản");
        System.out.println("6. Chỉnh sửa thông tin tài khoản");
        System.out.println("7. Phân quyền");
        System.out.println("8. Tìm tài khoản");
        System.out.println("9. Khóa tài khoản");
        System.out.println("0. Thoát");
        System.out.print("");
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Giá trị không hợp lệ. Vui lòng nhập từ " + min + " đến " + max + ".");
        }
    }

    public String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Không được để trống.");
        }
    }

    public String readOptionalString(String prompt, String currentValue) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? currentValue : value;
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String value = scanner.nextLine().trim().toUpperCase();
            if ("Y".equals(value)) {
                return true;
            }
            if ("N".equals(value)) {
                return false;
            }
            System.out.println("Vui lòng nhập Y hoặc N.");
        }
    }

    public int readRoleChoice(List<Role> roles) {
        System.out.println("Danh sách quyền:");
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            System.out.println((i + 1) + ". " + role.getRoleName() + " (" + role.getRoleCode() + ")");
        }
        return readInt("Chọn quyền", 1, roles.size()) - 1;
    }

    public void printUserList(List<AdminUser> users) {
        if (users.isEmpty()) {
            System.out.println("Không có tài khoản nào.");
            return;
        }

        System.out.println();
        System.out.println("DANH SÁCH TÀI KHOẢN");
        System.out.println("ID | Username | Role | Status | Last Login");
        System.out.println("-----------------------------------------");
        for (AdminUser user : users) {
            System.out.println(user.getUserId() + " | " + user.getUsername() + " | "
                    + user.getRoleName() + " | " + (user.isActive() ? "ACTIVE" : "INACTIVE")
                    + " | " + (user.getLastLogin() == null ? "N/A" : user.getLastLogin()));
        }
    }

    public void printUserDetail(AdminUser user) {
        System.out.println();
        System.out.println("CHI TIẾT TÀI KHOẢN");
        System.out.println("ID: " + user.getUserId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Role: " + user.getRoleName());
        System.out.println("Status: " + (user.isActive() ? "ACTIVE" : "INACTIVE"));
        System.out.println("Last Login: " + (user.getLastLogin() == null ? "N/A" : user.getLastLogin()));
        System.out.println("Created At: " + (user.getCreatedAt() == null ? "N/A" : user.getCreatedAt()));
        System.out.println("Updated At: " + (user.getUpdatedAt() == null ? "N/A" : user.getUpdatedAt()));
    }

    public void printAttendanceSchedule(List<Attendance> attendances) {
        if (attendances.isEmpty()) {
            System.out.println("Không có lịch ca làm việc.");
            return;
        }

        System.out.println();
        System.out.println("LỊCH CA LÀM VIỆC");
        System.out.println("Employee_id| Date       | Check In           | Check Out          | Hours");
        System.out.println("------------------------------------------------------------------");
        for (Attendance attendance : attendances) {
            System.out.printf("%-10s | %-18s | %-18s | %-5s | %s%n",
                    attendance.getEmp_id(),
                    attendance.getDate(),
                    attendance.getCheck_in() == null ? "N/A" : attendance.getCheck_in(),
                    attendance.getCheck_out() == null ? "N/A" : attendance.getCheck_out(),
                    attendance.getHours());
        }
    }

    public void printStatistics(Map<String, Integer> stats) {
        System.out.println();
        System.out.println("THỐNG KÊ");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printMessage(String message) {
        System.out.println("[INFO] " + message);
    }

    public void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

}