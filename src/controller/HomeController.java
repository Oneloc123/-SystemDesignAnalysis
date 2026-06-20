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
        MainController.currentUser = new Employer();
        MainController.currentUser.setRole(RoleEnum.valueOf(RoleEnum.EMPLOYER.toString()));
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
                functionRecruitmentManagement();
                break;
            case "3":
                System.out.println("Chuc nang 3 dang thuc hien");
                handleProfile();
                break;
            default:
                hv.showError("Lệnh không hợp lệ");
                break;
        }
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

}
