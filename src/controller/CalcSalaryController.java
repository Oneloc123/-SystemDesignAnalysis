package controller;

import dao.AttendanceDAO;
import dao.EmployeeDAO;
import model.hr.Employee;
import dao.ParameterDAO;
import dao.PayrollDAO;
import enumModel.RoleEnum;
import model.User;
import model.calcSalary.*;
import view.calcSalary.CalcSalaryView;
import view.calcSalary.ParameterSettingsView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CalcSalaryController {
    private Parameter parameter;
    private AttendanceDAO attendanceDAO;
    private PayrollDAO payrollDAO;
    private ParameterDAO parameterDAO;
    ParameterSettingsView parameterSettingsView;
    CalcSalaryView calcSalaryView;
    private Payroll currentPayroll;

    public CalcSalaryController() {
        this.attendanceDAO = new AttendanceDAO();
        this.payrollDAO = new PayrollDAO();
        this.parameterDAO = new ParameterDAO();

        // Load parameter from DB, fallback to default
        Parameter loaded = parameterDAO.load();
        if (loaded != null) {
            this.parameter = loaded;
        } else {
            this.parameter = new Parameter();
        }

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
        if (current == null || current.getRole() == null) return false;
        RoleEnum role = current.getRole();
        return role == RoleEnum.ACCOUNTANT || role == RoleEnum.ADMIN;
    }

    //Luu tham so tu View, co validation
    public String saveParameter(Parameter updatedParameter) {
        String errors = updatedParameter.validation();
        if (errors != null) {
            return errors;
        }
        // Save to DB
        parameterDAO.save(updatedParameter);
        this.parameter = updatedParameter;
        return null;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public List<AttendancePeriod> getAllAttendancePeriods() {
        // For now, return all periods from months 1-12 of a given year range
        // A more complete implementation would query a periods table
        return attendanceDAO.findAll();
    }

    public AttendancePeriod getAttendancePeriod(int month, int year) {
        return attendanceDAO.findByMonth(month, year);
    }

    public Payroll getCurrentPayroll() {
        return currentPayroll;
    }

    //tinh bang luong cho 1 ky cham cong
    public Payroll calculatePayroll(int month, int year) {
        AttendancePeriod period = attendanceDAO.findByMonth(month, year);

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

        // Save to DB
        payrollDAO.save(payroll);

        this.currentPayroll = payroll;
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
        double thue = tinhThueLuyTien(thuNhapChiuThue, param.getTaxBracket());
        pd.setIncomeTax(thue);

        //luong thuc nhan
        double net = tongGross - bhxh - bhyt - bhtn - thue;
        pd.setNetSalary(net);
    }

    private double tinhThueLuyTien(double thuNhap, List<TaxBracket> brackets) {
        double thue = 0;
        for (int i = 0; i < brackets.size(); i++) {
            TaxBracket b = brackets.get(i);
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
        attendanceDAO.save(period);
    }

    public void themChamCong(int month, int year, long employeeId, int ngayCong, int gioOT,
                             double basicSalary, double allowance, int dependentNumber) {
        AttendancePeriod period = attendanceDAO.findByMonth(month, year);
        if (period == null) return;

        AttendanceDetail ad = new AttendanceDetail();
        ad.setPeriodId(period.getId());
        ad.setEmployeeId(employeeId);
        ad.setActualWorkingDays(ngayCong);
        ad.setOvertimeHours(gioOT);
        ad.setBasicSalary(basicSalary);
        ad.setAllowance(allowance);
        ad.setDependentNumber(dependentNumber);
        period.addAttendanceDetail(ad);

        // Save detail directly to DB
        attendanceDAO.saveDetailOnly(ad);
    }

    public List<Payroll> loadPayrollHistory() {
        return payrollDAO.findAll();
    }

    public Payroll loadPayrollByMonth(int month, int year) {
        return payrollDAO.findByMonth(month, year);
    }

    public AttendanceDAO getAttendanceDAO() {
        return attendanceDAO;
    }

    public PayrollDAO getPayrollDAO() {
        return payrollDAO;
    }

    public ParameterDAO getParameterDAO() {
        return parameterDAO;
    }

    /** Xuat phieu luong cho tat ca nhan vien trong bang luong ra file text */
    public int xuatPhieuLuong(Payroll payroll) throws Exception {
        List<PayrollDetail> details = payroll.getDetails();
        if (details == null || details.isEmpty()) {
            System.out.println("Bang luong khong co du lieu.");
            return 0;
        }

        // Tao thu muc payslips neu chua co
        java.io.File dir = new java.io.File("payslips");
        if (!dir.exists()) dir.mkdir();

        EmployeeDAO empDAO = new EmployeeDAO();
        int count = 0;

        for (PayrollDetail pd : details) {
            String fileName = "payslips/phieu_luong_NV" + pd.getEmployeeId()
                    + "_" + payroll.getMonth() + "_" + payroll.getYear() + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, false))) {
                // Lay ten nhan vien
                String empName = "NV_" + pd.getEmployeeId();
                try {
                    Employee emp = empDAO.findById((int) pd.getEmployeeId());
                    if (emp != null) empName = emp.getFullName() + " (" + emp.getEmployeeCode() + ")";
                } catch (Exception e) {
                    // ignore, dung ten mac dinh
                }

                writer.println("========================================");
                writer.println("         PHIEU LUONG NHAN VIEN");
                writer.println("========================================");
                writer.println("Nhan vien: " + empName);
                writer.println("Ky luong: Thang " + payroll.getMonth() + "/" + payroll.getYear());
                writer.println("----------------------------------------");
                writer.println("Luong co ban:       " + String.format("%,.0f", pd.getBasicSalary()) + " VND");
                writer.println("Phu cap:            " + String.format("%,.0f", pd.getAllowance()) + " VND");
                writer.println("Ngay cong thuc te:  " + pd.getActualWorkingDays() + "/" + pd.getStandardWorkingDays());
                writer.println("Gio OT:             " + pd.getOvertimeHours() + "h");
                writer.println("----------------------------------------");
                writer.println("Tong Gross:         " + String.format("%,.0f", pd.getGrossSalary()) + " VND");
                writer.println("BHXH:               " + String.format("%,.0f", pd.getSocialInsurance()) + " VND");
                writer.println("BHYT:               " + String.format("%,.0f", pd.getHealthInsurance()) + " VND");
                writer.println("BHTN:               " + String.format("%,.0f", pd.getUnemploymentInsurance()) + " VND");
                writer.println("Thue TNCN:          " + String.format("%,.0f", pd.getIncomeTax()) + " VND");
                writer.println("----------------------------------------");
                writer.println("Luong thuc nhan:    " + String.format("%,.0f", pd.getNetSalary()) + " VND");
                writer.println("========================================");
                count++;
            }
        }
        return count;
    }

    /** Lay bang luong gan nhat cho mot nhan vien (dung cho Employee xem luong) */
    public PayrollDetail getLatestPayrollForEmployee(long employeeId) {
        List<Payroll> allPayrolls = payrollDAO.findAll();
        for (Payroll p : allPayrolls) {
            List<PayrollDetail> details = p.getDetails();
            if (details != null) {
                for (PayrollDetail d : details) {
                    if (d.getEmployeeId() == employeeId) {
                        return d;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Import cham cong tu file CSV.
     * Format: ma_nv,ngay_cong,gio_ot
     * Luong co ban & phu cap se tu dong lay tu ho so nhan vien.
     */
    public String importAttendanceFromCsv(String filePath, int month, int year) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "File khong ton tai: " + filePath;
        }

        // Tim hoac tao ky cham cong
        AttendancePeriod period = attendanceDAO.findByMonth(month, year);
        if (period == null) {
            period = new AttendancePeriod();
            period.setMonth(month);
            period.setYear(year);
            attendanceDAO.save(period);
        }

        EmployeeDAO empDAO = new EmployeeDAO();
        int total = 0;
        int success = 0;
        int skipped = 0;
        StringBuilder errors = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;

                // Bo qua dong header
                if (lineNum == 1 && line.toLowerCase().startsWith("ma_nv")) continue;

                total++;
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    errors.append("  Dong ").append(lineNum).append(": Thieu cot (can ma_nv,ngay_cong,gio_ot)\n");
                    skipped++;
                    continue;
                }

                String maNV = parts[0].trim();
                int ngayCong, gioOT;

                try {
                    ngayCong = Integer.parseInt(parts[1].trim());
                    gioOT = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    errors.append("  Dong ").append(lineNum).append(": ngay_cong hoac gio_ot khong phai so\n");
                    skipped++;
                    continue;
                }

                if (ngayCong < 0 || gioOT < 0) {
                    errors.append("  Dong ").append(lineNum).append(": Gia tri am khong hop le\n");
                    skipped++;
                    continue;
                }

                // Tra thong tin nhan vien tu DB
                Employee emp = empDAO.findByEmployeeCode(maNV);
                if (emp == null) {
                    errors.append("  Dong ").append(lineNum).append(": Khong tim thay ma NV ").append(maNV).append("\n");
                    skipped++;
                    continue;
                }

                // Kiem tra trung (bo qua neu da co cham cong cho NV nay trong ky)
                boolean exists = false;
                for (AttendanceDetail existing : period.getAttendanceDetails()) {
                    if (existing.getEmployeeId() == emp.getUserId()) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    errors.append("  Dong ").append(lineNum).append(": NV ").append(maNV).append(" da co cham cong trong ky nay\n");
                    skipped++;
                    continue;
                }

                // Tao AttendanceDetail
                AttendanceDetail detail = new AttendanceDetail();
                detail.setPeriodId(period.getId());
                detail.setEmployeeId(emp.getUserId());
                detail.setEmployeeCode(emp.getEmployeeCode());
                detail.setEmployeeName(emp.getFullName());
                detail.setActualWorkingDays(ngayCong);
                detail.setStandardDays(parameter.getStandardWorkingDays());
                detail.setOvertimeHours(gioOT);
                detail.setUnpaidLeave(0);
                detail.setPaidLeave(0);
                detail.setStatus("present");

                // Lay luong co ban & phu cap tu ho so nhan vien
                double basicSalary = (emp.getBaseSalary() != null) ? emp.getBaseSalary() : 0;
                double allowance = (emp.getFixedAllowance() != null) ? emp.getFixedAllowance() : 0;
                detail.setBasicSalary(basicSalary);
                detail.setAllowance(allowance);
                detail.setDependentNumber(emp.getDependentNumber());

                period.addAttendanceDetail(detail);
                attendanceDAO.saveDetailOnly(detail);
                success++;
            }
        } catch (IOException e) {
            return "Loi doc file: " + e.getMessage();
        }

        StringBuilder result = new StringBuilder();
        result.append("Ket qua import: ").append(success).append(" thanh cong");
        if (skipped > 0) result.append(", ").append(skipped).append(" bo qua");
        result.append(" (tong ").append(total).append(" dong)\n");
        if (errors.length() > 0) {
            result.append("Chi tiet loi:\n").append(errors);
        }
        return result.toString();
    }
}
