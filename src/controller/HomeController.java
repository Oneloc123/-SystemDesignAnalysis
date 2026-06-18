package controller;

import controller.recruimentManagement.RecruitmentManagementController;
import enumModel.RoleEnum;
import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;

    public HomeController() {
        MainController.currentUser = User.getMockData().get(2);
        ScreenManager.init(MainController.currentUser);
        this.hv = new HomeView(this);
    }

    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) throws Exception {
        switch(question) {
            case "1":
                ScreenManager.navigateTo("Profile");
                break;
            case "2":
                ScreenManager.navigateTo("Schedule");
                break;
            case "3":
                ScreenManager.navigateTo("ChangePassword");
                break;
            case "4":
                ScreenManager.navigateTo("EmployeeList");
                break;
            case "5":
                ScreenManager.navigateTo("Attendance");
                break;
            case "6":
                functionRecruitmentManagement();
                break;
            case "7":
                CalcSalaryController cc = new CalcSalaryController();
                if(!cc.execute(MainController.currentUser)){
                    hv.showError("Khong co quyen");
                }
                break;
            default:
                hv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void functionRecruitmentManagement() throws Exception {
        RecruitmentManagementController rmc = new RecruitmentManagementController();
        boolean result = rmc.navigate();
        if(!result){
            hv.showError("Không có quyền truy cập");
        }
    }
}
