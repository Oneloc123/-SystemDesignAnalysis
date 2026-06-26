package view.RecruitmentManagement;

import controller.recruitmentManagement.InterviewEvaluationController;
import model.Recruitment.InterviewSchedule;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class InterviewEvaluationView extends View {
    private InterviewEvaluationController controller;

    public InterviewEvaluationView(InterviewEvaluationController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Xem danh sách phỏng vấn", "Đánh giá kết quả phỏng vấn"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.executeCommand(question);
        }
    }

    public void displayScheduleList(List<InterviewSchedule> schedules) throws Exception {
        if (schedules.isEmpty()) {
            showError("Không có lịch phỏng vấn nào.");
            return;
        }
        showMessage("=== DANH SÁCH PHỎNG VẤN ===");
        int index = 1;
        for (InterviewSchedule s : schedules) {
            showMessage(index + ". " + s.getCandidate().getFullName()
                    + " - " + s.getInterviewDate()
                    + " - Người PV: " + s.getInterviewer()
                    + " - Trạng thái: " + s.getStatus());
            index++;
        }
    }

    public void selectScheduleForEvaluation(List<InterviewSchedule> schedules) throws Exception {
        if (schedules.isEmpty()) {
            showError("Không có lịch phỏng vấn nào.");
            return;
        }
        displayScheduleList(schedules);
        int choice = Integer.parseInt(handleParam("Chọn lịch phỏng vấn để đánh giá (nhập số thứ tự): "));
        InterviewSchedule selected = schedules.get(choice - 1);
        enterEvaluation(selected);
    }

    public void enterEvaluation(InterviewSchedule schedule) throws Exception {
        showMessage("--- ĐÁNH GIÁ KẾT QUẢ PHỎNG VẤN ---");
        showMessage("Ứng viên: " + schedule.getCandidate().getFullName());
        showMessage("Ngày PV: " + schedule.getInterviewDate());
        showMessage("Người PV: " + schedule.getInterviewer());

        double score = handleDouleParam("Điểm đánh giá (0-100)");
        String comment = handleParam("Nhận xét");

        showMessage("Kết quả phỏng vấn:");
        showMessage("1. Đạt (PASS)");
        showMessage("2. Không đạt (FAIL)");
        String resultChoice = handleParam("Lựa chọn (1/2): ");
        String result = resultChoice.equals("1") ? "PASS" : "FAIL";

        controller.saveEvaluation(schedule.getScheduleId(), score, comment, result);
    }

    public void showEvaluationSuccess(String result) {
        showMessage("Đã lưu kết quả đánh giá: " + result);
    }

    @Override
    public void showError(String error) {
        super.showError(error);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}
