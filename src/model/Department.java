package model;

import dao.DepartmentDAO;
import model.hr.Employee;
import java.util.List;

public class Department {
    private int departmentId;
    private String name;
    private String code;
    private String managerName;

    private static DepartmentDAO dao = new DepartmentDAO();

    public Department() {}

    public Department(int departmentId, String name, String code, String managerName) {
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
        this.managerName = managerName;
    }


    public List<Employee> getEmployees() {
        return Employee.findByDepartment(this.departmentId);
    }


    public boolean save() { return dao.save(this); }
    public boolean update() { return dao.update(this); }
    public boolean delete() { return dao.delete(this.departmentId); }
    public static Department findById(int id) { return dao.findById(id); }
    public static List<Department> findAll() { return dao.findAll(); }


    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
}
