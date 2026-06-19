package controller;

import controller.profileManagement.ProfileController;
import controller.recruimentManagement.RecruitmentManagementController;
import enumModel.RoleEnum;
import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;
    // login thanhf confg

    public HomeController() {
        MainController.currentUser = new User();
        MainController.currentUser.setRole(RoleEnum.EMPLOYER);
        this.hv = new HomeView(this);
    }
    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) throws Exception {
        switch(question) {
            case "1":
                hv.printAddress();
                System.out.println("Chuc nang 1 dang thuc hien");
                // thuc thi 1
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
    public void function(){
        CalcSalaryController cc = new CalcSalaryController();
        if(!cc.execute(MainController.currentUser)){
            hv.showError("Khong co quyen");
        }
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
