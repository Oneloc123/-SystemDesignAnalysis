//package controller;
//
//import dao.UserDAO;
//import enumModel.AddressEnum;
//import enumModel.RoleEnum;
//import model.User;
//import view.AdminView;
//
//import java.util.List;
//
//public class AdminController {
//    private final UserDAO userDAO;
//    private final AdminView adminView;
//    private final AddressEnum address = AddressEnum.AdminManagement;
//
//    public AdminController() {
//        this.userDAO = new UserDAO();
//        this.adminView = new AdminView(this);
//    }
//
//    public void show() throws Exception {
//        if (!hasAdminPermission()) {
//            adminView.showError("Không có quyền truy cập vào trang quản trị!");
//            return;
//        }
//        MainController.addresses.add(address);
//        try {
//            adminView.show();
//        } finally {
//            MainController.addresses.remove(address);
//        }
//    }
//
//    public void executeCommand(String command) throws Exception {
//        switch (command) {
//            case "1":
//                createAccount();
//                break;
//            case "2":
//                assignRole();
//                break;
//            case "3":
//                updateAccount();
//                break;
//            case "4":
//                showAccounts();
//                break;
//            case "5":
//                showAccountDetail();
//                break;
//            case "6":
//                searchAccounts();
//                break;
//            case "7":
//                deactivateAccount();
//                break;
//            default:
//                adminView.showError("Lệnh không hợp lệ. Vui lòng nhập lại.");
//                break;
//        }
//    }
//
//    private void createAccount() throws Exception {
//        User user = adminView.inputNewAccount();
//        if (userDAO.existsByUsername(user.getUsername())) {
//            adminView.showError("Tên đăng nhập đã tồn tại!");
//            return;
//        }
//        if (userDAO.save(user)) {
//            adminView.showMessage("Cấp ta khoản thành công. Mã tài khoản: " + user.getUserId());
//        } else {
//            adminView.showError("Cấp tài khoản thất bại!");
//        }
//    }
//
//    private void assignRole() throws Exception {
//        int userId = adminView.inputAccountId();
//        User user = userDAO.findById(userId);
//        if (user == null) {
//            adminView.showError("Không tìm thấy tài khoản!");
//            return;
//        }
//        user.setRole(adminView.inputRole());
//        if (userDAO.update(user)) {
//            adminView.showMessage("Phân quyền thành công.");
//        } else {
//            adminView.showError("Phân quyền thất bại!");
//        }
//    }
//
//    private void updateAccount() throws Exception {
//        int userId = adminView.inputAccountId();
//        User current = userDAO.findById(userId);
//        if (current == null) {
//            adminView.showError("Không tìm tháy tài khoản!");
//            return;
//        }
//        User updated = adminView.inputUpdatedAccount(current);
//        if (!current.getUsername().equalsIgnoreCase(updated.getUsername())
//                && userDAO.existsByUsername(updated.getUsername())) {
//            adminView.showError("Tên đăng nhập đẫ tồn tại!");
//            return;
//        }
//        if (userDAO.update(updated)) {
//            adminView.showMessage("Chỉnh sửa thông tin thành công.");
//        } else {
//            adminView.showError("Chỉnh sửa thông tin thất bại!");
//        }
//    }
//
//    private void showAccounts() {
//        List<User> users = userDAO.findAll();
//        adminView.printUsers(users);
//    }
//
//    private void showAccountDetail() throws Exception {
//        int userId = adminView.inputAccountId();
//        User user = userDAO.findById(userId);
//        if (user == null) {
//            adminView.showError("Không tìm thấy tài khoản!");
//            return;
//        }
//        adminView.printUserDetail(user);
//    }
//
//    private void searchAccounts() throws Exception {
//        String keyword = adminView.inputSearchKeyword();
//        List<User> users = userDAO.searchByUsername(keyword);
//        adminView.printUsers(users);
//    }
//
//    private void deactivateAccount() throws Exception {
//        int userId = adminView.inputAccountId();
//        User user = userDAO.findById(userId);
//        if (user == null) {
//            adminView.showError("Không tìm thấy tài khoản!");
//            return;
//        }
//        if (MainController.currentUser != null && MainController.currentUser.getUserId() == userId) {
//            adminView.showError("Không thể khoá tài khoản đang đăng nhập!");
//            return;
//        }
//        if (!adminView.confirm("Khoá tài khoản " + user.getUsername() + "?")) {
//            adminView.showMessage("Đã huỷ thao tac.");
//            return;
//        }
//        if (userDAO.delete(userId)) {
//            adminView.showMessage("Đã khoá tài khoản thành công.");
//        } else {
//            adminView.showError("Khoá tài khoản thất bại!");
//        }
//    }
//
//    private boolean hasAdminPermission() {
//        if (MainController.currentUser == null) {
//            return true;
//        }
//        return RoleEnum.ADMIN == MainController.currentUser.getRoleEnum();
//    }
//}
