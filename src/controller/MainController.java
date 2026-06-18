package controller;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static User currentUser;
    void setCurrentUser(User currentUser) {
        MainController.currentUser = currentUser;
    }
    public static List<String> addresses = new ArrayList<>();
}
