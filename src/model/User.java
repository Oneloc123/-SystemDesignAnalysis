package model;

import dao.UserDAO;
import enumModel.RoleEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;

    private int id;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phone;
    private String citizenIdentificationCard;
    private String address;

    private static UserDAO userDAO = new UserDAO();

    public User() {
    }

    public User(String address, String citizenIdentificationCard, String phone, String gender, Date dateOfBirth, String fullName, RoleEnum role, int id) {
        this.address = address;
        this.citizenIdentificationCard = citizenIdentificationCard;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.role = role.name();
        this.id = id;
    }

    public User(int userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }


    public String changePassword(String oldPw, String newPw, String confirmPw) {
        if (!newPw.equals(confirmPw)) return "Xác nhận mật khẩu không khớp.";
        if (oldPw.equals(newPw)) return "Mật khẩu mới không được trùng mật khẩu hiện tại.";
        if (newPw.length() < 8) return "Mật khẩu phải có ít nhất 8 ký tự.";
        if (!newPw.matches(".*[A-Z].*")) return "Mật khẩu phải có chữ hoa.";
        if (!newPw.matches(".*[a-z].*")) return "Mật khẩu phải có chữ thường.";
        if (!newPw.matches(".*[0-9].*")) return "Mật khẩu phải có chữ số.";
        if (!newPw.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return "Mật khẩu phải có ký tự đặc biệt.";

        this.password = newPw;
        if (userDAO.update(this)) {
            return "SUCCESS";
        }
        return "Lỗi cập nhật mật khẩu, vui lòng thử lại.";
    }

    public String getMaskedCitizenId() {
        if (citizenIdentificationCard == null || citizenIdentificationCard.length() < 4) return "Chưa cập nhật";
        return "****" + citizenIdentificationCard.substring(citizenIdentificationCard.length() - 4);
    }

    public String getDisplayValue(String value) {
        return (value == null || value.isEmpty()) ? "Chưa cập nhật" : value;
    }


    public boolean save() { return userDAO.save(this); }
    public boolean update() { return userDAO.update(this); }
    public boolean delete() { return userDAO.delete(this.userId); }


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

    public static List<User> getMockData() {
        List<User> list = new ArrayList<>();
        list.add(new User("Hà Nội", "001", "0901", "Nam", new Date(), "Nguyễn Văn Lộc", RoleEnum.ADMIN, 1));
        list.add(new User("HCM", "002", "0902", "Nữ", new Date(), "Trần Thị Ánh", RoleEnum.HR, 2));
        list.add(new User("Đà Nẵng", "003", "0903", "Nam", new Date(), "Lê Đình Cương", RoleEnum.EMPLOYER, 3));
        return list;
    }
}
