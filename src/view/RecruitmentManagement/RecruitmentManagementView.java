package view.RecruitmentManagement;

import controller.recruitmentManagement.RecruitmentManagementController;
import view.View;

import static controller.MainController.printList;

public class RecruitmentManagementView extends View {
    RecruitmentManagementController rmc;
    private String[] funcs = {"Tạo bài đăng tuyển dụng", "Xét duyệt hồ sơ", "Lên lịch phỏng vấn", "Đánh giá phỏng vấn"};

    public RecruitmentManagementView(RecruitmentManagementController rmc){
        this.rmc = rmc;
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
            rmc.executeCommand(question);
        }
    }

}
