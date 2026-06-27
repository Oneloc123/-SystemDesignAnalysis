package controller.admin;

import model.admin.AdminUser;
import model.admin.Attendance;
import model.admin.Role;
import dao.admin.AdminUserDao;
import dao.admin.RoleDao;
import view.admin.AdminConsoleView;

import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private final AdminUserDao adminUserDao;
    private final RoleDao roleDao;
    private final AdminConsoleView view;

    public AdminController() {
        this.adminUserDao = new AdminUserDao();
        this.roleDao = new RoleDao();
        this.view = new AdminConsoleView();
    }

    public void run() {
        view.showBanner();

        while (true) {
            view.showMenu();
            int choice = view.readInt("Lựa chọn", 0, 9);

            try {
                switch (choice) {
                    case 0 -> {
                        view.printMessage("Đã thoát Admin Site.");
                        return;
                    }
                    case 1 -> viewProfile();
                    case 2 -> viewSchedule();
                    case 3 -> changePassword();
                    case 4 -> createAccount();
                    case 5 -> listAccounts();
                    case 6 -> updateAccount();
                    case 7 -> assignRole();
                    case 8 -> searchAccounts();
                    case 9 -> deactivateAccount();
                    default -> view.printError("Lệnh không hợp lệ.");
                }
            } catch (SQLException e) {
                view.printError("Lỗi database: " + e.getMessage());
            }
        }
    }

    private void viewProfile() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản", 1, Integer.MAX_VALUE);
        AdminUser user = adminUserDao.findById(id);
        if (user == null) {
            view.printError("Không tìm thấy tài khoản.");
        } else {
            view.printUserDetail(user);
        }
    }

    private void viewSchedule() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản", 1, Integer.MAX_VALUE);
        List<Attendance> attendances = adminUserDao.ATT_findByUserId(id);
        view.printAttendanceSchedule(attendances);
    }

    private void changePassword() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản", 1, Integer.MAX_VALUE);
        AdminUser user = adminUserDao.findById(id);
        if (user == null) {
            view.printError("Không tìm thấy tài khoản.");
            return;
        }

        String password = view.readRequiredString("Mật khẩu mới");
        if (adminUserDao.changePassword(id, password)) {
            view.printSuccess("Đổi mật khẩu thành công.");
        } else {
            view.printError("Đổi mật khẩu thất bại.");
        }
    }

    private void createAccount() throws SQLException {
        AdminUser newUser = new AdminUser();
        newUser.setUsername(view.readRequiredString("Username"));
        newUser.setPasswordHash(view.readRequiredString("Password"));
        List<Role> roles = roleDao.findAll();
        int selectedRoleIndex = view.readRoleChoice(roles);
        newUser.setRoleId(roles.get(selectedRoleIndex).getRoleId());
        newUser.setActive(view.readYesNo("Kích hoạt tài khoản"));
        if (adminUserDao.create(newUser)) {
            view.printSuccess("Cấp tài khoản thành công.");
        } else {
            view.printError("Cấp tài khoản thất bại.");
        }
    }

    private void listAccounts() throws SQLException {
        view.printUserList(adminUserDao.findAll());
    }

    private void updateAccount() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản cần sửa", 1, Integer.MAX_VALUE);
        AdminUser current = adminUserDao.findById(id);
        if (id == 0){ view.showMenu();}
        if (current == null) {
            view.printError("Không tìm thấy tài khoản.");
            return;
        }

        AdminUser updated = new AdminUser();
        updated.setUserId(current.getUserId());
        updated.setUsername(view.readOptionalString("Username", current.getUsername()));
        updated.setPasswordHash(view.readOptionalString("Password", current.getPasswordHash()));
        List<Role> roles = roleDao.findAll();
        int selectedRoleIndex = view.readRoleChoice(roles);
        updated.setRoleId(roles.get(selectedRoleIndex).getRoleId());
        updated.setActive(view.readYesNo("Kích hoạt tài khoản"));

        if (adminUserDao.update(updated)) {
            view.printSuccess("Cập nhật thông tin tài khoản thành công.");
        } else {
            view.printError("Cập nhật thất bại.");
        }
    }

    private void assignRole() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản cần phân quyền", 1, Integer.MAX_VALUE);
        List<Role> roles = roleDao.findAll();
        int selectedRoleIndex = view.readRoleChoice(roles);
        int roleId = roles.get(selectedRoleIndex).getRoleId();
        if (adminUserDao.assignRole(id, roleId)) {
            view.printSuccess("Phân quyền thành công.");
        } else {
            view.printError("Phân quyền thất bại.");
        }
    }

    private void searchAccounts() throws SQLException {
        String keyword = view.readRequiredString("Từ khóa tìm kiếm");
        view.printUserList(adminUserDao.searchByUsername(keyword));
    }

    private void deactivateAccount() throws SQLException {
        Long id = (long) view.readInt("ID tài khoản cần khóa", 1, Integer.MAX_VALUE);
        if (adminUserDao.deactivate(id)) {
            view.printSuccess("Đã khóa tài khoản.");
        } else {
            view.printError("Không thể khóa tài khoản.");
        }
    }
}
