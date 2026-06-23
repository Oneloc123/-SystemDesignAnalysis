package controller;

import enumModel.RoleEnum;
import model.User;
import model.calcSalary.AttendancePeriod;
import model.calcSalary.Parameter;
import view.calcSalary.CalcSalaryView;
import view.calcSalary.ParameterSettingsView;

import java.util.ArrayList;
import java.util.List;

public class CalcSalaryController {
    private Parameter parameter;
    private List<AttendancePeriod>  attendancePeriods;
    ParameterSettingsView parameterSettingsView;
    CalcSalaryView calcSalaryView;

    public CalcSalaryController() {
        // Todo: Sau phải lấy dữ liệu  từ data base thay vào. Không được để new
        this.parameter = new Parameter();
        this.attendancePeriods = new ArrayList<AttendancePeriod>();

        this.parameterSettingsView = new ParameterSettingsView(this);
        this.calcSalaryView = new CalcSalaryView(this);
    }

    public boolean execute(User current) throws Exception {
        //Chinh sua tham so
        parameterSettingsView.show();
        //Tinh luong
        calcSalaryView.show();
        return true;
    }

    public boolean checkRole(User current) {
        RoleEnum role = current.getRoleEnum();
        if (role == RoleEnum.ACCOUNT || role == RoleEnum.ADMIN) {
            return true;
        }
        return false;
    }

    //Luu tham so tu View, co validation
    public String saveParameter(Parameter updatedParameter) {
        String errors = updatedParameter.validation();
        if (errors != null) {
            return errors;
        }
        this.parameter = updatedParameter;
        return null;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public List<AttendancePeriod> getAttendancePeriods() {
        return attendancePeriods;
    }

    public void setAttendancePeriods(List<AttendancePeriod> attendancePeriods) {
        this.attendancePeriods = attendancePeriods;
    }
}
