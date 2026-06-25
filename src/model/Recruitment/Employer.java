package model.Recruitment;

import enumModel.RoleEnum;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class Employer extends User {
    private List<JobPosting> postedJobs;
    private JobPosting draftPosting;

    public Employer() {
        super();
        this.postedJobs = new ArrayList<>();
    }

    public Employer(int userId, String username, String password, String email, RoleEnum role) {
        super(userId, username, password, email, role);
        this.postedJobs = new ArrayList<>();
    }

    public List<JobPosting> getPostedJobs() { return postedJobs; }
    public void setPostedJobs(List<JobPosting> postedJobs) { this.postedJobs = postedJobs; }
    public void addPostedJob(JobPosting job) {
        if (job != null && !postedJobs.contains(job)) {
            postedJobs.add(job);
            job.setEmployer(this);
        }
    }

    public JobPosting getDraftPosting() { return draftPosting; }
    public void setDraftPosting(JobPosting draftPosting) {
        this.draftPosting = draftPosting;
        if (draftPosting != null) {
            draftPosting.setEmployer(this);
            draftPosting.setStatus("DRAFT");
        }
    }

    // Tương thích với code cũ
    public JobPosting getJobPostingDraf() { return getDraftPosting(); }
}