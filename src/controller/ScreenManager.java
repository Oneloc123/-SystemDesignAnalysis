package controller;

import controller.base.Controller;
import controller.profileManagement.ProfileController;
import model.User;

import java.util.Stack;

public class ScreenManager {
    private static Stack<Controller> listView = new Stack<>();
    private static User currentUser;

    public static void init(User user) {
        currentUser = user;
    }

    public static boolean navigateTo(String screenName) {
        try {
            switch (screenName) {
                case "Profile":
                    ProfileController pc = new ProfileController();
                    listView.push(pc);
                    pc.showOn();
                    break;
                case "Schedule":
                    ScheduleController sc = new ScheduleController();
                    listView.push(sc);
                    sc.showOn();
                    break;
                case "ChangePassword":
                    ChangePasswordController cpc = new ChangePasswordController();
                    listView.push(cpc);
                    cpc.showOn();
                    break;
                case "EmployeeList":
                    EmployeeListController elc = new EmployeeListController();
                    listView.push(elc);
                    elc.showOn();
                    break;
                case "Attendance":
                    AttendanceController ac = new AttendanceController();
                    listView.push(ac);
                    ac.showOn();
                    break;
                default:
                    return false;
            }
            ScreenManager.back();
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi điều hướng: " + e.getMessage());
            return false;
        }
    }

    public static void back() {
        if (!listView.isEmpty()) {
            Controller c = listView.pop();
            c.showOff();
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
