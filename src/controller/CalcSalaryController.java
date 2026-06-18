package controller;

import enumModel.RoleEnum;
import model.User;
import view.calcSalary.ParameterSettingsView;

public class CalcSalaryController {
    ParameterSettingsView parameterSettingsView;
    public CalcSalaryController() {
        this.parameterSettingsView = new ParameterSettingsView(this);
    }
    public boolean execute(User current){
        //Kiem tra phan quyen
        if (!checkRole(current)) {return false;}
        //Chinh sua tham so
        parameterSettingsView.editParameter();
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
