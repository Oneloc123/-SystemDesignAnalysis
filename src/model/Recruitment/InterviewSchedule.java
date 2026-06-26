package model.Recruitment;

import java.sql.Timestamp;

public class InterviewSchedule {
    private int scheduleId;
    private Candidate candidate;
    private String interviewer;
    private Timestamp interviewDate;
    private String location;
    private String note;
    private String status;

    public InterviewSchedule() { this.status = "SCHEDULED"; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    public String getInterviewer() { return interviewer; }
    public void setInterviewer(String interviewer) { this.interviewer = interviewer; }
    public Timestamp getInterviewDate() { return interviewDate; }
    public void setInterviewDate(Timestamp interviewDate) { this.interviewDate = interviewDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void schedule() { this.status = "SCHEDULED"; }
    public void update(Timestamp interviewDate, String location, String interviewer) {
        this.interviewDate = interviewDate; this.location = location; this.interviewer = interviewer;
    }
    public void cancel() { this.status = "CANCELLED"; }

    public boolean isValid() { return candidate != null && interviewDate != null && interviewer != null && !interviewer.trim().isEmpty(); }
}
