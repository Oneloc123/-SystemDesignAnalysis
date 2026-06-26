package model.Recruitment;

import model.User;
import java.sql.Date;

public class JobPosting {
    private int jobId;
    private String title;
    private String description;
    private String requirement;
    private int quantity;
    private double salary;
    private Date deadline;
    private String status;
    private Date createdDate;
    private User createdBy;

    public JobPosting() {
        this.status = "OPEN";
    }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public void publish() { this.status = "OPEN"; }
    public void close() { this.status = "CLOSED"; }

    public void update(String title, String description, String requirement, int quantity, double salary, Date deadline) {
        this.title = title; this.description = description; this.requirement = requirement;
        this.quantity = quantity; this.salary = salary; this.deadline = deadline;
    }

    public boolean isValid() {
        return title != null && !title.trim().isEmpty() && deadline != null && salary > 0 && quantity > 0;
    }
}
