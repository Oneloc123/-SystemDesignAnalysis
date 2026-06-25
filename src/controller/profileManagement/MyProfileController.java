package controller.profileManagement;

import controller.ScreenManager;
import controller.base.Controller;
import dao.DepartmentDAO;
import dao.EmployeeDAO;
import model.Department;
import model.hr.Employee;
import view.profileManagement.MyProfileView;

public class MyProfileController extends Controller {
    private MyProfileView myProfileView;
    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;

    public MyProfileController() {
        this.myProfileView = new MyProfileView(this);
        this.view = this.myProfileView;
        this.employeeDAO = new EmployeeDAO();
        this.departmentDAO = new DepartmentDAO();
    }

    @Override
    public void showOn() {
        myProfileView.showProfile();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public Employee getMyProfile() {
        int userId = ScreenManager.getCurrentUser().getUserId();
        if (userId <= 0) return null;
        return employeeDAO.findByUserId(userId);
    }

    public String getDepartmentName(Integer departmentId) {
        if (departmentId == null) return null;
        Department dept = departmentDAO.findById(departmentId);
        return dept != null ? dept.getName() : null;
    }
}
