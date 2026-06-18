package model.Recruitment;

import dao.recruitment.JobPostingDAO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JobPosting {
    private int postId;
    private String title;
    private String description;
    private String requiment;
    private Date dayEnd;
    private double salary;
    private Employer employer;
    private Date createdDate;
    private String status;
    private List<JobApplication> applications;

    private static JobPostingDAO dao = new JobPostingDAO();

    public JobPosting() {
        this.applications = new ArrayList<>();
        this.status = "DRAFT";
    }

    public JobPosting(int postId, String title, String description, String requiment, Date dayEnd, double salary) {
        this();
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.requiment = requiment;
        this.dayEnd = dayEnd;
        this.salary = salary;
    }

    // Getters & Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequiment() { return requiment; }
    public void setRequiment(String requiment) { this.requiment = requiment; }

    public Date getDayEnd() { return dayEnd; }
    public void setDayEnd(Date dayEnd) { this.dayEnd = dayEnd; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Employer getEmployer() { return employer; }
    public void setEmployer(Employer employer) { this.employer = employer; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<JobApplication> getApplications() {
        // Nếu chưa load, có thể load từ DB (tuỳ chọn)
        return applications;
    }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }

    // Nghiệp vụ
    public boolean checkValid() {
        return title != null && !title.trim().isEmpty()
                && description != null && !description.trim().isEmpty()
                && requiment != null && !requiment.trim().isEmpty()
                && dayEnd != null
                && salary > 0;
    }

    public boolean save() {
        if (!checkValid()) return false;
        this.status = "OPEN";
        this.createdDate = new Date(System.currentTimeMillis());
        if (dao.save(this)) {
            if (employer != null) employer.addPostedJob(this);
            return true;
        }
        return false;
    }

    public boolean saveDraf() {
        if (!checkValid()) return false;
        this.status = "DRAFT";
        return dao.saveDraft(this);
    }

    public boolean update() { return dao.update(this); }
    public boolean delete() { return dao.delete(this.postId); }

    public static JobPosting findById(int id) { return dao.findById(id); }
    public static List<JobPosting> findAll() { return dao.findAll(); }
    public static List<JobPosting> findByEmployer(int employerId) { return dao.findByEmployer(employerId); }
}