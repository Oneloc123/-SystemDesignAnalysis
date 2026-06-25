package view;

import controller.AdminController;
import enumModel.RoleEnum;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import static controller.MainController.currentUser;
import static controller.MainController.printList;

public class AdminView extends View {
    private final AdminController adminController;
    private final String[] functions = {
            "Cấp tài khoản",
            "Phân quyền tài khoản",
            "Chỉnh sửa thông tin tài khoản",
            "Xem danh sách tài khoản",
            "Xem chi tiết tài khoản",
            "Tìm tài khoản theo tên người dùng",
            "Khoá tài khoản"
    };
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void show() throws Exception {
        while (true) {
            printAddress();
            printList(functions);
            handleInput();
            if ("0".equals(question)) {
                showMessage("Đã thoát quản lý admin");
                break;
            }
            adminController.executeCommand(question);
        }
    }

    public User inputNewAccount() throws IOException {
        User user = new User();
        user.setFullName(readRequired("Họ và tên"));
        user.setEmail(readRequired("Email"));
        user.setUsername(readRequired("Tên đăng nhập"));
        user.setPassword(readRequired("Mật khẩu"));
        user.setRole(inputRole());
        user.setActive(readOptionalBoolean("Tài khoản đang hoạt động.", true));
        inputEmployeeProfile(user, false);
        return user;
    }

    public User inputUpdatedAccount(User current) throws IOException {
        User user = new User();
        user.setUserId(current.getUserId());
        user.setEmail(readOptional("Email", current.getEmail()));
        user.setUsername(readOptional("Tên đăng nhập", current.getUsername()));
        user.setPassword(readOptional("Mật khẩu", current.getPassword()));
        user.setRole(readOptionalRole(current.getRole()));
        user.setActive(readOptionalBoolean("Tài khoản đang hoạt động.", current.getActive() == null || current.getActive()));
        inputEmployeeProfile(user, true, current);
        return user;
    }

    public String inputSearchKeyword() throws IOException {
        return readRequired("Từ khoá");
    }

    public boolean confirm(String message) throws IOException {
        while (true) {
            System.out.print(message + " (Y/N): ");
            String value = netIn.readLine();
            if (value == null) {
                return false;
            }
            value = value.trim().toUpperCase();
            if ("Y".equals(value)) {
                return true;
            }
            if ("N".equals(value)) {
                return false;
            }
            showError("Vui lòng nhập Y hoặc N");
        }
    }

    public int inputAccountId() throws IOException {
        while (true) {
            try {
                return Integer.parseInt(readRequired("Mã tài khoản"));
            } catch (NumberFormatException e) {
                showError("Mã tài khoản không phải là số.");
            }
        }
    }

    public String inputRole() throws IOException {
        while (true) {
            System.out.println("Danh sách role");
            for (RoleEnum role : RoleEnum.values()) {
                System.out.println("- " + role.name());
            }
            String role = readRequired("role").toUpperCase();
            if (isValidRole(role)) {
                return role;
            }
            showError("Role không hợp lệ");
        }
    }

    public void printUsers(List<User> users) {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s %-18s %-12s %-22s %-15s %-28s %-12s %-12s%n",
                "ID", "Username", "EmpCode", "FullName", "Role", "Email", "Active", "Status");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        if (users == null || users.isEmpty()) {
            System.out.println("Chưa có tài khoản nào.");
        } else {
            for (User user : users) {
                System.out.printf("%-6d %-18s %-12s %-22s %-15s %-28s %-12s %-12s%n",
                        user.getUserId(),
                        valueOrEmpty(user.getUsername()),
                        valueOrEmpty(user.getEmployeeCode()),
                        valueOrEmpty(user.getFullName()),
                        valueOrEmpty(user.getRole()),
                        valueOrEmpty(user.getEmail()),
                        user.getActive() == null ? "" : user.getActive().toString(),
                        valueOrEmpty(user.getStatus()));
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void printUserDetail(User user) {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("User ID        : " + user.getUserId());
        System.out.println("Username       : " + valueOrEmpty(user.getUsername()));
        System.out.println("Role           : " + valueOrEmpty(user.getRole()));
        System.out.println("Active         : " + (user.getActive() == null ? "" : user.getActive()));
        System.out.println("Last login     : " + valueOrEmpty(String.valueOf(user.getLastLogin())));
        System.out.println("Employee code  : " + valueOrEmpty(user.getEmployeeCode()));
        System.out.println("Full name      : " + valueOrEmpty(user.getFullName()));
        System.out.println("Gender         : " + valueOrEmpty(user.getGender()));
        System.out.println("Date of birth  : " + valueOrEmpty(String.valueOf(user.getDateOfBirth())));
        System.out.println("Email          : " + valueOrEmpty(user.getEmail()));
        System.out.println("Phone          : " + valueOrEmpty(user.getPhone()));
        System.out.println("Address        : " + valueOrEmpty(user.getAddress()));
        System.out.println("Hire date      : " + valueOrEmpty(String.valueOf(user.getHireDate())));
        System.out.println("Department ID  : " + valueOrEmpty(String.valueOf(user.getDepartmentId())));
        System.out.println("Position ID    : " + valueOrEmpty(String.valueOf(user.getPositionId())));
        System.out.println("Manager ID     : " + valueOrEmpty(String.valueOf(user.getManagerId())));
        System.out.println("Base salary    : " + valueOrEmpty(String.valueOf(user.getBaseSalary())));
        System.out.println("Employee status: " + valueOrEmpty(user.getStatus()));
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void inputEmployeeProfile(User user, boolean optional, User current) throws IOException {
        System.out.println("--- Hồ sơ nhân viên ---");
        user.setEmployeeCode(optional
                ? readOptional("ma nhan vien", current.getEmployeeCode())
                : readRequired("ma nhan vien"));
        user.setFullName(optional
                ? readOptional("ho ten", current.getFullName())
                : readRequired("ho ten"));
        user.setGender(optional
                ? readOptionalChoice("gioi tinh", current.getGender(), "MALE", "FEMALE", "OTHER")
                : readOptionalChoice("gioi tinh", null, "MALE", "FEMALE", "OTHER"));
        user.setDateOfBirth(readOptionalDate("ngay sinh yyyy-MM-dd", optional ? current.getDateOfBirth() : null));
        user.setEmail(readOptionalEmail("email", optional ? current.getEmail() : null));
        user.setPhone(readOptional("so dien thoai", optional ? current.getPhone() : null));
        user.setAddress(readOptional("dia chi", optional ? current.getAddress() : null));
        user.setHireDate(readOptionalDate("ngay vao lam yyyy-MM-dd", optional ? current.getHireDate() : null));
        user.setDepartmentId(readOptionalInteger("department_id", optional ? current.getDepartmentId() : null));
        user.setPositionId(readOptionalInteger("position_id", optional ? current.getPositionId() : null));
        user.setManagerId(readOptionalInteger("manager_id", optional ? current.getManagerId() : null));
        user.setBaseSalary(readOptionalDecimal("luong co ban", optional ? current.getBaseSalary() : BigDecimal.ZERO));
        user.setStatus(readOptionalChoice("trang thai nhan vien", optional ? current.getStatus() : "ACTIVE",
                "ACTIVE", "INACTIVE", "RESIGNED"));
    }

    private void inputEmployeeProfile(User user, boolean optional) throws IOException {
        inputEmployeeProfile(user, optional, new User());
    }

    private String readRequired(String fieldName) throws IOException {
        while (true) {
            System.out.print("Nhap " + fieldName + ": ");
            String value = netIn.readLine();
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
            showError(fieldName + " không được để trống");
        }
    }

    private String readOptional(String fieldName, String currentValue) throws IOException {
        System.out.print("Nhập " + fieldName + " mới [" + valueOrEmpty(currentValue) + "]: ");
        String value = netIn.readLine();
        if (value == null || value.trim().isEmpty()) {
            return currentValue;
        }
        return value.trim();
    }

    private String readEmail(String fieldName) throws IOException {
        while (true) {
            String value = readRequired(fieldName);
            if (isValidEmail(value)) {
                return value;
            }
            showError("Email không hợp lệ.");
        }
    }

    private String readOptionalEmail(String fieldName, String currentValue) throws IOException {
        while (true) {
            String value = readOptional(fieldName, currentValue);
            if (value == null || value.trim().isEmpty() || isValidEmail(value)) {
                return value;
            }
            showError("Email không hợp lệ.");
        }
    }

    private Boolean readOptionalBoolean(String fieldName, boolean currentValue) throws IOException {
        while (true) {
            String value = readOptional(fieldName + " (Y/N)", currentValue ? "Y" : "N");
            value = value.trim().toUpperCase();
            if ("Y".equals(value)) {
                return true;
            }
            if ("N".equals(value)) {
                return false;
            }
            showError("Vui lòng nhập Y hoặc N");
        }
    }

    private Date readOptionalDate(String fieldName, java.util.Date currentValue) throws IOException {
        while (true) {
            String current = currentValue == null ? "" : currentValue.toString();
            String value = readOptional(fieldName, current);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            try {
                return Date.valueOf(value.trim());
            } catch (IllegalArgumentException e) {
                showError("Ngày không hợp lệ, đang dùng kiểu định dạng yyyy-MM-dd");
            }
        }
    }

    private Integer readOptionalInteger(String fieldName, Integer currentValue) throws IOException {
        while (true) {
            String value = readOptional(fieldName, currentValue == null ? "" : String.valueOf(currentValue));
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                showError(fieldName + " phải nhập số nguyên.");
            }
        }
    }

    private BigDecimal readOptionalDecimal(String fieldName, BigDecimal currentValue) throws IOException {
        while (true) {
            String value = readOptional(fieldName, currentValue == null ? "" : currentValue.toPlainString());
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            try {
                return new BigDecimal(value.trim());
            } catch (NumberFormatException e) {
                showError(fieldName + " phải là só");
            }
        }
    }

    private String readOptionalChoice(String fieldName, String currentValue, String... allowedValues) throws IOException {
        while (true) {
            System.out.println("Giá trị hợp lệ cho " + fieldName + ": " + String.join(", ", allowedValues));
            String value = readOptional(fieldName, currentValue);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            value = value.trim().toUpperCase();
            for (String allowed : allowedValues) {
                if (allowed.equals(value)) {
                    return value;
                }
            }
            showError(fieldName + " không hợp lệ");
        }
    }

    private String readOptionalRole(String currentRole) throws IOException {
        while (true) {
            String role = readOptional("role", currentRole).toUpperCase();
            if (isValidRole(role)) {
                return role;
            }
            showError("Role không hợp lệ");
        }
    }

    private boolean isValidRole(String value) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEmail(String value) {
        return EMAIL_PATTERN.matcher(value).matches();
    }

    private String valueOrEmpty(String value) {
        if (value == null || "null".equals(value)) {
            return "";
        }
        return value;
    }
}
