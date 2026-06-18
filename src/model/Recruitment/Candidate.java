package model.Recruitment;

import dao.recruitment.CandidateDAO;
import dao.recruitment.JobApplicationDAO;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class Candidate extends User {
    private List<JobApplication> applications;

    private static CandidateDAO candidateDAO = new CandidateDAO();
    private static JobApplicationDAO jobAppDAO = new JobApplicationDAO();

    public Candidate() {
        super();
        this.applications = new ArrayList<>();
    }

    public Candidate(int userId, String username, String password, String email, String role) {
        super(userId, username, password, email, role);
        this.applications = new ArrayList<>();
    }

    public List<JobApplication> getApplications() {
        if (applications == null || applications.isEmpty()) {
            applications = jobAppDAO.findByCandidate(this.getUserId());
        }
        return applications;
    }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }
    public void addApplication(JobApplication application) {
        if (application != null && !applications.contains(application)) {
            applications.add(application);
            application.setCandidate(this);
        }
    }

    public boolean save() { return candidateDAO.save(this); }
    public boolean update() { return candidateDAO.update(this); }
    public boolean delete() { return candidateDAO.delete(this.getUserId()); }

    public static Candidate findById(int id) { return candidateDAO.findById(id); }
    public static List<Candidate> findAll() { return candidateDAO.findAll(); }
}