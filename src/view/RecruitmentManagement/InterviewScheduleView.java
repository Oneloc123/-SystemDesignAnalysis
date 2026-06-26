package view.RecruitmentManagement;

import controller.recruitmentManagement.InterviewScheduleController;
import model.Recruitment.Candidate;
import model.Recruitment.InterviewSchedule;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class InterviewScheduleView extends View {
    private InterviewScheduleController controller;

    public InterviewScheduleView(InterviewScheduleController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Tạo lịch phỏng vấn", "Chỉnh sửa lịch phỏng vấn", "Hủy lịch phỏng vấn", "Xem danh sách lịch phỏng vấn"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.executeCommand(question);
        }
    }

    public void displayApprovedCandidates(List<Candidate> candidates) throws Exception {
        if (candidates.isEmpty()) {
            showError("Không có ứng viên nào được chấp thuận.");
            return;
        }
        showMessage("=== DANH SÁCH ỨNG VIÊN ĐÃ ĐƯỢC CHẤP THUẬN ===");
        int index = 1;
        for (Candidate c : candidates) {
            showMessage(index + ". " + c.getFullName() + " - " + c.getEmail() + " - " + c.getPhone());
            index++;
        }
        int choice = Integer.parseInt(handleParam("Chọn ứng viên (nhập số thứ tự): "));
        Candidate selected = candidates.get(choice - 1);
        enterScheduleDetails(selected);
    }

    public void enterScheduleDetails(Candidate candidate) throws Exception {
        showMessage("--- TẠO LỊCH PHỎNG VẤN CHO: " + candidate.getFullName() + " ---");
        java.sql.Timestamp interviewDate = handleDateTime("Thời gian phỏng vấn (vd: 2026-07-15 09:00): ");
        String location = handleParam("Địa điểm phỏng vấn");
        String interviewer = handleParam("Người phỏng vấn");
        String note = handleParam("Ghi chú (nếu có)");
        controller.createSchedule(candidate.getCandidateId(), interviewDate, location, interviewer, note);
    }

    public void displayScheduleList(List<InterviewSchedule> schedules) throws Exception {
        if (schedules.isEmpty()) {
            showError("Không có lịch phỏng vấn nào.");
            return;
        }
        showMessage("=== DANH SÁCH LỊCH PHỎNG VẤN ===");
        int index = 1;
        for (InterviewSchedule s : schedules) {
            showMessage(index + ". " + s.getCandidate().getFullName()
                    + " - " + s.getInterviewDate()
                    + " - " + s.getLocation()
                    + " - Trạng thái: " + s.getStatus());
            index++;
        }
    }

    public void displayScheduleDetail(InterviewSchedule schedule) {
        showMessage("==============================");
        showMessage("Ứng viên      : " + schedule.getCandidate().getFullName());
        showMessage("Ngày PV       : " + schedule.getInterviewDate());
        showMessage("Địa điểm      : " + schedule.getLocation());
        showMessage("Người PV      : " + schedule.getInterviewer());
        showMessage("Ghi chú       : " + schedule.getNote());
        showMessage("Trạng thái    : " + schedule.getStatus());
        showMessage("==============================");
    }

    public void selectScheduleForEdit(List<InterviewSchedule> schedules) throws Exception {
        if (schedules.isEmpty()) {
            showError("Không có lịch phỏng vấn nào.");
            return;
        }
        displayScheduleList(schedules);
        int choice = Integer.parseInt(handleParam("Chọn lịch phỏng vấn (nhập số thứ tự): "));
        InterviewSchedule selected = schedules.get(choice - 1);
        displayScheduleDetail(selected);
        showMessage("--- NHẬP THÔNG TIN MỚI ---");
        String dateStr = handleParam("Thời gian mới (vd: 2026-07-15 09:00)");
        java.sql.Timestamp newDate = null;
        if (!dateStr.trim().isEmpty()) {
            try {
                newDate = java.sql.Timestamp.valueOf(dateStr.trim());
            } catch (IllegalArgumentException e) {
                showError("Định dạng thời gian không hợp lệ.");
            }
        }
        String location = handleParam("Địa điểm mới");
        if (location.trim().isEmpty()) location = selected.getLocation();
        String interviewer = handleParam("Người PV mới");
        if (interviewer.trim().isEmpty()) interviewer = selected.getInterviewer();
        controller.updateSchedule(selected.getScheduleId(), newDate, location, interviewer);
    }

    public void selectScheduleForCancel(List<InterviewSchedule> schedules) throws Exception {
        if (schedules.isEmpty()) {
            showError("Không có lịch phỏng vấn nào.");
            return;
        }
        displayScheduleList(schedules);
        int choice = Integer.parseInt(handleParam("Chọn lịch phỏng vấn để hủy (nhập số thứ tự): "));
        InterviewSchedule selected = schedules.get(choice - 1);
        String confirm = handleParam("Xác nhận hủy lịch phỏng vấn với " + selected.getCandidate().getFullName() + "? (Y/N): ");
        if (confirm.toUpperCase().equals("Y")) {
            controller.cancelSchedule(selected.getScheduleId());
        }
    }

    @Override
    public void showError(String error) {
        super.showError(error);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}
