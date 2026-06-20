package view.calcSalary;

import model.calcSalary.AttendancePeriod;
import model.calcSalary.Parameter;
//import test.CalcSalaryControllerTest;
import view.View;
import controller.CalcSalaryController;

import java.util.ArrayList;
import java.util.List;

public class CalcSalaryView extends View {
    private Parameter parameter;
    private List<AttendancePeriod> attendancePeriods;
    CalcSalaryController calcSalaryController;
    public CalcSalaryView(CalcSalaryController calcSalaryController) {
        this.calcSalaryController = calcSalaryController;
    }

    @Override
    public void show() throws Exception {
        parameter = calcSalaryController.getParameter();
        attendancePeriods = calcSalaryController.getAttendancePeriods();

        calcSalaryMethod();
    }

    private void calcSalaryMethod() {

        while (true) {
            //  TODO: Hiện thị danh sách các tháng chấm công đã có
            printAttendancePeriod();

            // TODO: Chọn tháng chấm công


            // TODO: Chấm công cho tháng đã chọn

        }
    }

    private void printAttendancePeriod() {

    }
}
