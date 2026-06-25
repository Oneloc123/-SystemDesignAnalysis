package controller.recruimentManagement;

import controller.MainController;
import dao.recruitment.ApplicationReviewDAO;
import dao.recruitment.JobApplicationDAO;
import dao.recruitment.JobPostingDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.ApplicationReview;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.ReviewApplicationsView;

import java.util.List;


public class ReviewApplicationsController {
    private ReviewApplicationsView view;
    private AddressEnum address = AddressEnum.ReviewApplications;
    private JobPostingDAO jobPostingDAO;
    private JobApplicationDAO jobApplicationDAO;
    private ApplicationReviewDAO applicationReviewDAO;

    private JobPosting selectedJobPosting;
    private JobApplication selectedApplication;
    private ApplicationReview currentReview;

    public ReviewApplicationsController() {
        view = new ReviewApplicationsView(this);
        this.jobPostingDAO = new JobPostingDAO();
        this.jobApplicationDAO = new JobApplicationDAO();
        this.applicationReviewDAO = new ApplicationReviewDAO();
    }

    public boolean navigateTo() throws Exception {
        if (MainController.currentUser.getRole() != RoleEnum.EMPLOYER) {
            return false;
        }
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void handleMainMenuCommand(String command) throws Exception {
        switch (command) {
            case "1":
                List<JobPosting> jobPostings = jobPostingDAO.findAll();
                view.displayJobPostingList(jobPostings);
                break;
            case "2":
                if (selectedJobPosting == null) {
                    view.showError("Vui lòng chọn tin tuyển dụng trước (lệnh 1).");
                } else {
                    List<JobApplication> applications = jobApplicationDAO.findByJobPosting(selectedJobPosting.getPostId());
                    view.displayApplicationList(applications);
                }
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void selectJobPosting(JobPosting jobPosting) {
        this.selectedJobPosting = jobPosting;
        view.showMessage("Đã chọn: " + jobPosting.getTitle());
    }

    public void selectApplication(JobApplication application) throws Exception {
        this.selectedApplication = application;
        // Tải review nếu có
        currentReview = applicationReviewDAO.findByApplication(application.getApplicationId());
        view.displayApplicationDetail(application);
    }

    public void handleReviewOption(String option) throws Exception {
        if (selectedApplication == null) {
            view.showError("Chưa chọn hồ sơ.");
            return;
        }
        switch (option) {
            case "1": // ACCEPT
                updateApplicationStatus("ACCEPTED");
                break;
            case "2": // REJECT
                updateApplicationStatus("REJECTED");
                break;
            case "3":
                view.promptReviewNote();
                break;
            case "4":
                view.showMessage("Quay lại");
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                view.showReviewOptions();
                break;
        }
    }

    public void saveReviewNote(String note) throws Exception {
        if (selectedApplication == null) {
            view.showError("Chưa chọn hồ sơ.");
            return;
        }
        if (currentReview == null) {
            currentReview = new ApplicationReview();
            currentReview.setApplication(selectedApplication);
            currentReview.setReviewer(MainController.currentUser);
        }
        currentReview.setStatus("PENDING_REVIEW");
        currentReview.setNote(note);
        if (applicationReviewDAO.save(currentReview)) {
            view.showMessage("Đã lưu ghi chú.");
            selectedApplication.setStatus("REVIEWING");
            jobApplicationDAO.update(selectedApplication);
            view.displayApplicationDetail(selectedApplication);
        } else {
            view.showError("Lưu ghi chú thất bại.");
        }
    }

    private void updateApplicationStatus(String status) throws Exception {
        if (selectedApplication == null) return;
        selectedApplication.setStatus(status);
        if (!jobApplicationDAO.update(selectedApplication)) {
            view.showError("Cập nhật trạng thái thất bại.");
            return;
        }
        if (currentReview == null) {
            currentReview = new ApplicationReview();
            currentReview.setApplication(selectedApplication);
            currentReview.setReviewer(MainController.currentUser);
        }
        currentReview.setStatus(status);
        currentReview.setNote(currentReview.getNote() != null ? currentReview.getNote() : "Đánh giá tự động");
        applicationReviewDAO.save(currentReview);
        view.showReviewSuccess(status);
        view.displayApplicationDetail(selectedApplication);
    }
}