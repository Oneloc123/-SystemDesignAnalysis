package controller;

import dao.UserDAO;
import enumModel.AddressEnum;
import model.User;
import view.LoginView;

import java.sql.SQLException;

public class LoginController {
    private final UserDAO userDAO = new UserDAO();
    public final LoginView loginView;

    private static final int MAX_ATTEMPTS = 3;

    private boolean loggedIn = false;

    public LoginController() {
        this.loginView = new LoginView(this);
    }
    public boolean start() throws SQLException {
        MainController.addresses.clear();
        MainController.addresses.add(AddressEnum.LOGIN);
        return loggedIn;
    }
    public User login(String username,String password){
        if (username == null || username.isBlank()) return null;
        if (password == null || password.isBlank()) return null;

        User user = userDAO.authenticate(username.trim(), password.trim());
        if (user != null) {
            MainController.currentUser = user;
        }
        return user;
    }
    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
