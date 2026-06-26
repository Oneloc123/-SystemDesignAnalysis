package controller.recruitmentManagement;

import controller.MainController;
import dao.recruitment.CandidateDAO;
import dao.recruitment.InterviewEvaluationDAO;
import dao.recruitment.InterviewScheduleDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.Candidate;
import model.Recruitment.InterviewEvaluation;
import model.Recruitment.InterviewSchedule;
import view.RecruitmentManagement.InterviewEvaluationView;

import java.util.List;

public class InterviewEvaluationController {
    private InterviewEvaluationView view;
    private InterviewScheduleDAO scheduleDAO;
    private InterviewEvaluationDAO evaluationDAO;
    private CandidateDAO candidateDAO;
    private AddressEnum address = AddressEnum.InterviewEvaluationManagement;

    public InterviewEvaluationController() {
        this.view = new InterviewEvaluationView(this);
        this.scheduleDAO = new InterviewScheduleDAO();
        this.evaluationDAO = new InterviewEvaluationDAO();
        this.candidateDAO = new CandidateDAO();
    }

    public boolean navigate() throws Exception {
        if (MainController.currentUser.getRole() != RoleEnum.HR
                && MainController.currentUser.getRole() != RoleEnum.ADMIN) {
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
                List<InterviewSchedule> schedules = scheduleDAO.findAll();
                view.displayScheduleList(schedules);
                break;
            case "2":
                List<InterviewSchedule> schedulesForEval = scheduleDAO.findAll();
                view.selectScheduleForEvaluation(schedulesForEval);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void saveEvaluation(int scheduleId, double score, String comment, String result) throws Exception {
        InterviewSchedule schedule = scheduleDAO.findById(scheduleId);
        if (schedule == null) {
            view.showError("Không tìm thấy lịch phỏng vấn.");
            return;
        }

        InterviewEvaluation evaluation = new InterviewEvaluation();
        evaluation.setSchedule(schedule);
        evaluation.setScore(score);
        evaluation.setComment(comment);

        if ("PASS".equals(result)) {
            evaluation.pass();
        } else {
            evaluation.fail();
        }

        if (evaluationDAO.save(evaluation)) {
            // Update schedule status to COMPLETED
            schedule.setStatus("COMPLETED");
            scheduleDAO.update(schedule);

            // Update candidate status based on result
            Candidate candidate = schedule.getCandidate();
            if ("PASS".equals(result)) {
                candidate.setStatus("HIRED");
            } else {
                candidate.setStatus("FAILED");
            }
            candidateDAO.update(candidate);

            view.showEvaluationSuccess(result);
        } else {
            view.showError("Lưu đánh giá thất bại.");
        }
    }
}
