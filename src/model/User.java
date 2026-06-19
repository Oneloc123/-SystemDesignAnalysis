package model;

import enumModel.RoleEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private RoleEnum role;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String citizenIdentificationCard;
    private String address;

    public User(String address, String citizenIdentificationCard, String phone, String gender, Date dateOfBirth, String fullName, RoleEnum role, int id) {
        this.address = address;
        this.citizenIdentificationCard = citizenIdentificationCard;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.role = role;
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
    public int getId() { return id; }

    public static List<User> getMockData() {
        List<User> list = new ArrayList<>();
        list.add(new User("Hà Nội", "001", "0901", "Nam", new Date(), "Nguyễn Văn Lộc", RoleEnum.ADMIN, 1));
        list.add(new User("HCM", "002", "0902", "Nữ", new Date(), "Trần Thị Ánh", RoleEnum.HR, 2));
        list.add(new User("Đà Nẵng", "003", "0903", "Nam", new Date(), "Lê Đình Cương", RoleEnum.EMPLOYER, 3));
        return list;
    }
}

