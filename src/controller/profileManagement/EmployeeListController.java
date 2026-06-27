package controller.profileManagement;

import controller.ScreenManager;
import enumModel.RoleEnum;
import model.Department;
import model.hr.Employee;
import view.managerView.EmployeeListView;
import java.util.List;

public class EmployeeListController {
    private EmployeeListView employeeListView;
    private int selectedDeptId;
    private String searchKeyword = "";
    private String filterStatus = "";

    public EmployeeListController() {
        this.employeeListView = new EmployeeListView(this);
    }

    public void showOn() {
        employeeListView.showDeptSelection();
    }

    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null
            && ScreenManager.getCurrentUser().getRole() == RoleEnum.MANAGER;
    }

    public List<Department> getDepartments() {
        return Department.findAll();
    }

    public boolean selectDepartment(int id) {
        Department dept = Department.findById(id);
        if (dept != null) {
            selectedDeptId = id;
            searchKeyword = "";
            filterStatus = "";
            return true;
        }
        return false;
    }

    public List<Employee> getFilteredEmployees() {
        return Employee.search(searchKeyword, filterStatus, selectedDeptId);
    }

    public String getEmployeeDetail(int index) {
        List<Employee> list = getFilteredEmployees();
        if (index < 1 || index > list.size()) return "Không hợp lệ.";
        Employee emp = list.get(index - 1);
        String deptName = emp.getDepartmentName();
        if (deptName == null) {
            Department dept = Department.findById(emp.getDepartmentId());
            deptName = dept != null ? dept.getName() : null;
        }
        return "Họ tên    : " + emp.getDisplayValue(emp.getFullName())
             + "\nMã NV     : " + emp.getDisplayValue(emp.getEmployeeCode())
             + "\nPhòng ban : " + emp.getDisplayValue(deptName)
             + "\nChức danh : " + emp.getDisplayValue(emp.getPosition())
             + "\nSĐT       : " + emp.getDisplayValue(emp.getPhone())
             + "\nEmail     : " + emp.getDisplayValue(emp.getEmail())
             + "\nNgày vào  : " + emp.getDisplayValue(emp.getStartDate())
             + "\nLoại HĐ   : " + emp.getDisplayValue(emp.getContractType())
             + "\nTrạng thái: " + emp.getDisplayValue(emp.getEmployeeStatus())
             + "\n";
    }

    public void setSearchKeyword(String kw) { this.searchKeyword = kw; }
    public void setFilterStatus(String s) { this.filterStatus = s; }
    public int getSelectedDeptId() { return selectedDeptId; }
}
