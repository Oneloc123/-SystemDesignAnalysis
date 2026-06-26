package controller.profileManagement;

import controller.ScreenManager;
import controller.base.Controller;
import model.Department;
import model.hr.Employee;
import view.profileManagement.MyProfileView;

public class MyProfileController extends Controller {
    private MyProfileView myProfileView;

    public MyProfileController() {
        this.myProfileView = new MyProfileView(this);
        this.view = this.myProfileView;
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
        return Employee.findByUserId(userId);
    }

    public String getDepartmentName(Integer departmentId) {
        if (departmentId == null) return null;
        Department dept = Department.findById(departmentId);
        return dept != null ? dept.getName() : null;
    }
}
