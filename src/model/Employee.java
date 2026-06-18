package model;

public class Employee {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String idCard;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String hometown;
    private Double baseSalary;
    private Double fixedAllowance;
    private String bankAccount;
    private String bankName;
    private String bankAccountHolder;
    private String taxCode;
    private String socialInsuranceNumber;
    private String position;
    private String departmentName;
    private String startDate;
    private String contractType;
    private String status;
    private String qualification;
    private String major;
    private String experience;

    public Employee() {}

    public String getMaskedIdCard() {
        if (idCard == null || idCard.length() < 4) return "Chưa cập nhật";
        return "****" + idCard.substring(idCard.length() - 4);
    }

    public String getMaskedBankAccount() {
        if (bankAccount == null || bankAccount.length() < 4) return "Chưa cập nhật";
        return "****" + bankAccount.substring(bankAccount.length() - 4);
    }

    public String getDisplayValue(String value) {
        return (value == null || value.isEmpty()) ? "Chưa cập nhật" : value;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
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
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
