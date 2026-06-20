package controller.recruimentManagement;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.InterviewSchedule;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.ScheduleInterviewView;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleInterviewController {
    private ScheduleInterviewView view;
    private AddressEnum address = AddressEnum.ScheduleInterview;

    private JobPosting selectedJobPosting;
    private JobApplication selectedApplication;
    private InterviewSchedule draftSchedule;

    public ScheduleInterviewController() {
        view = new ScheduleInterviewView(this);
    }

    public boolean navigateTo() throws Exception {
        if (!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER.toString())) {
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
                List<JobPosting> jobPostings = JobPosting.findAll();
                view.displayJobPostingList(jobPostings);
                break;
            case "2":
                if (selectedJobPosting == null) {
                    view.showError("Vui lòng chọn tin tuyển dụng trước (lệnh 1).");
                } else {
                    List<JobApplication> acceptedApps = JobApplication.findByJobPosting(selectedJobPosting.getPostId())
                            .stream()
                            .filter(app -> "ACCEPTED".equals(app.getStatus()))
                            .collect(Collectors.toList());
                    view.displayAcceptedCandidates(acceptedApps);
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

    public void selectCandidate(JobApplication application) throws Exception {
        this.selectedApplication = application;
        draftSchedule = InterviewSchedule.findDraftByApplication(application.getApplicationId());
        if (draftSchedule == null) {
            draftSchedule = new InterviewSchedule();
            draftSchedule.setApplication(application);
            draftSchedule.setStatus("DRAFT");
        }
        view.enterInterviewDetails(draftSchedule);
    }

    public void processFormOptions(String option, InterviewSchedule schedule) throws Exception {
        this.draftSchedule = schedule;
        switch (option) {
            case "1":
                view.promptSaveDraft(schedule);
                break;
            case "2":
                view.promptConfirmSubmit(schedule);
                break;
            case "3":
                view.displayEditOptions(schedule, "Chỉnh sửa lịch phỏng vấn");
                break;
            case "0":
                view.showMessage("Thoát form.");
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                view.displayFormOptions(schedule);
                break;
        }
    }

    public void handleSaveDraft(String choice, InterviewSchedule schedule) throws Exception {
        if (choice.toUpperCase().equals("Y")) {
            if (!schedule.checkValid()) {
                view.showMessage("Dữ liệu không hợp lệ (thiếu thông tin). Vui lòng nhập lại.");
                view.enterInterviewDetails(schedule);
                return;
            }
            if (!schedule.saveDraft()) {
                view.showMessage("Lưu nháp thất bại.");
                return;
            }
            view.showMessage("Lưu nháp thành công.");
            view.displayScheduleDetail(schedule);
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Hủy lưu nháp.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptSaveDraft(schedule);
    }

    public void handleConfirmSubmit(String choice, InterviewSchedule schedule) throws Exception {
        if (choice.toUpperCase().equals("Y")) {
            if (!schedule.checkValid()) {
                view.showMessage("Dữ liệu không hợp lệ. Vui lòng nhập lại.");
                view.enterInterviewDetails(schedule);
                return;
            }
            if (!schedule.save()) {
                view.showMessage("Gửi lịch thất bại.");
                return;
            }
            view.showMessage("Đã gửi lịch phỏng vấn chính thức.");
            selectedApplication.setStatus("INTERVIEW_SCHEDULED");
            selectedApplication.update();
            view.displayScheduleDetail(schedule);
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Hủy gửi lịch.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptConfirmSubmit(schedule);
    }

    public void handleContinueDraft(String choice, InterviewSchedule schedule) throws Exception {
        if (choice.toUpperCase().equals("Y")) {
            view.displayEditOptions(schedule, "Tiếp tục bản nháp");
            return;
        }
        if (choice.toUpperCase().equals("N")) {
            view.showMessage("Thoát nháp.");
            return;
        }
        view.showError("Lệnh không hợp lệ");
        view.promptContinueWithDraft(schedule);
    }

    public void handleEditDraft(String choice) throws Exception {
        if (draftSchedule == null) {
            view.showError("Không có bản nháp để chỉnh sửa.");
            return;
        }
        switch (choice) {
            case "1":
                draftSchedule.setInterviewTime(view.handleDateTime("Thời gian mới (vd: 2026-06-25 14:30): ").toLocalDateTime());
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
            case "2":
                draftSchedule.setLocation(view.handleParam("Địa điểm mới: "));
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
            case "3":
                draftSchedule.setFormat(view.handleParam("Hình thức mới (ONLINE/OFFLINE): "));
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
            case "4":
                draftSchedule.setInterviewer(view.handleParam("Người phỏng vấn mới: "));
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
            case "5":
                draftSchedule.setNote(view.handleParam("Ghi chú mới: "));
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
            case "6":
                view.showMessage("Thoát chỉnh sửa.");
                break;
            case "7":
                view.promptSaveDraft(draftSchedule);
                break;
            case "8":
                view.promptConfirmSubmit(draftSchedule);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                view.displayEditOptions(draftSchedule, "Bản nháp");
                break;
        }
    }
}