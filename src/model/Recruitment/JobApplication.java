package model.Recruitment;

import java.time.LocalDate;

public class JobApplication {
    private int applicationId;
    private JobPosting jobPosting;
    private Candidate candidate;
    private String fullName;
    private String email;
    private String phone;
    private String coverLetter;
    private String cvFilePath;
    private String status;
    private LocalDate submittedDate;
    private boolean isDraft;

    public JobApplication() {
        this.status = "PENDING";
        this.isDraft = true;
    }

    // Getters & Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public JobPosting getJobPosting() { return jobPosting; }
    public void setJobPosting(JobPosting jobPosting) { this.jobPosting = jobPosting; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getCvFilePath() { return cvFilePath; }
    public void setCvFilePath(String cvFilePath) { this.cvFilePath = cvFilePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }

    public boolean isDraft() { return isDraft; }
    public void setDraft(boolean draft) { isDraft = draft; }

    // Nghiệp vụ
    public boolean checkValid() {
        return fullName != null && !fullName.trim().isEmpty()
                && email != null && !email.trim().isEmpty()
                && phone != null && !phone.trim().isEmpty()
                && cvFilePath != null && !cvFilePath.trim().isEmpty()
                && jobPosting != null;
    }

    public boolean isReadyToSave() {
        return checkValid();
    }
}