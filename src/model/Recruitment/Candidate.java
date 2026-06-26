package model.Recruitment;

import java.sql.Date;

public class Candidate {
    private int candidateId;
    private String fullName;
    private String gender;
    private Date birthday;
    private String phone;
    private String email;
    private String address;
    private String education;
    private String experience;
    private String cvFile;
    private String status;

    public Candidate() { this.status = "NEW"; }

    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getCvFile() { return cvFile; }
    public void setCvFile(String cvFile) { this.cvFile = cvFile; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void submitCV() { this.status = "NEW"; }

    public void updateInformation(String fullName, String gender, Date birthday, String phone,
                                   String email, String address, String education,
                                   String experience, String cvFile) {
        this.fullName = fullName; this.gender = gender; this.birthday = birthday;
        this.phone = phone; this.email = email; this.address = address;
        this.education = education; this.experience = experience; this.cvFile = cvFile;
    }

    public boolean isValid() {
        return fullName != null && !fullName.trim().isEmpty()
                && phone != null && !phone.trim().isEmpty()
                && email != null && !email.trim().isEmpty();
    }
}
