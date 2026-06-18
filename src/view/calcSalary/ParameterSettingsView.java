package view.calcSalary;

import controller.CalcSalaryController;
import model.calcSalary.Parameter;
import view.View;

public class ParameterSettingsView extends View {
    private CalcSalaryController calcSalaryController;
    public ParameterSettingsView(CalcSalaryController calcSalaryController) {
        calcSalaryController = calcSalaryController;
    }

    @Override
    public void show() throws Exception {
        editParameter();

    }

    public void editParameter() {

        //Lấy Parameter cũ. Hiện thị Parameter
        Parameter parameter = new Parameter();
        //parameter.loadData() lấy dữ liệu từ data base




        //Có chỉnh sửa hay không?

        // Có: Chỉnh sửa Parameter
            // Validate Parameter
                // True: updateParameter
                // False: quay về màn chỉnh tham số


        // Không: Chuyển qua lấy dữ liệu chấm công ...

    }


}
