package model;

import dao.ProfileDao.ProfileDao;
import enumModel.RoleEnum;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private RoleEnum role;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phone;
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



    public RoleEnum getRole() {
        return role;
    }
    public void setRole(RoleEnum role) {
        this.role = role;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFullName() {return fullName;}
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




    public static List<User> getAllEmployee() throws SQLException {
        ProfileDao pd = new ProfileDao();
        return pd.getAllUsers();
    }





}
