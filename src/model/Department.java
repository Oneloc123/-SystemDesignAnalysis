package model;

public class Department {
    private int departmentId;
    private String name;
    private String code;
    private String managerName;

    public Department() {}

    public Department(int departmentId, String name, String code, String managerName) {
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
        this.managerName = managerName;
    }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
}
