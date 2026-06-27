package controller.recruitmentManagement;

import controller.MainController;
import dao.recruitment.ApplicationReviewDAO;
import dao.recruitment.CandidateDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.ApplicationReview;
import model.Recruitment.Candidate;
import view.RecruitmentManagement.ApplicationReviewView;

import java.util.List;

public class ApplicationReviewController {
    private ApplicationReviewView view;
    private CandidateDAO candidateDAO;
    private ApplicationReviewDAO reviewDAO;
    private AddressEnum address = AddressEnum.ApplicationReviewManagement;

    public ApplicationReviewController() {
        this.view = new ApplicationReviewView(this);
        this.candidateDAO = new CandidateDAO();
        this.reviewDAO = new ApplicationReviewDAO();
    }

    public boolean navigate() throws Exception {
        if (MainController.currentUser.getRole() != RoleEnum.HR) {
            return false;
        }
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void executeCommand(String command) throws Exception {
        switch (command) {
            case "1":
                List<Candidate> allCandidates = candidateDAO.findAll();
                view.displayCandidateList(allCandidates);
                break;
            case "2":
                List<Candidate> candidates = candidateDAO.findAll();
                view.selectCandidateForDetail(candidates);
                break;
            case "3":
                List<Candidate> candidatesForReview = candidateDAO.findAll();
                view.selectCandidateForReview(candidatesForReview);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void viewCandidateDetail(int candidateId) throws Exception {
        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null) {
            view.showError("Không tìm thấy ứng viên.");
            return;
        }
        view.displayCandidateDetail(candidate);
    }

    public void saveReview(int candidateId, double score, String comment, String result) throws Exception {
        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null) {
            view.showError("Không tìm thấy ứng viên.");
            return;
        }

        ApplicationReview review = new ApplicationReview();
        review.setCandidate(candidate);
        review.setReviewer(MainController.currentUser);
        review.setScore(score);
        review.setComment(comment);

        if ("APPROVED".equals(result)) {
            review.approve();
        } else {
            review.reject();
        }

        if (reviewDAO.save(review)) {
            // Update candidate status
            candidate.setStatus(result);
            candidateDAO.update(candidate);
            view.showMessage("Đã lưu đánh giá: " + result);
        } else {
            view.showError("Lưu đánh giá thất bại.");
        }
    }
}
