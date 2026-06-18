package controller;

import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;
    // login thanhf confg

    public HomeController() {
        this.hv = new HomeView(this);
    }
    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) {
        switch(question) {
            case "1":
                hv.printAddress();
                System.out.println("Chuc nang 1 dang thuc hien");
                // thuc thi 1
                break;
            case "2":
                System.out.println("Chuc nang 2 dang thuc hien");
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

    public void functionRecruitmentManagement() throws Exception {
        RecruitmentManagementController rmc = new RecruitmentManagementController();
        boolean result = rmc.navigate();
        if(!result){
            hv.showError("Không có quyền truy cập");
        }
    }

}
