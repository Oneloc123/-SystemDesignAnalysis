package view;

import controller.LoginController;
import controller.MainController;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        MainController.addresses = new ArrayList<>();

        LoginController loginController = new LoginController();
        loginController.show();
    }
}
