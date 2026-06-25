package model.Recruitment;

import java.time.LocalDateTime;

public class InterviewSchedule {
    private int interviewId;
    private JobApplication application;
    private LocalDateTime interviewTime;
    private String location;
    private String format;      // ONLINE, OFFLINE
    private String interviewer;
    private String note;
    private String status;      // DRAFT, CONFIRMED, CANCELLED

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

    public boolean isReadyToSave() {
        return checkValid();
    }
}