package model;

import dao.ProfileDao.ProfileDao;
import dao.UserDAO;
import enumModel.RoleEnum;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class User {

    //Lộc
    private String password;
    private int userId;
    private  String username;


    private int id;
    private RoleEnum role;
    private Double basicSalary;
    private int dependentNumber;

    public User() {}


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

    public RoleEnum getRole() {
        return role;
    }

    public User(int userId, String username, String password, String email, RoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Double getBasicSalary() {
        return basicSalary;
      
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public int getDependentNumber() {
        return dependentNumber;
    }

    public void setDependentNumber(int dependentNumber) {
        this.dependentNumber = dependentNumber;
    }

    public void setId(int id) {this.id = id;}

    public void setFullName(String fullName) {this.fullName = fullName;}
    public Date getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(Date dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getCitizenIdentificationCard() {return citizenIdentificationCard;}
    public void setCitizenIdentificationCard(String citizenIdentificationCard) {this.citizenIdentificationCard = citizenIdentificationCard;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}



    public String changePassword(String oldPw, String newPw, String confirmPw) {
        if (!this.password.equals(oldPw)) {
            return "Mật khẩu hiện tại không đúng";
        }

        if (newPw.equals(oldPw)) {
            return "Mật khẩu mới không được trùng mật khẩu hiện tại";
        }

        if (!newPw.equals(confirmPw)) {
            return "Xác nhận mật khẩu không khớp";
        }

        String pwError = validatePasswordStrength(newPw);
        if (pwError != null) {
            return pwError;
        }

        this.password = newPw;
        UserDAO userDAO = new UserDAO();
        if (userDAO.update(this)) {
            return "SUCCESS";
        }
        return "Lỗi hệ thống. Vui lòng thử lại sau.";
    }

    private String validatePasswordStrength(String password) {
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ hoa";
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ thường";
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 chữ số";
        }
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            return "Mật khẩu phải có ít nhất 1 ký tự đặc biệt";
        }
        return null;
    }

    public static List<User> getAllEmployee() throws SQLException {
        ProfileDao pd = new ProfileDao();
        return pd.getAllUsers();
    }

}
