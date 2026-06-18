package view.RecruitmentManagement;

import controller.RecruitmentManagementController;
import view.View;

import static controller.MainController.printList;

public class RecruitmentManagementView extends View {
    RecruitmentManagementController rmc;
    private String[] funcs = {"CREATE RECRUITMENT POST","CV REVIEW","SCHEDULE AN INTERVIEW"};

    public RecruitmentManagementView(RecruitmentManagementController rmc){
        this.rmc =rmc;

    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            //
            printAddress();
            printList(funcs);
            //
            printAddress();
       //     handleInput();
            //
        }
    }

}
