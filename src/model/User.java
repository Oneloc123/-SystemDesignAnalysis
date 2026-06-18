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
