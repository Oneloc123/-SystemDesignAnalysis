package view.profileManagement;

import controller.profileManagement.EditProfileController;
import model.User;
import view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EditProfileView extends View {

    private EditProfileController epc;
    User user;

    public EditProfileView(EditProfileController epc) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.epc = epc;
    }

    @Override
    public void show() throws Exception {

        loop:
        while(true) {
            handleForm();
            printAddress();
            handleInput();
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
        }
    }

    private void handleForm() throws Exception {

    }
}
