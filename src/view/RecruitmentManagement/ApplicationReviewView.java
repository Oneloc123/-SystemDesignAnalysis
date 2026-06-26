package view.RecruitmentManagement;

import controller.recruitmentManagement.ApplicationReviewController;
import model.Recruitment.Candidate;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class ApplicationReviewView extends View {
    private ApplicationReviewController controller;

    public ApplicationReviewView(ApplicationReviewController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Xem danh sách ứng viên", "Xem chi tiết ứng viên", "Đánh giá ứng viên"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.executeCommand(question);
        }
    }

    public void displayCandidateList(List<Candidate> candidates) throws Exception {
        if (candidates.isEmpty()) {
            showError("Không có ứng viên nào.");
            return;
        }
        showMessage("=== DANH SÁCH ỨNG VIÊN ===");
        int index = 1;
        for (Candidate c : candidates) {
            showMessage(index + ". " + c.getFullName()
                    + " - " + c.getEmail()
                    + " - " + c.getPhone()
                    + " - Trạng thái: " + c.getStatus());
            index++;
        }
    }

    public void displayCandidateDetail(Candidate candidate) {
        showMessage("==============================");
        showMessage("Họ tên        : " + candidate.getFullName());
        showMessage("Giới tính     : " + candidate.getGender());
        showMessage("Ngày sinh     : " + candidate.getBirthday());
        showMessage("SĐT           : " + candidate.getPhone());
        showMessage("Email         : " + candidate.getEmail());
        showMessage("Địa chỉ       : " + candidate.getAddress());
        showMessage("Học vấn       : " + candidate.getEducation());
        showMessage("Kinh nghiệm   : " + candidate.getExperience());
        showMessage("File CV       : " + candidate.getCvFile());
        showMessage("Trạng thái    : " + candidate.getStatus());
        showMessage("==============================");
    }

    public void selectCandidateForDetail(List<Candidate> candidates) throws Exception {
        if (candidates.isEmpty()) {
            showError("Không có ứng viên nào.");
            return;
        }
        displayCandidateList(candidates);
        int choice = Integer.parseInt(handleParam("Chọn ứng viên (nhập số thứ tự): "));
        Candidate selected = candidates.get(choice - 1);
        controller.viewCandidateDetail(selected.getCandidateId());
    }

    public void enterReview(Candidate candidate) throws Exception {
        showMessage("--- ĐÁNH GIÁ ỨNG VIÊN: " + candidate.getFullName() + " ---");
        double score = handleDouleParam("Điểm đánh giá (0-100)");
        String comment = handleParam("Nhận xét");
        showMessage("Chọn kết quả đánh giá:");
        showMessage("1. Chấp nhận (APPROVED)");
        showMessage("2. Từ chối (REJECTED)");
        String resultChoice = handleParam("Lựa chọn (1/2): ");
        String result = resultChoice.equals("1") ? "APPROVED" : "REJECTED";
        controller.saveReview(candidate.getCandidateId(), score, comment, result);
    }

    public void selectCandidateForReview(List<Candidate> candidates) throws Exception {
        if (candidates.isEmpty()) {
            showError("Không có ứng viên nào.");
            return;
        }
        displayCandidateList(candidates);
        int choice = Integer.parseInt(handleParam("Chọn ứng viên để đánh giá (nhập số thứ tự): "));
        Candidate selected = candidates.get(choice - 1);
        displayCandidateDetail(selected);
        enterReview(selected);
    }

    @Override
    public void showError(String error) {
        super.showError(error);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}
