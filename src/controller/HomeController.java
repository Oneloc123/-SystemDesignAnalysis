package controller;

import controller.profileManagement.ProfileController;
import controller.recruimentManagement.RecruitmentManagementController;
import enumModel.RoleEnum;
import model.Recruitment.Employer;
import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;
    CalcSalaryController calcSalaryController;

    public HomeController() {
        if (MainController.currentUser == null) {
            MainController.currentUser = new Employer();
            MainController.currentUser.setRole(RoleEnum.valueOf(RoleEnum.EMPLOYER.toString()));
        }
        this.hv = new HomeView(this);
        this.calcSalaryController = new CalcSalaryController();
    }

    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) throws Exception {
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
                functionRecruitmentManagement();
                break;
            case "7":
                functionContractManagement();
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

    public void function() throws Exception {
        if (!calcSalaryController.checkRole(MainController.currentUser)) {
            hv.showError("Khong co quyen");
            return;
        }
        calcSalaryController.execute(MainController.currentUser);
    }

    public void handleProfile() throws Exception {
        ProfileController pc = new ProfileController();
        boolean check = pc.navigate();
        if (!check){
            hv.showError("Không có quyền truy cập");
        }

    }

    public void functionRecruitmentManagement() throws Exception {
        RecruitmentManagementController rmc = new RecruitmentManagementController();
        boolean result = rmc.navigate();
        if(!result){
            hv.showError("Không có quyền truy cập");
        }
    }
    public void functionContractManagement() throws Exception {
        controller.contract.ContractManagementController cmc =
                new controller.contract.ContractManagementController();
        boolean result = cmc.navigate();
        if (!result) {
            hv.showError("Không có quyền truy cập chức năng quản lý hợp đồng");
        }
    }

}
