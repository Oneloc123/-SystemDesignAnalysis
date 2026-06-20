package model;

import enumModel.RoleEnum;

public class User {
    private RoleEnum role;
    private Double basicSalary;
    private int dependentNumber;

    public User() {
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Double getBasicSalary() {
        return basicSalary;
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
}
