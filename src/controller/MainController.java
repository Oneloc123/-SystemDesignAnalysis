package controller;

import enumModel.AddressEnum;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static User currentUser;
    void setCurrentUser(User currentUser) {
        MainController.currentUser = currentUser;
    }
    public static List<AddressEnum> addresses = new ArrayList<>();
}
