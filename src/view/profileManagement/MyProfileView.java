package view.profileManagement;

import controller.MainController;
import controller.profileManagement.MyProfileController;
import enumModel.AddressEnum;
import model.hr.Employee;
import view.View;

import java.text.SimpleDateFormat;

public class MyProfileView extends View {
    private MyProfileController controller;
    private AddressEnum address = AddressEnum.MyProfile;

    public MyProfileView(MyProfileController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showProfile() {
        if (!controller.checkAuth()) {
            showError("Không có quyền truy cập");
            MainController.addresses.remove(address);
            return;
        }

        Employee emp = controller.getMyProfile();
        if (emp == null) {
            showError("Không tìm thấy thông tin cá nhân. Vui lòng liên hệ HR để được hỗ trợ.");
            MainController.addresses.remove(address);
            return;
        }

        String deptName = controller.getDepartmentName(emp.getDepartmentId());

        while (true) {
            try {
                printAddress();
                System.out.println("\n================== HỒ SƠ CÁ NHÂN ==================\n");

                System.out.println("--- THÔNG TIN CÁ NHÂN ---");
                System.out.println("  Họ tên        : " + emp.getDisplayValue(emp.getFullName()));
                System.out.println("  Ngày sinh     : " + formatDate(emp.getDateOfBirth()));
                System.out.println("  Giới tính     : " + emp.getDisplayValue(emp.getGender()));
                System.out.println("  CCCD          : " + emp.getMaskedIdCard());
                System.out.println("  Quê quán      : " + emp.getDisplayValue(emp.getHometown()));
                System.out.println("  Địa chỉ       : " + emp.getDisplayValue(emp.getAddress()));
                System.out.println("  SĐT           : " + emp.getDisplayValue(emp.getPhone()));
                System.out.println("  Email         : " + emp.getDisplayValue(emp.getEmail()));
                System.out.println();

                System.out.println("--- THÔNG TIN CÔNG VIỆC ---");
                System.out.println("  Mã NV         : " + emp.getDisplayValue(emp.getEmployeeCode()));
                System.out.println("  Phòng ban     : " + emp.getDisplayValue(deptName));
                System.out.println("  Chức danh     : " + emp.getDisplayValue(emp.getPosition()));
                System.out.println("  Ngày vào làm  : " + emp.getDisplayValue(emp.getStartDate()));
                System.out.println("  Loại HĐ       : " + emp.getDisplayValue(emp.getContractType()));
                System.out.println("  Trạng thái    : " + emp.getDisplayValue(emp.getEmployeeStatus()));
                System.out.println();

                System.out.println("--- LƯƠNG CƠ BẢN ---");
                System.out.println("  Lương cơ bản  : " + formatSalary(emp.getBaseSalary()));
                System.out.println("  Phụ cấp       : " + formatSalary(emp.getFixedAllowance()));
                System.out.println();

                System.out.println("--- BẢO HIỂM & THUẾ ---");
                System.out.println("  Số sổ BHXH    : " + emp.getDisplayValue(emp.getSocialInsuranceNumber()));
                System.out.println("  Mã số thuế    : " + emp.getDisplayValue(emp.getTaxCode()));
                System.out.println();

                System.out.println("--- TÀI KHOẢN NGÂN HÀNG ---");
                System.out.println("  Ngân hàng     : " + emp.getDisplayValue(emp.getBankName()));
                System.out.println("  Số tài khoản  : " + emp.getMaskedBankAccount());
                System.out.println("  Chủ tài khoản : " + emp.getDisplayValue(emp.getBankAccountHolder()));
                System.out.println();

                System.out.println("====================================================");
                System.out.println("0. Quay lại");
                printAddress();
                System.out.print("Chọn: ");

                handleInput();
                if ("0".equals(question)) {
                    break;
                } else {
                    showError("Lệnh không hợp lệ");
                }
            } catch (Exception e) {
                showError("Lỗi hiển thị thông tin. Vui lòng thử lại sau.");
                break;
            }
        }

        MainController.addresses.remove(address);
    }

    private String formatDate(java.util.Date date) {
        if (date == null) return "Chưa cập nhật";
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            return "Chưa cập nhật";
        }
    }

    private String formatSalary(Double salary) {
        if (salary == null) return "Chưa cập nhật";
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        return df.format(salary) + " VND";
    }
}
