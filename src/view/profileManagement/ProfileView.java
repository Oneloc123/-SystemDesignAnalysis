package view.profileManagement;

import controller.MainController;
import controller.profileManagement.ProfileController;
import enumModel.AddressEnum;
import model.hr.Employee;
import view.View;

public class ProfileView extends View {
    ProfileController controller;
    AddressEnum address = AddressEnum.Profile;

    public ProfileView(ProfileController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showProfile(Employee emp) {
        boolean hasMissing = false;
        StringBuilder missingFields = new StringBuilder();

        System.out.println("\n========== THÔNG TIN CÁ NHÂN ==========");

        System.out.println("\n--- Thông tin cá nhân ---");
        String fullName = emp.getDisplayValue(emp.getFullName());
        String dob = emp.getDateOfBirth() != null ? emp.getDateOfBirth().toString() : "Chưa cập nhật";
        String gender = emp.getDisplayValue(emp.getGender());
        String idCard = emp.getMaskedIdCard();
        String hometown = emp.getDisplayValue(emp.getHometown());
        String address = emp.getDisplayValue(emp.getAddress());
        String phone = emp.getDisplayValue(emp.getPhone());
        String email = emp.getDisplayValue(emp.getEmail());

        System.out.println("Họ tên: " + fullName);
        System.out.println("Ngày sinh: " + dob);
        System.out.println("Giới tính: " + gender);
        System.out.println("CCCD: " + idCard);
        System.out.println("Quê quán: " + hometown);
        System.out.println("Địa chỉ thường trú: " + address);
        System.out.println("SĐT: " + phone);
        System.out.println("Email: " + email);

        if (emp.getFullName() == null) { hasMissing = true; missingFields.append("- Họ tên\n"); }
        if (emp.getCitizenIdentificationCard() == null) { hasMissing = true; missingFields.append("- CCCD\n"); }

        System.out.println("\n--- Thông tin công việc ---");
        String empCode = emp.getDisplayValue(emp.getEmployeeCode());
        String dept = emp.getDisplayValue(emp.getDepartmentName());
        String position = emp.getDisplayValue(emp.getPosition());
        String startDate = emp.getDisplayValue(emp.getStartDate());
        String contract = emp.getDisplayValue(emp.getContractType());
        String status = emp.getDisplayValue(emp.getEmployeeStatus());

        System.out.println("Mã NV: " + empCode);
        System.out.println("Phòng ban: " + dept);
        System.out.println("Chức danh: " + position);
        System.out.println("Ngày vào làm: " + startDate);
        System.out.println("Loại hợp đồng: " + contract);
        System.out.println("Trạng thái: " + status);

        System.out.println("\n--- Lương cơ bản ---");
        Double baseSalary = emp.getBaseSalary();
        Double allowance = emp.getFixedAllowance();
        System.out.println("Lương cơ bản: " + (baseSalary != null ? String.format("%,.0fđ", baseSalary) : "Chưa cập nhật"));
        System.out.println("Phụ cấp: " + (allowance != null ? String.format("%,.0fđ", allowance) : "Chưa cập nhật"));

        System.out.println("\n--- Bảo hiểm & Thuế ---");
        System.out.println("Số sổ BHXH: " + emp.getDisplayValue(emp.getSocialInsuranceNumber()));
        System.out.println("Mã số thuế TNCN: " + emp.getDisplayValue(emp.getTaxCode()));

        System.out.println("\n--- Tài khoản ngân hàng ---");
        System.out.println("Tên ngân hàng: " + emp.getDisplayValue(emp.getBankName()));
        System.out.println("Số tài khoản: " + emp.getMaskedBankAccount());
        System.out.println("Chủ tài khoản: " + emp.getDisplayValue(emp.getBankAccountHolder()));

        System.out.println("\n--- Trình độ ---");
        System.out.println("Trình độ: " + emp.getDisplayValue(emp.getQualification()));
        System.out.println("Chuyên ngành: " + emp.getDisplayValue(emp.getMajor()));
        System.out.println("Kinh nghiệm: " + emp.getDisplayValue(emp.getExperience()));

        if (hasMissing) {
            System.out.println("\n------------------------");
            System.out.println("Một số thông tin chưa được cập nhật, vui lòng liên hệ HR.");
            System.out.println("Các trường thiếu:");
            System.out.println(missingFields.toString());
        }

        System.out.println("========================================");
        System.out.println("Nhấn Enter để quay lại...");
        try {
            netIn.readLine();
        } catch (Exception e) {
        } finally {
            MainController.addresses.remove(address);
        }
    }
}
