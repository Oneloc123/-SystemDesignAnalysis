package model.calcSalary;

public class AttendanceDetail {
    private long id;
    private long employeeId;
    private int actualWorkingDays;
    private int overtimeHours;
    private double basicSalary;
    private double allowance;
    private int dependentNumber;

    public AttendanceDetail() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public int getActualWorkingDays() {
        return actualWorkingDays;
    }

    public void setActualWorkingDays(int actualWorkingDays) {
        this.actualWorkingDays = actualWorkingDays;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public int getDependentNumber() {
        return dependentNumber;
    }

    public void setDependentNumber(int dependentNumber) {
        this.dependentNumber = dependentNumber;
    }
}
