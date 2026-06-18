package view.RecruitmentManagement;

import controller.recruimentManagement.CreatePostRecruimentController;
import model.Recruitment.JobPosting;
import view.View;

import static controller.MainController.printList;

public class CreatePostRecruimentView extends View {
    private CreatePostRecruimentController cprc;
    private JobPosting rp;

    public CreatePostRecruimentView(CreatePostRecruimentController cprc){
        this.cprc = cprc;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            printList(new String[]{"nhập form tạo tin","Xem bản nháp"});
            printAddress();
            handleInput();
            if(question.equals("0")) {
                System.out.println("Thoat thanh cong");
                break loop;
            }
            cprc.excuteCommand(question);

        }
    }
    public void handleForm() throws Exception {
        rp = new JobPosting();
        rp.setTitle(handleParam("tiêu đề"));
        rp.setDescription(handleParam("mô tả"));
        rp.setRequiment(handleParam("yêu cầu"));
        rp.setSalary(handleDouleParam("Luong"));
        rp.setDayEnd(java.sql.Date.valueOf(handleParam("ngày hết hạn (vd : 2026-06-18):")));

        executeIsSaveDraf();
        executeIsConfirm();
    }

    public void printDraf(JobPosting draf) throws Exception {
        showMessage("----------Bản nháp-----------");
        showMessage("----Tiêu đề       :"+draf.getTitle());
        showMessage("----Mô tả         :"+draf.getDescription());
        showMessage("----Yêu cầu       :"+draf.getRequiment());
        showMessage("----Mức lương     :"+draf.getSalary());
        showMessage("----Ngày kết thúc :"+draf.getDayEnd());
        showMessage("------------------------------");
        isContinueDraf();
    }

    public void handleDraf(JobPosting draf) throws Exception {
        showMessage("----------Chỉnh sửa nháp------------");
        showMessage("Nhập stt input cần chỉnh sửa (vd: 1 ):");
        showMessage("----1. Tiêu đề       :"+draf.getTitle());
        showMessage("----2. Mô tả         :"+draf.getDescription());
        showMessage("----3. Yêu cầu       :"+draf.getRequiment());
        showMessage("----4. Mức lương     :"+draf.getSalary());
        showMessage("----5. Ngày kết thúc :"+draf.getDayEnd());
        showMessage("------------------------------");
        showMessage("----6.thoát");
        showMessage("----7.lưu nháp");
        showMessage("----8.gửi form");
        showMessage("------------------------------");
        cprc.handleEditDraf(handleParam("số tt cần chỉnh sửa hoặc các chức năng khác"));
    }

    public void isContinueDraf() throws Exception {
        showMessage(" bạn có muốn tiếp tục bản nháp");
        cprc.executeContinueDraf(handleParam("Bạn có muốn tiếp tục bản nháp (Y/N): "));
    }

    public void executeIsSaveDraf()throws Exception {
        cprc.executeSaveDarf(handleParam("Bạn có muôn lưu nháp (Y/N):"),rp);
    }

    public void executeIsConfirm() throws Exception {
        cprc.excecuteConfirm(handleParam("Xác nhận gửi form (Y/N):"),rp);
    }

    public void executeIsSaveDraf(JobPosting draf)throws Exception {
        cprc.executeSaveDarf(handleParam("Bạn có muôn lưu nháp (Y/N):"),draf);
    }

    public void executeIsConfirm(JobPosting draf) throws Exception {
        cprc.excecuteConfirm(handleParam("Xác nhận gửi form (Y/N):"),draf);
    }
}
