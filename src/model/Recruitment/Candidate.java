package model.Recruitment;

import enumModel.RoleEnum;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class Candidate extends User {
    private List<JobApplication> applications;

    public Candidate() {
        super();
        this.applications = new ArrayList<>();
    }

    public Candidate(int userId, String username, String password, String email, RoleEnum role) {
        super(userId, username, password, email, role);
        this.applications = new ArrayList<>();
    }

    public List<JobApplication> getApplications() { return applications; }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }
    public void addApplication(JobApplication application) {
        if (application != null && !applications.contains(application)) {
            applications.add(application);
            application.setCandidate(this);
        }
    }
}