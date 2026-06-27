package controller.recruitmentManagement;

import controller.MainController;
import dao.recruitment.CandidateDAO;
import enumModel.AddressEnum;
import model.Recruitment.Candidate;
import view.RecruitmentManagement.CandidateView;

import java.sql.Date;

public class CandidateController {
    private CandidateView view;
    private CandidateDAO candidateDAO;
    private AddressEnum address = AddressEnum.CandidateSubmission;

    public CandidateController() {
        this.view = new CandidateView(this);
        this.candidateDAO = new CandidateDAO();
    }

    public boolean navigate() throws Exception {
        // No auth check - anyone can submit CV
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void submitCV(String fullName, String gender, Date birthday, String phone,
                          String email, String address, String education,
                          String experience, String cvFile) throws Exception {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setGender(gender);
        candidate.setBirthday(birthday);
        candidate.setPhone(phone);
        candidate.setEmail(email);
        candidate.setAddress(address);
        candidate.setEducation(education);
        candidate.setExperience(experience);
        candidate.setCvFile(cvFile);
        candidate.submitCV();

        if (!candidate.isValid()) {
            view.showError("Vui lòng nhập đầy đủ thông tin bắt buộc (họ tên, SĐT, email).");
            return;
        }

        if (candidateDAO.save(candidate)) {
            view.showSuccessMessage(candidate.getCandidateId());
        } else {
            view.showError("Nộp hồ sơ thất bại, vui lòng thử lại sau.");
        }
    }
}
