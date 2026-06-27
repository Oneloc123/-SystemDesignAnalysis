package controller;

import dao.PayrollDAO;
import enumModel.RoleEnum;
//import model.Recruitment.Employer;
import model.User;
import model.calcSalary.Payroll;
import model.calcSalary.PayrollDetail;
import controller.profileManagement.ProfileController;
import controller.recruitmentManagement.RecruitmentManagementController;
import view.HomeView;
import view.View;

public class HomeController {
    HomeView hv;

    public HomeController() {
        if (MainController.currentUser == null) {
//            MainController.currentUser = new Employer();
            MainController.currentUser.setRole(RoleEnum.valueOf(RoleEnum.EMPLOYER.toString()));
        }
        this.hv = new HomeView(this);
    }

    public void show() throws Exception {
        hv.show();
    }

    public void excuteComent(String question) throws Exception {
        switch(question) {
            case "1":
                functionViewMyProfile();
                break;
            case "2":
                functionViewSchedule();
                break;
            case "3":
                ScreenManager.navigateTo("ChangePassword");
                break;
            case "4":
                ScreenManager.navigateTo("EmployeeList");
                break;
            case "5":
                ScreenManager.navigateTo("Attendance");
                break;
            case "6":
                if (!ScreenManager.navigateTo("RecruitmentManagement")) {
                    hv.showError("Không có quyền truy cập chức năng quản lý tuyển dụng");
                }
                break;
            case "7":
                if (MainController.currentUser != null
                        && MainController.currentUser.getRole() == RoleEnum.EMPLOYEE) {
                    xemBangLuongCaNhan();
                } else {
                    ScreenManager.navigateTo("CalcSalary");
                }
                break;
            case "8":
                if (!ScreenManager.navigateTo("ContractManagement")) {
                    hv.showError("Không có quyền truy cập chức năng quản lý hợp đồng");
                }
                break;
            default:
                hv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void functionViewSchedule() {
        ScreenManager.navigateTo("Schedule");
    }

    public void functionViewMyProfile() {
        ScreenManager.navigateTo("MyProfile");
    }

    /** Employee xem bang luong cua chinh minh */
    private void xemBangLuongCaNhan() {
        User current = MainController.currentUser;
        if (current == null) {
            System.out.println("Vui long dang nhap truoc.");
            return;
        }

        PayrollDAO payrollDAO = new PayrollDAO();
        java.util.List<Payroll> allPayrolls = payrollDAO.findAll();
        if (allPayrolls.isEmpty()) {
            System.out.println("\nChua co bang luong nao duoc tao.");
            return;
        }

        System.out.println("\n========== BANG LUONG CUA TOI ==========");

        boolean found = false;
        for (Payroll payroll : allPayrolls) {
            if (payroll.getDetails() == null) continue;
            for (PayrollDetail pd : payroll.getDetails()) {
                if (pd.getEmployeeId() == current.getUserId()) {
                    System.out.println("Thang " + payroll.getMonth() + "/" + payroll.getYear());
                    System.out.println("----------------------------------------");
                    System.out.println("Luong co ban:       " + String.format("%,.0f", pd.getBasicSalary()) + " VND");
                    System.out.println("Phu cap:            " + String.format("%,.0f", pd.getAllowance()) + " VND");
                    System.out.println("Ngay cong:          " + pd.getActualWorkingDays() + "/" + pd.getStandardWorkingDays());
                    System.out.println("Gio OT:             " + pd.getOvertimeHours() + "h");
                    System.out.println("Tong Gross:         " + String.format("%,.0f", pd.getGrossSalary()) + " VND");
                    System.out.println("BHXH:               " + String.format("%,.0f", pd.getSocialInsurance()) + " VND");
                    System.out.println("BHYT:               " + String.format("%,.0f", pd.getHealthInsurance()) + " VND");
                    System.out.println("BHTN:               " + String.format("%,.0f", pd.getUnemploymentInsurance()) + " VND");
                    System.out.println("Thue TNCN:          " + String.format("%,.0f", pd.getIncomeTax()) + " VND");
                    System.out.println("Luong thuc nhan:    " + String.format("%,.0f", pd.getNetSalary()) + " VND");
                    System.out.println("========================================\n");
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (!found) {
            System.out.println("Chua co bang luong cho ban.");
        }

        System.out.println("Nhan Enter de tiep tuc...");
        try {
            View.netIn.readLine();
        } catch (Exception e) {
            // ignore
        }
    }

}
