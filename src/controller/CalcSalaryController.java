package controller;

import model.RoleEnum;
import model.User;

public class CalcSalaryController {
    public CalcSalaryController() {}
    public boolean execute(User current){
        RoleEnum role = current.getRole();
        // validate
        if(role.equals("admin")){
//            hv.show();
            return true;}
        return false;
    }
}
