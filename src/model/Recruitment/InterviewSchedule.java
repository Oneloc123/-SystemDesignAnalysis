package model.Recruitment;

import dao.recruitment.InterviewScheduleDAO;
import java.time.LocalDateTime;
import java.util.List;

public class InterviewSchedule {
    private int interviewId;
    private JobApplication application;
    private LocalDateTime interviewTime;
    private String location;
    private String format;      // ONLINE, OFFLINE
    private String interviewer;
    private String note;
    private String status;      // DRAFT, CONFIRMED, CANCELLED

    private static InterviewScheduleDAO dao = new InterviewScheduleDAO();

    public InterviewSchedule() {
        this.status = "DRAFT";
    }

    // Getters & Setters
    public int getInterviewId() { return interviewId; }
    public void setInterviewId(int interviewId) { this.interviewId = interviewId; }

    public JobApplication getApplication() { return application; }
    public void setApplication(JobApplication application) { this.application = application; }

    public LocalDateTime getInterviewTime() { return interviewTime; }
    public void setInterviewTime(LocalDateTime interviewTime) { this.interviewTime = interviewTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getInterviewer() { return interviewer; }
    public void setInterviewer(String interviewer) { this.interviewer = interviewer; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Nghiệp vụ
    public boolean checkValid() {
        return interviewTime != null
                && location != null && !location.trim().isEmpty()
                && format != null && !format.trim().isEmpty()
                && interviewer != null && !interviewer.trim().isEmpty();
    }

    public boolean save() {
        if (!checkValid()) return false;
        this.status = "CONFIRMED";
        return dao.save(this);
    }

    public boolean saveDraft() {
        if (!checkValid()) return false;
        this.status = "DRAFT";
        return dao.saveDraft(this);
    }

    public boolean update() {
        return dao.update(this);
    }

    public boolean delete() {
        return dao.delete(this.interviewId);
    }

    public static InterviewSchedule findById(int id) {
        return dao.findById(id);
    }

    public static List<InterviewSchedule> findAll() {
        return dao.findAll();
    }

    public static InterviewSchedule findDraftByApplication(int applicationId) {
        return dao.findDraftByApplication(applicationId);
    }

    public static List<InterviewSchedule> findByApplication(int applicationId) {
        return dao.findByApplication(applicationId);
    }
}