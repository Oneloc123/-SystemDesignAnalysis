package model.Recruitment;

import model.User;
import java.sql.Date;

public class ApplicationReview {
    private int reviewId;
    private Candidate candidate;
    private User reviewer;
    private double score;
    private String comment;
    private String result;
    private Date reviewDate;

    public ApplicationReview() {
        this.reviewDate = new Date(System.currentTimeMillis());
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }

    public void approve() { this.result = "APPROVED"; if (this.candidate != null) this.candidate.setStatus("APPROVED"); }
    public void reject() { this.result = "REJECTED"; if (this.candidate != null) this.candidate.setStatus("REJECTED"); }
}
