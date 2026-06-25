package controller.profileManagement;

import controller.MainController;
import dao.EmployeeDAO;
import enumModel.RoleEnum;
import model.hr.Employee;
import view.profileManagement.ProfileView;

import java.util.List;

public class ViewEmployeeProfileController {
    ProfileView pv;
    private EmployeeDAO employeeDAO;

    public ViewEmployeeProfileController(){
        pv = new ProfileView(this);
        this.employeeDAO = new EmployeeDAO();
    }

    public List<Employee> getEmployeeList() {
        return employeeDAO.findAll();
    }

    public boolean navigateTo() throws Exception{
        if(MainController.currentUser.getRole() != RoleEnum.EMPLOYER){
            return false;
        }
        pv.show();
        return true;
    }
}
