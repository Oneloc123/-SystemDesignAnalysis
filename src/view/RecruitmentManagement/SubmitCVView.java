package view.RecruitmentManagement;

import controller.MainController;
import controller.recruimentManagement.SubmitCVController;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class SubmitCVView extends View {
    private SubmitCVController controller;

    public SubmitCVView(SubmitCVController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Tạo hồ sơ ứng tuyển mới", "Xem bản nháp CV"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.handleMainMenuCommand(question);
        }
        return false;
    }

    // Thêm phương thức hiển thị danh sách job
    public void displayJobPostingList(List<JobPosting> jobPostings) throws Exception {
        showMessage("Chọn tin tuyển dụng bạn muốn ứng tuyển:");
        int index = 1;
        for (JobPosting jp : jobPostings) {
            showMessage(index++ + ". " + jp.getTitle() + " (ID: " + jp.getPostId() + ")");
        }
        int choice = Integer.parseInt(handleParam("Nhập số thứ tự: "));
        controller.selectJobPosting(jobPostings.get(choice - 1));
    }

    public void enterApplicationDetails() throws Exception {
        JobApplication application = new JobApplication();
        JobPosting selected = controller.getSelectedJobPosting();
        if (selected == null) {
            showError("Chưa chọn tin tuyển dụng. Vui lòng quay lại.");
            return;
        }
        application.setJobPosting(selected);
        application.setFullName(handleParam("Họ và tên"));
        application.setEmail(handleParam("Email"));
        application.setPhone(handleParam("Số điện thoại"));
        application.setCoverLetter(handleParam("Thư xin việc (cover letter)"));
        application.setCvFilePath(handleParam("Đường dẫn file CV (ví dụ: /cv/nguyenvanA.pdf)"));
        // Gán candidate hiện tại
        application.setCandidate((model.Recruitment.Candidate) MainController.currentUser);
        displayFormOptions();
    }

    public void displayFormOptions() throws Exception {
        printList(new String[]{"Lưu nháp", "Gửi hồ sơ", "Chỉnh sửa"});
        controller.processFormOptions(handleParam("Nhập lệnh: "), new JobApplication());
    }

    public void displayDraft(JobApplication draft) throws Exception {
        showMessage("-----------------------------");
        showMessage("----Họ tên          : " + draft.getFullName());
        showMessage("----Email           : " + draft.getEmail());
        showMessage("----Số điện thoại   : " + draft.getPhone());
        showMessage("----Thư xin việc    : " + draft.getCoverLetter());
        showMessage("----Đường dẫn CV    : " + draft.getCvFilePath());
        showMessage("----Trạng thái      : " + draft.getStatus());
        showMessage("-----------------------------");
        promptContinueDraft();
    }

    public void displayEditOptions(JobApplication draft, String title) throws Exception {
        showMessage("---------- " + title + " ------------");
        showMessage("Nhập STT mục cần chỉnh sửa (vd: 1):");
        showMessage("----1. Họ và tên        : " + draft.getFullName());
        showMessage("----2. Email            : " + draft.getEmail());
        showMessage("----3. Số điện thoại    : " + draft.getPhone());
        showMessage("----4. Thư xin việc     : " + draft.getCoverLetter());
        showMessage("----5. Đường dẫn CV     : " + draft.getCvFilePath());
        showMessage("------------------------------");
        showMessage("----6. Thoát");
        showMessage("----7. Lưu nháp");
        showMessage("----8. Gửi hồ sơ");
        showMessage("------------------------------");
        controller.handleEditDraft(handleParam("Số TT cần sửa hoặc chức năng khác: "));
    }

    public void promptContinueDraft() throws Exception {
        showMessage("Bạn có muốn tiếp tục với bản nháp này không?");
        controller.handleContinueDraft(handleParam("(Y/N): "));
    }

    public void promptSaveDraft() throws Exception {
        controller.handleSaveDraft(handleParam("Bạn có muốn lưu nháp (Y/N): "), new JobApplication());
    }

    public void promptConfirmSubmit() throws Exception {
        controller.handleConfirmSubmit(handleParam("Xác nhận gửi hồ sơ (Y/N): "), new JobApplication());
    }

    public void promptSaveDraft(JobApplication draft) throws Exception {
        controller.handleSaveDraft(handleParam("Bạn có muốn lưu nháp (Y/N): "), draft);
    }

    public void promptConfirmSubmit(JobApplication draft) throws Exception {
        controller.handleConfirmSubmit(handleParam("Xác nhận gửi hồ sơ (Y/N): "), draft);
    }
}