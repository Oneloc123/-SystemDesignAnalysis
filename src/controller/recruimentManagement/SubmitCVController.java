package controller.recruimentManagement;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.Candidate;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.SubmitCVView;

import java.util.List;
import java.util.stream.Collectors;

public class SubmitCVController {
    private JobApplication draftApplication;
    private SubmitCVView view;
    private AddressEnum address = AddressEnum.SubmitCV;
    private JobPosting selectedJobPosting; // thêm để lưu tin đã chọn

    public SubmitCVController() {
        view = new SubmitCVView(this);
    }

    public boolean navigateTo() throws Exception {
        if (!MainController.currentUser.getRole().equals(RoleEnum.CANDIDATE.toString())) {
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
                view.displayJobPostingList(JobPosting.findAll());
                break;
            case "2":
                Candidate candidate = (Candidate) MainController.currentUser;
                List<JobApplication> drafts = candidate.getApplications().stream()
                        .filter(JobApplication::isDraft)
                        .collect(Collectors.toList());
                if (drafts.isEmpty()) {
                    view.showError("Không có bản nháp CV nào.");
                    break;
                }
                draftApplication = drafts.get(0);
                view.displayEditOptions(draftApplication, "Bản nháp CV");
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    // Phương thức được gọi từ view sau khi chọn job
    public void selectJobPosting(JobPosting job) {
        this.selectedJobPosting = job;
        try {
            view.enterApplicationDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JobPosting getSelectedJobPosting() {
        return selectedJobPosting;
    }

    public void processFormOptions(String option, JobApplication application) throws Exception {
        draftApplication = application;
        switch (option) {
            case "1":
                view.promptSaveDraft();
                break;
            case "2":
                view.promptConfirmSubmit();
                break;
            case "3":
                view.displayEditOptions(draftApplication, "Chỉnh sửa hồ sơ");
                break;
            case "0":
                view.showMessage("Thoát form");
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                view.displayFormOptions();
                break;
        }
    }

    public void handleConfirmSubmit(String choice, JobApplication application) throws Exception {
        if (choice.toUpperCase().equals("Y")) {
            if (!application.checkValid()) {
                view.showMessage("Dữ liệu không hợp lệ (thiếu thông tin hoặc chưa chọn bài đăng). Vui lòng nhập lại.");
                view.enterApplicationDetails();
                return;
            }
            if (!application.save()) {
                view.showMessage("Lưu vào database thất bại, vui lòng thử lại sau.");
                return;
            }
            view.showMessage("Gửi hồ sơ ứng tuyển thành công!");
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Hủy gửi hồ sơ.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptConfirmSubmit();
    }

    public void handleSaveDraft(String choice, JobApplication application) throws Exception {
        draftApplication = application;
        if (choice.toUpperCase().equals("Y")) {
            if (!application.checkValid()) {
                view.showMessage("Dữ liệu không hợp lệ, vui lòng nhập lại.");
                view.enterApplicationDetails();
                return;
            }
            if (!application.saveDraft()) {
                view.showMessage("Lưu nháp thất bại, vui lòng thử lại sau.");
                return;
            }
            Candidate candidate = (Candidate) MainController.currentUser;
            if (!candidate.getApplications().contains(application)) {
                candidate.addApplication(application);
            }
            view.showMessage("Lưu nháp thành công.");
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Hủy lưu nháp.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptSaveDraft(draftApplication);
    }

    public void handleContinueDraft(String choice) throws Exception {
        if (choice.toUpperCase().equals("Y")) {
            view.displayEditOptions(draftApplication, "Tiếp tục bản nháp");
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Thoát nháp.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptContinueDraft();
    }

    public void handleEditDraft(String choice) throws Exception {
        switch (choice) {
            case "1":
                draftApplication.setFullName(view.handleParam("Họ và tên mới: "));
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
            case "2":
                draftApplication.setEmail(view.handleParam("Email mới: "));
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
            case "3":
                draftApplication.setPhone(view.handleParam("Số điện thoại mới: "));
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
            case "4":
                draftApplication.setCoverLetter(view.handleParam("Thư xin việc mới: "));
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
            case "5":
                draftApplication.setCvFilePath(view.handleParam("Đường dẫn CV mới: "));
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
            case "6":
                view.showMessage("Thoát chỉnh sửa.");
                break;
            case "7":
                view.promptSaveDraft(draftApplication);
                break;
            case "8":
                view.promptConfirmSubmit(draftApplication);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                view.displayEditOptions(draftApplication, "Bản nháp");
                break;
        }
    }
}