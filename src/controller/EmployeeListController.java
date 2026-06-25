package controller;

import controller.base.Controller;
import dao.DepartmentDAO;
import dao.EmployeeDAO;
import model.Department;
import model.hr.Employee;
import view.EmployeeListView;
import java.util.List;

public class EmployeeListController extends Controller {
    EmployeeListView employeeListView;
    private int selectedDeptId;
    private String searchKeyword = "";
    private String filterStatus = "";
    private DepartmentDAO departmentDAO;
    private EmployeeDAO employeeDAO;

    public EmployeeListController() {
        this.employeeListView = new EmployeeListView(this);
        this.view = this.employeeListView;
        this.departmentDAO = new DepartmentDAO();
        this.employeeDAO = new EmployeeDAO();
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
        return departmentDAO.findAll();
    }

    public boolean selectDepartment(int id) {
        Department dept = departmentDAO.findById(id);
        if (dept != null) {
            selectedDeptId = id;
            searchKeyword = "";
            filterStatus = "";
            return true;
        }
        return false;
    }

    public List<Employee> getFilteredEmployees() {
        return employeeDAO.search(searchKeyword, filterStatus, selectedDeptId);
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
