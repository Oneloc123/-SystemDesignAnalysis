package model.Recruitment;

import model.User;
import java.time.LocalDate;

public class ApplicationReview {
    private int reviewId;
    private JobApplication application;
    private User reviewer;
    private String status;
    private String note;
    private LocalDate reviewDate;

    public ApplicationReview() {
        this.status = "PENDING_REVIEW";
        this.reviewDate = LocalDate.now();
    }

    // Getters & Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public JobApplication getApplication() { return application; }
    public void setApplication(JobApplication application) { this.application = application; }

    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }

}