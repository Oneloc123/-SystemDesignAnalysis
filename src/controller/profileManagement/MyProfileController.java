package controller.profileManagement;

import controller.ScreenManager;
import model.Department;
import model.hr.Employee;
import view.profileManagement.MyProfileView;

public class MyProfileController {
    private MyProfileView myProfileView;

    public MyProfileController() {
        this.myProfileView = new MyProfileView(this);
    }

    public void showOn() {
        myProfileView.showProfile();
    }

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
