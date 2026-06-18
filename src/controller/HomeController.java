package controller;

import model.User;
import view.HomeView;

public class HomeController {
    HomeView hv;
    CalcSalaryController calcSalaryController;

    public HomeController() {
        this.hv = new HomeView(this);
        this.calcSalaryController = new CalcSalaryController();
    }

    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) throws Exception {
        switch(question) {
            case "1":
                hv.printAddress();
                System.out.println("Chuc nang 1 dang thuc hien");
                function();
                break;
            case "2":
                System.out.println("Chuc nang 2 dang thuc hien");
                break;
            default:
                hv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void function() throws Exception {
        if (!calcSalaryController.checkRole(MainController.currentUser)) {
            hv.showError("Khong co quyen");
            return;
        }
        calcSalaryController.execute(MainController.currentUser);
    }

    public void functionRecruitmentManagement(){

    }

}
