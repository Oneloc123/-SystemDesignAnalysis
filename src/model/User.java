package model;

import enumModel.RoleEnum;
import model.Recruitment.JobPosting;

public abstract class User {
    private RoleEnum role ;

    public User() {
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}
