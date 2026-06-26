package model.hr;

import dao.EmployeeDAO;
import model.User;
import java.util.List;

public class Employee extends User {
    private String employeeCode;
    private String hometown;
    private Double baseSalary;
    private Double fixedAllowance;
    private String bankAccount;
    private String bankName;
    private String bankAccountHolder;
    private String taxCode;
    private String socialInsuranceNumber;
    private String position;
    private Integer departmentId;
    private String departmentName;
    private String startDate;
    private String contractType;
    private String employeeStatus;
    private String qualification;
    private String major;
    private String experience;

    private static EmployeeDAO employeeDAO = new EmployeeDAO();

    public Employee() {
        super();
    }


    public String getMaskedIdCard() {
        String id = getCitizenIdentificationCard();
        if (id == null || id.length() < 4) return "Chưa cập nhật";
        return "****" + id.substring(id.length() - 4);
    }

    public String getMaskedBankAccount() {
        if (bankAccount == null || bankAccount.length() < 4) return "Chưa cập nhật";
        return "****" + bankAccount.substring(bankAccount.length() - 4);
    }

    public String getDisplayValue(String value) {
        return (value == null || value.isEmpty()) ? "Chưa cập nhật" : value;
    }


    public boolean save() { return employeeDAO.save(this); }
    public boolean update() { return employeeDAO.update(this); }
    public boolean delete() { return employeeDAO.delete((int) this.getUserId()); }

    public static Employee findById(int id) { return employeeDAO.findById(id); }
    public static Employee findByUserId(int userId) { return employeeDAO.findByUserId(userId); }
    public static List<Employee> findAll() { return employeeDAO.findAll(); }
    public static List<Employee> findByDepartment(int deptId) { return employeeDAO.findByDepartment(deptId); }
    public static List<Employee> search(String keyword, String statusFilter, int deptId) {
        return employeeDAO.search(keyword, statusFilter, deptId);
    }


    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }
    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }
    public Double getFixedAllowance() { return fixedAllowance; }
    public void setFixedAllowance(Double fixedAllowance) { this.fixedAllowance = fixedAllowance; }
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankAccountHolder() { return bankAccountHolder; }
    public void setBankAccountHolder(String bankAccountHolder) { this.bankAccountHolder = bankAccountHolder; }
    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    public String getSocialInsuranceNumber() { return socialInsuranceNumber; }
    public void setSocialInsuranceNumber(String socialInsuranceNumber) { this.socialInsuranceNumber = socialInsuranceNumber; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public String getEmployeeStatus() { return employeeStatus; }
    public void setEmployeeStatus(String employeeStatus) { this.employeeStatus = employeeStatus; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
