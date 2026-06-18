package view.RecruitmentManagement;

import controller.recruimentManagement.ReviewApplicationsController;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class ReviewApplicationsView extends View {
    private ReviewApplicationsController controller;

    public ReviewApplicationsView(ReviewApplicationsController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Xem danh sách tin tuyển dụng", "Xem danh sách hồ sơ theo tin"});
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

    public void displayJobPostingList(List<JobPosting> jobPostings) throws Exception {
        showMessage("Danh sách tin tuyển dụng:");
        int index = 1;
        for (JobPosting jp : jobPostings) {
            showMessage(index++ + ". " + jp.getTitle() + " (ID: " + jp.getPostId() + ")");
        }
        int choice = Integer.parseInt(handleParam("Chọn tin tuyển dụng (nhập số thứ tự): "));
        controller.selectJobPosting(jobPostings.get(choice - 1));
    }

    public void displayApplicationList(List<JobApplication> applications) throws Exception {
        if (applications.isEmpty()) {
            showError("Không có hồ sơ nào cho tin này.");
            return;
        }
        showMessage("Danh sách hồ sơ ứng tuyển:");
        int index = 1;
        for (JobApplication app : applications) {
            showMessage(index++ + ". " + app.getFullName() + " - " + app.getStatus());
        }
        int choice = Integer.parseInt(handleParam("Chọn hồ sơ để xem chi tiết (nhập số thứ tự): "));
        controller.selectApplication(applications.get(choice - 1));
    }

    public void displayApplicationDetail(JobApplication application) throws Exception {
        showMessage("----------------------------");
        showMessage("Họ tên        : " + application.getFullName());
        showMessage("Email         : " + application.getEmail());
        showMessage("SĐT           : " + application.getPhone());
        showMessage("CV            : " + application.getCvFilePath());
        showMessage("Thư xin việc  : " + application.getCoverLetter());
        showMessage("Trạng thái    : " + application.getStatus());
        showMessage("Ngày nộp      : " + application.getSubmittedDate());
        showMessage("Bài đăng      : " + application.getJobPosting().getTitle());
        showMessage("----------------------------");
        showReviewOptions();
    }

    public void showReviewOptions() throws Exception {
        printList(new String[]{"Chấp nhận (ACCEPT)", "Từ chối (REJECT)", "Thêm ghi chú", "Quay lại"});
        controller.handleReviewOption(handleParam("Chọn hành động: "));
    }

    public void promptReviewNote() throws Exception {
        String note = handleParam("Nhập ghi chú đánh giá: ");
        controller.saveReviewNote(note);
    }

    public void showReviewSuccess(String status) {
        showMessage("Đã cập nhật trạng thái thành " + status);
    }

    public void showError(String message) {
        super.showError(message);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}