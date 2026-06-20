package view.RecruitmentManagement;

import controller.recruimentManagement.CreatePostRecruimentController;
import model.Recruitment.Employer;
import model.Recruitment.JobPosting;
import view.View;

import static controller.MainController.currentUser;
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
            cprc.handleMainMenuCommand(question);

        }
    }
    public void enterJobPostingDetails() throws Exception {
        rp = new JobPosting();
        rp.setEmployer((Employer) currentUser);
        rp.setTitle(handleParam("tiêu đề"));
        rp.setDescription(handleParam("mô tả"));
        rp.setRequiment(handleParam("yêu cầu"));
        rp.setSalary(handleDouleParam("Luong"));
        rp.setDayEnd(handleDate("ngày hết hạn (vd : 2026-06-18):"));

        displayFormOptions();
    }

    public void displayFormOptions()throws Exception{
        printList(new String[]{"lưu nháp","gửi form","chỉnh sửa"});
        cprc.processFormOptions(handleParam("Nhập lệnh: "),rp);
    }

    public void displayEditOptions(JobPosting draf,String title) throws Exception {
        showMessage("----------"+title+"------------");
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
        cprc.handleEditDraft(handleParam("số tt cần chỉnh sửa hoặc các chức năng khác"));
    }

    public void promptContinueDraft() throws Exception {
        showMessage(" bạn có muốn tiếp tục bản nháp");
        cprc.handleContinueDraft(handleParam("Bạn có muốn tiếp tục bản nháp (Y/N): "));
    }

    public void promptSaveDraft()throws Exception {
        cprc.handleSaveDraft(handleParam("Bạn có muôn lưu nháp (Y/N):"),rp);
    }

    public void promptConfirmSubmit() throws Exception {
        cprc.handleConfirmSubmit(handleParam("Xác nhận gửi form (Y/N):"),rp);
    }

    public void promptSaveDraft(JobPosting draf)throws Exception {
        cprc.handleSaveDraft(handleParam("Bạn có muôn lưu nháp (Y/N):"),draf);
    }

    public void promptConfirmSubmit(JobPosting draf) throws Exception {
        cprc.handleConfirmSubmit(handleParam("Xác nhận gửi form (Y/N):"),draf);
    }
}