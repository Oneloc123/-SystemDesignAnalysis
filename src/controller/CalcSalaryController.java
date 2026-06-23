package controller;

import enumModel.RoleEnum;
import model.User;
import model.calcSalary.*;
import view.calcSalary.CalcSalaryView;
import view.calcSalary.ParameterSettingsView;

import java.util.ArrayList;
import java.util.List;

public class CalcSalaryController {
    private Parameter parameter;
    private List<AttendancePeriod>  attendancePeriods;
    ParameterSettingsView parameterSettingsView;
    CalcSalaryView calcSalaryView;
    private Payroll payroll;

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
        RoleEnum role = current.getRole();
        if (role == RoleEnum.ACCOUNTANT || role == RoleEnum.ADMIN) {
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

    public Payroll getPayroll() {
        return payroll;
    }

    //tinh bang luong cho 1 ky cham cong
    public Payroll calculatePayroll(int month, int year) {
        AttendancePeriod period = null;
        for (int i = 0; i < attendancePeriods.size(); i++) {
            AttendancePeriod ap = attendancePeriods.get(i);
            if (ap.getMonth() == month && ap.getYear() == year) {
                period = ap;
                break;
            }
        }

        if (period == null) return null;
        if (period.getAttendanceDetails().isEmpty()) return null;

        Payroll payroll = new Payroll();
        payroll.setMonth(month);
        payroll.setYear(year);

        List<PayrollDetail> details = new ArrayList<>();
        double tongGross = 0, tongNet = 0, tongThue = 0, tongBH = 0;

        List<AttendanceDetail> att = period.getAttendanceDetails();
        for (int i = 0; i < att.size(); i++) {
            AttendanceDetail ad = att.get(i);
            PayrollDetail pd = new PayrollDetail();
            pd.setEmployeeId(ad.getEmployeeId());
            pd.setActualWorkingDays(ad.getActualWorkingDays());
            pd.setStandardWorkingDays(parameter.getStandardWorkingDays());
            pd.setOvertimeHours(ad.getOvertimeHours());
            pd.setBasicSalary(ad.getBasicSalary());
            pd.setAllowance(ad.getAllowance());

            tinhLuongChoMotNhanVien(pd, parameter, ad.getDependentNumber());

            details.add(pd);
            tongGross += pd.getGrossSalary();
            tongNet += pd.getNetSalary();
            tongThue += pd.getIncomeTax();
            tongBH += pd.getSocialInsurance() + pd.getHealthInsurance() + pd.getUnemploymentInsurance();
        }

        payroll.setDetails(details);
        payroll.setTotalGross(tongGross);
        payroll.setTotalNet(tongNet);
        payroll.setTotalTax(tongThue);
        payroll.setTotalInsurance(tongBH);

        this.payroll = payroll;
        return payroll;
    }

    public void tinhLuongChoMotNhanVien(PayrollDetail pd, Parameter param, int dependentNumber) {
        //luong co ban + phu cap
        double gross = pd.getBasicSalary() + pd.getAllowance();
        //luong theo ngay cong thuc te
        double luongThucTe = gross * pd.getActualWorkingDays() / pd.getStandardWorkingDays();
        //luong OT
        double luongOT = (pd.getBasicSalary() / pd.getStandardWorkingDays()) * pd.getOvertimeHours() * param.getOvertimeRate();
        double tongGross = luongThucTe + luongOT;
        pd.setGrossSalary(tongGross);

        //bao hiem
        double bhxh = tongGross * param.getSocialInsuranceRate();
        double bhyt = tongGross * param.getHealthInsuranceRate();
        double bhtn = tongGross * param.getUnemploymentInsuranceRate();
        pd.setSocialInsurance(bhxh);
        pd.setHealthInsurance(bhyt);
        pd.setUnemploymentInsurance(bhtn);

        //thu nhap chiu thue = Gross - BH - giam tru ban than - giam tru nguoi phu thuoc
        double giamTru = param.getPersonalDeduction() + param.getDependentDeduction() * dependentNumber;
        double thuNhapChiuThue = tongGross - bhxh - bhyt - bhtn - giamTru;
        if (thuNhapChiuThue < 0) thuNhapChiuThue = 0;
        pd.setTaxableIncome(thuNhapChiuThue);

        //tinh thue TNCN luy tien
        double thue = tinhThueLuyTien(thuNhapChiuThue, param.getTaxBraket());
        pd.setIncomeTax(thue);

        //luong thuc nhan
        double net = tongGross - bhxh - bhyt - bhtn - thue;
        pd.setNetSalary(net);
    }

    private double tinhThueLuyTien(double thuNhap, List<TaxBraket> brackets) {
        double thue = 0;
        for (int i = 0; i < brackets.size(); i++) {
            TaxBraket b = brackets.get(i);
            if (thuNhap > b.getMinIncome()) {
                double trongKhoan = Math.min(thuNhap, b.getMaxIncome()) - b.getMinIncome();
                if (trongKhoan > 0) {
                    thue += trongKhoan * b.getTaxRate();
                }
            }
        }
        return thue;
    }

    public void themAttendancePeriod(int month, int year) {
        AttendancePeriod period = new AttendancePeriod();
        period.setMonth(month);
        period.setYear(year);
        attendancePeriods.add(period);
    }

    public void themChamCong(int month, int year, long employeeId, int ngayCong, int gioOT,
                             double basicSalary, double allowance, int dependentNumber) {
        for (int i = 0; i < attendancePeriods.size(); i++) {
            AttendancePeriod ap = attendancePeriods.get(i);
            if (ap.getMonth() == month && ap.getYear() == year) {
                AttendanceDetail ad = new AttendanceDetail();
                ad.setEmployeeId(employeeId);
                ad.setActualWorkingDays(ngayCong);
                ad.setOvertimeHours(gioOT);
                ad.setBasicSalary(basicSalary);
                ad.setAllowance(allowance);
                ad.setDependentNumber(dependentNumber);
                ap.addAttendanceDetail(ad);
                break;
            }
        }
    }
}
