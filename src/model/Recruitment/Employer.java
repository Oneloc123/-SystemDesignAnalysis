package model.Recruitment;

import dao.recruitment.EmployerDAO;
import dao.recruitment.JobPostingDAO;
import enumModel.RoleEnum;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class Employer extends User {
    private List<JobPosting> postedJobs;
    private JobPosting draftPosting;

    private static EmployerDAO employerDAO = new EmployerDAO();
    private static JobPostingDAO jobPostingDAO = new JobPostingDAO();

    public Employer() {
        super();
        this.postedJobs = new ArrayList<>();
    }

    public Employer(int userId, String username, String password, String email, RoleEnum role) {
        super(userId, username, password, email, role);
        this.postedJobs = new ArrayList<>();
    }

    public List<JobPosting> getPostedJobs() {
        if (postedJobs == null || postedJobs.isEmpty()) {
            postedJobs = jobPostingDAO.findByEmployer(this.getUserId());
        }
        return postedJobs;
    }
    public void setPostedJobs(List<JobPosting> postedJobs) { this.postedJobs = postedJobs; }
    public void addPostedJob(JobPosting job) {
        if (job != null && !postedJobs.contains(job)) {
            postedJobs.add(job);
            job.setEmployer(this);
        }
    }

    public JobPosting getDraftPosting() {
        if (draftPosting == null) {
            draftPosting = employerDAO.getDraftPosting(this.getUserId());
        }
        return draftPosting;
    }
    public void setDraftPosting(JobPosting draftPosting) {
        this.draftPosting = draftPosting;
        if (draftPosting != null) {
            draftPosting.setEmployer(this);
            draftPosting.setStatus("DRAFT");
        }
    }

    // Tương thích với code cũ
    public JobPosting getJobPostingDraf() { return getDraftPosting(); }

    public boolean save() { return employerDAO.save(this); }
    public boolean update() { return employerDAO.update(this); }
    public boolean delete() { return employerDAO.delete(this.getUserId()); }

    public static Employer findById(int id) { return employerDAO.findById(id); }
    public static List<Employer> findAll() { return employerDAO.findAll(); }
}