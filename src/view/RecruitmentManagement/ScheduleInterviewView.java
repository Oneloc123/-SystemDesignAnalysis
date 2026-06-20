package view.RecruitmentManagement;

import controller.recruimentManagement.ScheduleInterviewController;
import model.Recruitment.InterviewSchedule;
import model.Recruitment.JobApplication;
import model.Recruitment.JobPosting;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class ScheduleInterviewView extends View {
    private ScheduleInterviewController controller;

    public ScheduleInterviewView(ScheduleInterviewController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Chọn tin tuyển dụng", "Xem danh sách ứng viên đã được chấp nhận"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.handleMainMenuCommand(question);
        }
    }

    public void displayJobPostingList(List<JobPosting> jobPostings) throws Exception {
        showMessage("Danh sách tin tuyển dụng:");
        int index = 1;
        for (JobPosting jp : jobPostings) {
            showMessage(index++ + ". " + jp.getTitle() + " (ID: " + jp.getPostId() + ")");
        }
        int choice = Integer.parseInt(handleParam("Chọn tin (nhập số thứ tự): "));
        controller.selectJobPosting(jobPostings.get(choice - 1));
    }

    public void displayAcceptedCandidates(List<JobApplication> applications) throws Exception {
        if (applications.isEmpty()) {
            showError("Không có ứng viên nào được chấp nhận cho tin này.");
            return;
        }
        showMessage("Danh sách ứng viên đã được chấp nhận:");
        int index = 1;
        for (JobApplication app : applications) {
            showMessage(index++ + ". " + app.getFullName() + " - " + app.getStatus());
        }
        int choice = Integer.parseInt(handleParam("Chọn ứng viên để lập lịch (nhập số thứ tự): "));
        controller.selectCandidate(applications.get(choice - 1));
    }

    public void enterInterviewDetails(InterviewSchedule schedule) throws Exception {
        schedule.setInterviewTime(handleDateTime("Thời gian phỏng vấn (vd: 2026-06-25 14:30): ").toLocalDateTime());
        schedule.setLocation(handleParam("Địa điểm: "));
        schedule.setFormat(handleParam("Hình thức (ONLINE/OFFLINE): "));
        schedule.setInterviewer(handleParam("Người phỏng vấn: "));
        schedule.setNote(handleParam("Ghi chú (nếu có): "));
        displayFormOptions(schedule);
    }

    public void displayFormOptions(InterviewSchedule schedule) throws Exception {
        printList(new String[]{"Lưu nháp", "Gửi lịch chính thức", "Chỉnh sửa"});
        controller.processFormOptions(handleParam("Nhập lệnh: "), schedule);
    }

    public void displayScheduleDetail(InterviewSchedule schedule) throws Exception {
        showMessage("----------------------------");
        showMessage("Thời gian     : " + schedule.getInterviewTime());
        showMessage("Địa điểm      : " + schedule.getLocation());
        showMessage("Hình thức     : " + schedule.getFormat());
        showMessage("Người PV      : " + schedule.getInterviewer());
        showMessage("Ghi chú       : " + schedule.getNote());
        showMessage("Trạng thái    : " + schedule.getStatus());
        showMessage("----------------------------");
        promptContinueWithDraft(schedule);
    }

    public void displayEditOptions(InterviewSchedule schedule, String title) throws Exception {
        showMessage("---------- " + title + " ------------");
        showMessage("Nhập STT mục cần chỉnh sửa (vd: 1):");
        showMessage("----1. Thời gian PV   : " + schedule.getInterviewTime());
        showMessage("----2. Địa điểm       : " + schedule.getLocation());
        showMessage("----3. Hình thức      : " + schedule.getFormat());
        showMessage("----4. Người PV       : " + schedule.getInterviewer());
        showMessage("----5. Ghi chú        : " + schedule.getNote());
        showMessage("------------------------------");
        showMessage("----6. Thoát");
        showMessage("----7. Lưu nháp");
        showMessage("----8. Gửi lịch chính thức");
        showMessage("------------------------------");
        controller.handleEditDraft(handleParam("Số TT cần sửa hoặc chức năng: "));
    }

    public void promptContinueWithDraft(InterviewSchedule schedule) throws Exception {
        showMessage("Bạn có muốn tiếp tục với lịch nháp này không?");
        controller.handleContinueDraft(handleParam("(Y/N): "), schedule);
    }

    public void promptSaveDraft(InterviewSchedule schedule) throws Exception {
        controller.handleSaveDraft(handleParam("Bạn có muốn lưu nháp (Y/N): "), schedule);
    }

    public void promptConfirmSubmit(InterviewSchedule schedule) throws Exception {
        controller.handleConfirmSubmit(handleParam("Xác nhận gửi lịch chính thức (Y/N): "), schedule);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }

    public void showError(String message) {
        super.showError(message);
    }
}