package controller;

import enumModel.RoleEnum;
import model.User;

public class CalcSalaryController {
    public CalcSalaryController() {}
    public boolean execute(User current){
        //Kiem tra phan quyen
        if (!checkRole(current)) {return false;}
        //Chinh sua tham so

        //Tinh luong
        return  true;
    }
    public boolean checkRole(User current){
        RoleEnum role = current.getRole();
        if(role.equals(RoleEnum.ACCOUNT)){
            return true;}
        return false;
    }

    public void parameterSettings(){

    }
}
