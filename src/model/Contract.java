package model;

import dao.ContractDAO;

import java.sql.Date;
import java.util.List;


public class Contract {
    private int contractId;
    private int employeeId;
    private String employeeName;
    private String employeeCode;
    private String contractCode;
    private String contractType;
    private Date startDate;
    private Date endDate;
    private double baseSalary;
    private double allowance;
    private String position;
    private int departmentId;
    private String departmentName;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String notes;

    private static ContractDAO contractDAO = new ContractDAO();

    public Contract() {}

    public boolean save()   { return contractDAO.save(this); }
    public boolean update() { return contractDAO.update(this); }
    public boolean delete() { return contractDAO.delete(this.contractId); }

    public static Contract findById(int id)             { return contractDAO.findById(id); }
    public static List<Contract> findAll()              { return contractDAO.findAll(); }
    public static List<Contract> findByEmployee(int employeeId) { return contractDAO.findByEmployee(employeeId); }
    public static List<Contract> search(String keyword, String statusFilter, String typeFilter) {
        return contractDAO.search(keyword, statusFilter, typeFilter);
    }
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }

    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }

    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDisplayEndDate() {
        return endDate == null ? "Không xác định thời hạn" : endDate.toString();
    }

}
