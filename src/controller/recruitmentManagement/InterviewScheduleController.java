package controller.recruitmentManagement;

import controller.MainController;
import dao.recruitment.CandidateDAO;
import dao.recruitment.InterviewScheduleDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.Candidate;
import model.Recruitment.InterviewSchedule;
import view.RecruitmentManagement.InterviewScheduleView;

import java.sql.Timestamp;
import java.util.List;

public class InterviewScheduleController {
    private InterviewScheduleView view;
    private InterviewScheduleDAO scheduleDAO;
    private CandidateDAO candidateDAO;
    private AddressEnum address = AddressEnum.InterviewScheduleManagement;

    public InterviewScheduleController() {
        this.view = new InterviewScheduleView(this);
        this.scheduleDAO = new InterviewScheduleDAO();
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
                List<Candidate> approvedCandidates = candidateDAO.findByStatus("APPROVED");
                view.displayApprovedCandidates(approvedCandidates);
                break;
            case "2":
                List<InterviewSchedule> schedules = scheduleDAO.findAll();
                view.selectScheduleForEdit(schedules);
                break;
            case "3":
                List<InterviewSchedule> allSchedules = scheduleDAO.findAll();
                view.selectScheduleForCancel(allSchedules);
                break;
            case "4":
                List<InterviewSchedule> list = scheduleDAO.findAll();
                view.displayScheduleList(list);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void createSchedule(int candidateId, Timestamp interviewDate, String location,
                                String interviewer, String note) throws Exception {
        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null) {
            view.showError("Không tìm thấy ứng viên.");
            return;
        }

        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setCandidate(candidate);
        schedule.setInterviewDate(interviewDate);
        schedule.setLocation(location);
        schedule.setInterviewer(interviewer);
        schedule.setNote(note);

        if (!schedule.isValid()) {
            view.showError("Vui lòng nhập đầy đủ thông tin (thời gian, người phỏng vấn).");
            return;
        }

        if (scheduleDAO.save(schedule)) {
            // Update candidate status
            candidate.setStatus("INTERVIEW_SCHEDULED");
            candidateDAO.update(candidate);
            view.showMessage("Tạo lịch phỏng vấn thành công! (ID: " + schedule.getScheduleId() + ")");
            view.displayScheduleDetail(schedule);
        } else {
            view.showError("Tạo lịch phỏng vấn thất bại.");
        }
    }

    public void updateSchedule(int scheduleId, Timestamp interviewDate, String location,
                                String interviewer) throws Exception {
        InterviewSchedule schedule = scheduleDAO.findById(scheduleId);
        if (schedule == null) {
            view.showError("Không tìm thấy lịch phỏng vấn.");
            return;
        }
        if (interviewDate != null) {
            schedule.setInterviewDate(interviewDate);
        }
        schedule.setLocation(location);
        schedule.setInterviewer(interviewer);

        if (scheduleDAO.update(schedule)) {
            view.showMessage("Cập nhật lịch phỏng vấn thành công.");
            view.displayScheduleDetail(schedule);
        } else {
            view.showError("Cập nhật thất bại.");
        }
    }

    public void cancelSchedule(int scheduleId) throws Exception {
        InterviewSchedule schedule = scheduleDAO.findById(scheduleId);
        if (schedule == null) {
            view.showError("Không tìm thấy lịch phỏng vấn.");
            return;
        }
        schedule.cancel();

        if (scheduleDAO.update(schedule)) {
            // Update candidate status
            Candidate candidate = schedule.getCandidate();
            candidate.setStatus("CANCELLED");
            candidateDAO.update(candidate);
            view.showMessage("Đã hủy lịch phỏng vấn.");
        } else {
            view.showError("Hủy lịch thất bại.");
        }
    }
}
