package test;

import controller.HomeController;
import model.RoleEnum;
import view.HomeView;

public class HomeTest {
    public static void main(String[] args) throws Exception {
       HomeController hc = new HomeController();
       hc.show();
//        RoleEnum role = RoleEnum.ACCOUNT;
//        System.out.println(role);
    }

}
