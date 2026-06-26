package model.Recruitment;

import java.sql.Date;

public class InterviewEvaluation {
    private int evaluationId;
    private InterviewSchedule schedule;
    private double score;
    private String comment;
    private String result;
    private Date evaluationDate;

    public InterviewEvaluation() {
        this.evaluationDate = new Date(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getEvaluationId() { return evaluationId; }
    public void setEvaluationId(int evaluationId) { this.evaluationId = evaluationId; }

    public InterviewSchedule getSchedule() { return schedule; }
    public void setSchedule(InterviewSchedule schedule) { this.schedule = schedule; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public Date getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(Date evaluationDate) { this.evaluationDate = evaluationDate; }

    // Business methods
    public void pass() {
        this.result = "PASS";
    }

    public void fail() {
        this.result = "FAIL";
    }

    @Override
    public String toString() {
        return "InterviewEvaluation{" +
                "evaluationId=" + evaluationId +
                ", schedule=" + (schedule != null ? schedule.getScheduleId() : 0) +
                ", score=" + score +
                ", result='" + result + '\'' +
                '}';
    }
}
