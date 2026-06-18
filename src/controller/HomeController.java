package controller;

import controller.profileManagement.ProfileController;
import model.User;
import view.HomeView;

public class HomeController {
    static User currentUser;
    HomeView hv;
    // login thanhf confg
    void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public HomeController() {
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
                System.out.println("Chuc nang 2 dang thuc hien");
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

        if(!cc.execute(currentUser)){
            hv.showError("Khong co quyen");
        }
    }

    public void handleProfile() throws Exception {
        ProfileController pc = new ProfileController();

        pc.showEmployeeList();
    }
}
