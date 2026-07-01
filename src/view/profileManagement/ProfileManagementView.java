package view.profileManagement;

import controller.profileManagement.ProfileController;
import view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static controller.MainController.printList;

public class ProfileManagementView extends View {

    ProfileController pc;
    static BufferedReader netIn;

    private String address = "Home/Profile";
    private String[] funcs = {"EDIT PROFILE","VIEW EMPLOYEE PROFILE","CREATE NEW PROFILE"};

    public ProfileManagementView(ProfileController pc) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.pc = pc;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            printAddress();
            printList(funcs);
            printAddress();
            handleInput();
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            pc.excuteComent(question);
        }
    }
}
