package view.calcSalary;

import model.calcSalary.*;
import model.calcSalary.AttendancePeriod;
import model.calcSalary.Parameter;
import view.View;
import controller.CalcSalaryController;
import dao.AttendanceDAO;

import java.util.List;

public class CalcSalaryView extends View {
    private Parameter parameter;
    private List<AttendancePeriod> attendancePeriods;
    CalcSalaryController calcSalaryController;
    AttendanceDAO attendanceDAO;

    public CalcSalaryView(CalcSalaryController calcSalaryController) {
        this.calcSalaryController = calcSalaryController;
        this.attendanceDAO = calcSalaryController.getAttendanceDAO();
    }

    @Override
    public void show() throws Exception {
        parameter = calcSalaryController.getParameter();
        attendancePeriods = calcSalaryController.getAllAttendancePeriods();

        calcSalaryMethod();
    }

    private void calcSalaryMethod() {
        while (true) {
            System.out.println("\n========== TINH LUONG ==========");
            System.out.println("Danh sach ky cham cong:");
            printAttendancePeriod();
            System.out.println("-------------------------------");
            System.out.println("a. Tao ky cham cong moi");
            System.out.println("b. Cham cong cho ky");
            System.out.println("c. Tinh luong");
            System.out.println("d. Xem bang luong hien tai");
            System.out.println("e. Lich su bang luong");
            System.out.println("0. Quay lai");
            System.out.println("-------------------------------");
            System.out.print("Chon: ");

            try {
                String input = netIn.readLine().trim().toLowerCase();
                if (input.equals("0")) break;

                switch (input) {
                    case "a":
                        themKyChamCong();
                        break;
                    case "b":
                        chamCong();
                        break;
                    case "c":
                        tinhLuong();
                        break;
                    case "d":
                        xemBangLuong();
                        break;
                    case "e":
                        lichSuBangLuong();
                        break;
                    default:
                        System.out.println("Lua chon ko hop le.");
                }
            } catch (Exception e) {
                System.out.println("Loi: " + e.getMessage());
            }
        }
    }

    private void printAttendancePeriod() {
        if (attendancePeriods.isEmpty()) {
            System.out.println("  (chua co ky cham cong nao)");
            return;
        }
        for (int i = 0; i < attendancePeriods.size(); i++) {
            AttendancePeriod ap = attendancePeriods.get(i);
            int soNV = ap.getAttendanceDetails().size();
            System.out.println("  " + (i + 1) + ". Thang " + ap.getMonth() + "/" + ap.getYear()
                    + " (" + soNV + " nv)");
        }
    }

    // Refresh data from DB
    private void refreshData() {
        parameter = calcSalaryController.getParameter();
        attendancePeriods = calcSalaryController.getAllAttendancePeriods();
    }

    private void themKyChamCong() throws Exception {
        System.out.print("Nhap thang (1-12): ");
        int month = Integer.parseInt(netIn.readLine().trim());
        System.out.print("Nhap nam: ");
        int year = Integer.parseInt(netIn.readLine().trim());

        //kiem tra trung
        for (int i = 0; i < attendancePeriods.size(); i++) {
            AttendancePeriod ap = attendancePeriods.get(i);
            if (ap.getMonth() == month && ap.getYear() == year) {
                System.out.println("Ky cham cong da ton tai.");
                return;
            }
        }

        calcSalaryController.themAttendancePeriod(month, year);
        refreshData();
        System.out.println("Da tao ky cham cong thang " + month + "/" + year);
    }

    private void chamCong() throws Exception {
        if (attendancePeriods.isEmpty()) {
            System.out.println("Chua co ky cham cong nao. Tao ky moi truoc.");
            return;
        }

        printAttendancePeriod();
        System.out.print("Chon ky cham cong (nhap so thu tu): ");
        int idx = Integer.parseInt(netIn.readLine().trim()) - 1;
        if (idx < 0 || idx >= attendancePeriods.size()) {
            System.out.println("So thu tu ko hop le.");
            return;
        }

        AttendancePeriod period = attendancePeriods.get(idx);
        System.out.println("--- Cham cong thang " + period.getMonth() + "/" + period.getYear() + " ---");

        //hien thi danh sach cham cong hien co
        List<AttendanceDetail> ds = period.getAttendanceDetails();
        if (!ds.isEmpty()) {
            System.out.println("Danh sach nhan vien da cham:");
            for (int i = 0; i < ds.size(); i++) {
                AttendanceDetail ad = ds.get(i);
                System.out.println("  " + (i + 1) + ". Ma NV: " + ad.getEmployeeId()
                        + " - Ngay cong: " + ad.getActualWorkingDays()
                        + " - OT: " + ad.getOvertimeHours() + "h");
            }
        }

        //them moi
        while (true) {
            System.out.println("\n--- Them nhan vien (nhap 0 de ket thuc) ---");
            System.out.print("Ma nhan vien: ");
            String maInput = netIn.readLine().trim();
            if (maInput.equals("0")) break;

            long empId = Long.parseLong(maInput);
            System.out.print("Luong co ban: ");
            double basicSalary = Double.parseDouble(netIn.readLine().trim());
            System.out.print("Phu cap: ");
            double allowance = Double.parseDouble(netIn.readLine().trim());
            System.out.print("So nguoi phu thuoc: ");
            int dependentNumber = Integer.parseInt(netIn.readLine().trim());
            System.out.print("Ngay cong thuc te: ");
            int ngayCong = Integer.parseInt(netIn.readLine().trim());
            System.out.print("Gio OT: ");
            int gioOT = Integer.parseInt(netIn.readLine().trim());

            calcSalaryController.themChamCong(
                    period.getMonth(), period.getYear(),
                    empId, ngayCong, gioOT,
                    basicSalary, allowance, dependentNumber);
            System.out.println("Da cham cong cho ma NV " + empId);
        }

        refreshData();
    }

    private void tinhLuong() throws Exception {
        if (attendancePeriods.isEmpty()) {
            System.out.println("Chua co du lieu cham cong.");
            return;
        }

        printAttendancePeriod();
        System.out.print("Chon ky can tinh luong (so thu tu): ");
        int idx = Integer.parseInt(netIn.readLine().trim()) - 1;
        if (idx < 0 || idx >= attendancePeriods.size()) {
            System.out.println("So thu tu ko hop le.");
            return;
        }

        AttendancePeriod period = attendancePeriods.get(idx);
        if (period.getAttendanceDetails().isEmpty()) {
            System.out.println("Ky cham cong chua co du lieu. Cham cong truoc.");
            return;
        }

        if (parameter == null || parameter.getTaxBraket() == null || parameter.getTaxBraket().isEmpty()) {
            System.out.println("Tham so tinh luong chua duoc cau hinh. Vao chinh sua tham so truoc.");
            return;
        }

        String err = parameter.validation();
        if (err != null) {
            System.out.println("Tham so ko hop le: " + err);
            return;
        }

        System.out.println("Dang tinh luong cho thang " + period.getMonth() + "/" + period.getYear() + "...");
        Payroll payroll = calcSalaryController.calculatePayroll(period.getMonth(), period.getYear());

        if (payroll == null) {
            System.out.println("Tinh luong that bai.");
            return;
        }

        System.out.println("Tinh luong thanh cong!");
        hienThiBangLuong(payroll);
    }

    private void xemBangLuong() {
        Payroll payroll = calcSalaryController.getCurrentPayroll();
        if (payroll == null) {
            System.out.println("Chua co bang luong. Tinh luong truoc.");
            return;
        }
        hienThiBangLuong(payroll);
    }

    private void lichSuBangLuong() {
        List<Payroll> history = calcSalaryController.loadPayrollHistory();
        if (history.isEmpty()) {
            System.out.println("Chua co bang luong nao trong he thong.");
            return;
        }
        System.out.println("\n--- LICH SU BANG LUONG ---");
        for (int i = 0; i < history.size(); i++) {
            Payroll p = history.get(i);
            System.out.println("  " + (i + 1) + ". Thang " + p.getMonth() + "/" + p.getYear()
                    + " - Tong Gross: " + String.format("%,.0f", p.getTotalGross())
                    + " - Tong Net: " + String.format("%,.0f", p.getTotalNet()));
        }
        System.out.print("\nNhap so thu tu de xem chi tiet (0 de quay lai): ");
        try {
            String input = netIn.readLine().trim();
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < history.size()) {
                hienThiBangLuong(history.get(idx));
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private void hienThiBangLuong(Payroll payroll) {
        System.out.println("\n========== BANG LUONG ==========");
        System.out.println("  Thang " + payroll.getMonth() + "/" + payroll.getYear());

        List<PayrollDetail> details = payroll.getDetails();
        if (details == null || details.isEmpty()) {
            System.out.println("  (khong co du lieu)");
            return;
        }

        System.out.println("+-----+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.println("| STT | Ma NV      | Gross     | BHXH      | BHYT      | BHTN      | TN chiu    | Thue TNCN | Net       |");
        System.out.println("|     |            |           |           |           |           | thue       |           |           |");
        System.out.println("+-----+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        for (int i = 0; i < details.size(); i++) {
            PayrollDetail pd = details.get(i);
            System.out.printf("| %-3d | %-10d | %,.0f    | %,.0f    | %,.0f    | %,.0f    | %,.0f    | %,.0f    | %,.0f    |\n",
                    (i + 1),
                    pd.getEmployeeId(),
                    pd.getGrossSalary(),
                    pd.getSocialInsurance(),
                    pd.getHealthInsurance(),
                    pd.getUnemploymentInsurance(),
                    pd.getTaxableIncome(),
                    pd.getIncomeTax(),
                    pd.getNetSalary());
        }

        System.out.println("+-----+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
        System.out.printf("| TONG |            | %,.0f    | %,.0f                              | %,.0f    | %,.0f    |\n",
                payroll.getTotalGross(),
                payroll.getTotalInsurance(),
                payroll.getTotalTax(),
                payroll.getTotalNet());
        System.out.println("+-----+------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");

        System.out.println("Ghi chu:");
        System.out.println("  Gross = Luong CB + Phu cap");
        System.out.println("  Luong thuc te = Gross x (Ngay cong / Ngay chuan)");
        System.out.println("  Luong OT = (Luong CB / Ngay chuan) x Gio OT x He so OT");
        System.out.println("  Net = Gross - BHXH - BHYT - BHTN - Thue TNCN");
    }
}
