package model.calcSalary;

public class AttendanceDetail {
    private long id;
    private long periodId;
    private long employeeId;
    private String employeeCode;
    private String employeeName;
    private int actualWorkingDays;
    private int standardDays;
    private int overtimeHours;

    private int unpaidLeave;
    private int paidLeave;
    private double basicSalary;
    private double allowance;
    private int dependentNumber;
    private String status;

    public AttendanceDetail() {}


    public boolean isAbnormal() {
        return unpaidLeave >= 2 || overtimeHours > 40;
    }

    public String getAssessment() {
        return isAbnormal() ? "⚠ Bất thường" : "Bình thường";
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getPeriodId() { return periodId; }
    public void setPeriodId(long periodId) { this.periodId = periodId; }
    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public int getActualWorkingDays() { return actualWorkingDays; }
    public void setActualWorkingDays(int actualWorkingDays) { this.actualWorkingDays = actualWorkingDays; }
    public int getStandardDays() { return standardDays; }
    public void setStandardDays(int standardDays) { this.standardDays = standardDays; }
    public int getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(int overtimeHours) { this.overtimeHours = overtimeHours; }

    public int getUnpaidLeave() { return unpaidLeave; }
    public void setUnpaidLeave(int unpaidLeave) { this.unpaidLeave = unpaidLeave; }
    public int getPaidLeave() { return paidLeave; }
    public void setPaidLeave(int paidLeave) { this.paidLeave = paidLeave; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }
    public int getDependentNumber() { return dependentNumber; }
    public void setDependentNumber(int dependentNumber) { this.dependentNumber = dependentNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
