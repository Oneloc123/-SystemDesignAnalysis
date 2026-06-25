package controller;

import enumModel.RoleEnum;
import model.Recruitment.Employer;
import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;

    public HomeController() {
        if (MainController.currentUser == null) {
            MainController.currentUser = new Employer();
            MainController.currentUser.setRole(RoleEnum.valueOf(RoleEnum.EMPLOYER.toString()));
        }
        this.hv = new HomeView(this);
    }

    public void show() throws Exception {
        hv.show();
    }

    public void executeCommand(String question) throws Exception {
        switch(question) {
            case "1":
                functionViewMyProfile();
                break;
            case "2":
                functionViewSchedule();
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
                if (!ScreenManager.navigateTo("RecruitmentManagement")) {
                    hv.showError("Không có quyền truy cập chức năng quản lý tuyển dụng");
                }
                break;
            case "7":
                ScreenManager.navigateTo("CalcSalary");
                break;
            case "8":
                if (!ScreenManager.navigateTo("ContractManagement")) {
                    hv.showError("Không có quyền truy cập chức năng quản lý hợp đồng");
                }
                break;
            default:
                hv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void functionViewSchedule() {
        ScreenManager.navigateTo("Schedule");
    }

    public void functionViewMyProfile() {
        ScreenManager.navigateTo("MyProfile");
    }



}
