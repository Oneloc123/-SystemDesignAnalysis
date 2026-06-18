package view.RecruitmentManagement;

import controller.recruimentManagement.CreatePostRecruimentController;
import model.Recruitment.RecruitmentPost;
import view.View;

import java.io.IOException;

import static controller.MainController.printList;

public class CreatePostRecruimentView extends View {
    private CreatePostRecruimentController cprv;
    private RecruitmentPost rp;

    public CreatePostRecruimentView(CreatePostRecruimentController cprv){
        this.cprv = cprv;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            //
            printList(new String[]{"nhập form"," gửi form"," thoát"});
            //

            //
            handleForm();

            //
            printAddress();
            handleInput();
            //
            //exit
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            // thuc thi func

        }
    }
    public void handleForm() throws IOException {
        rp = new RecruitmentPost();
        rp.setTitle(handleParam("tiêu đề"));
        rp.setDescription(handleParam("mô tả"));
        rp.setRequiment(handleParam("yêu cầu"));
        rp.setSalary(handleDouleParam("Luong"));
        boolean valid = rp.checkValid();
        if(!valid){
            showError("dữ liệu không hợp lệ");
            handleForm();
        }
        System.out.println("nhập thành công");
    }
}
