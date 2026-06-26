package controller;

import controller.profileManagement.ScheduleController;
import controller.profileManagement.EmployeeListController;
import controller.profileManagement.AttendanceController;
import controller.profileManagement.ChangePasswordController;
import controller.profileManagement.MyProfileController;
import model.User;

public class ScreenManager {
    private static User currentUser;

    public static void init(User user) {
        currentUser = user;
    }

    public static boolean navigateTo(String screenName) {
        try {
            switch (screenName) {
                case "Schedule":
                    new ScheduleController().showOn();
                    break;
                case "EmployeeList":
                    new EmployeeListController().showOn();
                    break;
                case "Attendance":
                    new AttendanceController().showOn();
                    break;
                case "ChangePassword":
                    new ChangePasswordController().showOn();
                    break;
                case "MyProfile":
                    new MyProfileController().showOn();
                    break;
                case "ContractManagement":
                    controller.contract.ContractManagementController cmc =
                            new controller.contract.ContractManagementController();
                    cmc.navigate();
                    return true;
                default:
                    return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi điều hướng: " + e.getMessage());
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
