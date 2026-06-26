package controller;

import controller.base.Controller;
import model.Department;
import model.hr.Employee;
import view.EmployeeListView;
import java.util.List;

public class EmployeeListController extends Controller {
    EmployeeListView employeeListView;
    private int selectedDeptId;
    private String searchKeyword = "";
    private String filterStatus = "";

    public EmployeeListController() {
        this.employeeListView = new EmployeeListView(this);
        this.view = this.employeeListView;
    }

    @Override
    public void showOn() {
        employeeListView.showDeptSelection();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
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
        return "Họ tên: " + emp.getDisplayValue(emp.getFullName())
             + "\nMã NV: " + emp.getDisplayValue(emp.getEmployeeCode())
             + "\nChức danh: " + emp.getDisplayValue(emp.getPosition())
             + "\nSĐT: " + emp.getDisplayValue(emp.getPhone())
             + "\nEmail: " + emp.getDisplayValue(emp.getEmail())
             + "\nTrạng thái: " + emp.getDisplayValue(emp.getEmployeeStatus())
             + "\n";
    }

    public void setSearchKeyword(String kw) { this.searchKeyword = kw; }
    public void setFilterStatus(String s) { this.filterStatus = s; }
    public int getSelectedDeptId() { return selectedDeptId; }
}
