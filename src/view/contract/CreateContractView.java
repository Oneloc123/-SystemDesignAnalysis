package view.contract;

import controller.MainController;
import controller.contract.CreateContractController;
import enumModel.AddressEnum;
import view.View;

public class CreateContractView extends View {
    private CreateContractController controller;
    private AddressEnum address = AddressEnum.CreateContract;

    public CreateContractView(CreateContractController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        MainController.addresses.add(address);
        try {
            doShow();
        } finally {
            MainController.addresses.remove(address);
        }
    }

    private void doShow() throws Exception {
        printWelcome("TẠO HỢP ĐỒNG MỚI");

        String contractCode = handleParam("mã hợp đồng (VD: HD2024001)");
        if (contractCode == null || contractCode.trim().isEmpty()) {
            showError("Mã hợp đồng không được để trống");
            return;
        }

        String employeeCodeInput = handleParam("mã nhân viên");
        if (employeeCodeInput == null || employeeCodeInput.trim().isEmpty()) {
            showError("Mã nhân viên không được để trống");
            return;
        }

        System.out.println("Loại hợp đồng:");
        System.out.println("  1. Thử việc");
        System.out.println("  2. Xác định thời hạn");
        System.out.println("  3. Không xác định thời hạn");
        String typeChoice = handleParam("loại hợp đồng (1/2/3)");
        String contractType;
        switch (typeChoice.trim()) {
            case "1": contractType = "Thử việc"; break;
            case "2": contractType = "Xác định thời hạn"; break;
            case "3": contractType = "Không xác định thời hạn"; break;
            default:
                showError("Lựa chọn không hợp lệ");
                return;
        }

        java.sql.Date startDate = handleDate("ngày bắt đầu (yyyy-MM-dd)");

        java.sql.Date endDate = null;
        if (!contractType.equals("Không xác định thời hạn")) {
            endDate = handleDate("ngày kết thúc (yyyy-MM-dd)");
            if (endDate.before(startDate)) {
                showError("Ngày kết thúc phải sau ngày bắt đầu");
                return;
            }
        }

        double baseSalary = handleDouleParam("lương cơ bản (VNĐ)");
        if (baseSalary <= 0) {
            showError("Lương cơ bản phải lớn hơn 0");
            return;
        }

        double allowance = handleDouleParam("phụ cấp cố định (VNĐ, nhập 0 nếu không có)");

        String position = handleParam("chức vụ");

        String notes = handleParam("ghi chú (Enter để bỏ qua)");

        System.out.println("\n========== XÁC NHẬN THÔNG TIN HỢP ĐỒNG ==========");
        System.out.println("Mã hợp đồng    : " + contractCode.trim());
        System.out.println("Mã nhân viên   : " + employeeCodeInput.trim());
        System.out.println("Loại hợp đồng  : " + contractType);
        System.out.println("Ngày bắt đầu   : " + startDate);
        System.out.println("Ngày kết thúc  : " + (endDate == null ? "Không xác định" : endDate));
        System.out.println("Lương cơ bản   : " + String.format("%,.0f", baseSalary) + " VNĐ");
        System.out.println("Phụ cấp        : " + String.format("%,.0f", allowance) + " VNĐ");
        System.out.println("Chức vụ        : " + position);
        System.out.println("Ghi chú        : " + (notes == null || notes.isEmpty() ? "(không có)" : notes));
        System.out.println("===================================================");

        String confirm = handleParam("Xác nhận tạo hợp đồng? (Y/N)");
        if (!confirm.trim().equalsIgnoreCase("Y")) {
            showMessage("Đã hủy tạo hợp đồng.");
            return;
        }

        controller.createContract(
                contractCode.trim(),
                employeeCodeInput.trim(),
                contractType,
                startDate,
                endDate,
                baseSalary,
                allowance,
                position.trim(),
                notes == null ? "" : notes.trim()
        );
    }
}
