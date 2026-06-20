package model.Recruitment;

import dao.recruitment.ApplicationReviewDAO;
import model.User;
import java.time.LocalDate;
import java.util.List;

public class ApplicationReview {
    private int reviewId;
    private JobApplication application;
    private User reviewer;
    private String status;
    private String note;
    private LocalDate reviewDate;

    private static ApplicationReviewDAO dao = new ApplicationReviewDAO();

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

    // Nghiệp vụ
    public boolean save() {
        return dao.save(this);
    }

    public boolean update() {
        return dao.update(this);
    }

    public boolean delete() {
        return dao.delete(this.reviewId);
    }

    public static ApplicationReview findById(int id) {
        return dao.findById(id);
    }

    public static List<ApplicationReview> findAll() {
        return dao.findAll();
    }

    public static ApplicationReview findByApplication(int applicationId) {
        return dao.findByApplication(applicationId);
    }
}