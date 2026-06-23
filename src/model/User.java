package model;

import enumModel.RoleEnum;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {
    private int id;
    private int userId;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String citizenIdentificationCard;
    private String address;
    private String employeeCode;
    private Date hireDate;
    private Integer departmentId;
    private Integer positionId;
    private Integer managerId;
    private BigDecimal baseSalary;
    private String status;
    private Boolean active;
    private Timestamp lastLogin;

    public User(String address, String citizenIdentificationCard, String phone, String gender, Date dateOfBirth, String fullName, RoleEnum role, int id) {
        this.address = address;
        this.citizenIdentificationCard = citizenIdentificationCard;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.role = role == null ? null : role.name();
        this.id = id;
    }

    public User() {
    }

    public User(int userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public void setRole(RoleEnum role) { this.role = role == null ? null : role.name(); }

    public RoleEnum getRoleEnum() {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }
        try {
            return RoleEnum.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, role);
    }



    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCitizenIdentificationCard() { return citizenIdentificationCard; }
    public void setCitizenIdentificationCard(String citizenIdentificationCard) { this.citizenIdentificationCard = citizenIdentificationCard; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }
    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }


    public static List<User> getMockData() {
        List<User> list = new ArrayList<>();
        // Nhét luôn new Date() cho nhanh, bỏ qua định dạng ngày tháng phức tạp
        list.add(new User("Hà Nội", "001", "0901", "Nam", new Date(), "Nguyễn Văn Lộc", RoleEnum.ADMIN, 1));
        list.add(new User("HCM", "002", "0902", "Nữ", new Date(), "Trần Thị Ánh", RoleEnum.HR, 2));
        list.add(new User("Đà Nẵng", "003", "0903", "Nam", new Date(), "Lê Đình Cương", RoleEnum.EMPLOYER, 3));
        return list;
    }
}

