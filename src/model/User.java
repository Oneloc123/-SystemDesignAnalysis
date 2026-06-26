package model;

import enumModel.RoleEnum;

import java.util.Date;

public class User {

    //Lộc
    private String password;
    private int userId;
    private  String username;
    private String email;



    private RoleEnum role;
    private Double basicSalary;
    private int dependentNumber;

    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phone;
    private String citizenIdentificationCard;
    private String address;


    public User() {}


    public User(String address, String citizenIdentificationCard, String phone, String gender, Date dateOfBirth, String fullName, RoleEnum role, int userId) {
        this.address = address;
        this.citizenIdentificationCard = citizenIdentificationCard;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.role = role;
        this.userId = userId;
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
    }

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
    public String getFullName() {return fullName;}
    public void setFullname(String fullName){ this.fullName = fullName;}




}
