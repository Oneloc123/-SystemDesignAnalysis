package view.contract;

import controller.MainController;
import controller.contract.RenewContractController;
import enumModel.AddressEnum;
import model.Contract;
import view.View;

import java.sql.Date;

public class RenewContractView extends View {

    private RenewContractController controller;
    private AddressEnum address = AddressEnum.RenewContract;

    public RenewContractView(RenewContractController controller) {
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
        printWelcome("GIA HẠN HỢP ĐỒNG");

        String idInput = handleParam("mã số hợp đồng cần gia hạn (contract_id)");
        int oldContractId;
        try {
            oldContractId = Integer.parseInt(idInput.trim());
        } catch (NumberFormatException e) {
            showError("ID hợp đồng phải là số nguyên.");
            return;
        }

        Contract old = controller.getContractById(oldContractId);
        if (old == null) {
            showError("Không tìm thấy hợp đồng với ID: " + oldContractId);
            return;
        }
        if ("Đã hủy".equals(old.getStatus())) {
            showError("Không thể gia hạn hợp đồng đã bị hủy.");
            return;
        }

        printCurrentContract(old);

        String newContractCode = handleParam("mã hợp đồng MỚI (VD: HD2025001)");
        if (newContractCode == null || newContractCode.trim().isEmpty()) {
            showError("Mã hợp đồng không được để trống.");
            return;
        }

        // Loại hợp đồng mới
        System.out.println("Loại hợp đồng mới:");
        System.out.println("  1. Thử việc");
        System.out.println("  2. Xác định thời hạn");
        System.out.println("  3. Không xác định thời hạn");
        System.out.println("  (Enter để giữ nguyên: " + old.getContractType() + ")");
        String typeChoice = handleParam("loại hợp đồng (1/2/3 hoặc Enter)");
        String newContractType;
        switch (typeChoice.trim()) {
            case "1":  newContractType = "Thử việc"; break;
            case "2":  newContractType = "Xác định thời hạn"; break;
            case "3":  newContractType = "Không xác định thời hạn"; break;
            case "":   newContractType = old.getContractType(); break;
            default:
                showError("Lựa chọn không hợp lệ.");
                return;
        }

        System.out.println("Ngày bắt đầu hợp đồng mới (yyyy-MM-dd):");
        Date newStartDate = handleDate("ngày bắt đầu (yyyy-MM-dd)");

        Date newEndDate = null;
        if (!"Không xác định thời hạn".equals(newContractType)) {
            newEndDate = handleDate("ngày kết thúc (yyyy-MM-dd)");
            if (!newEndDate.after(newStartDate)) {
                showError("Ngày kết thúc phải sau ngày bắt đầu.");
                return;
            }
        }

        System.out.println("Lương cơ bản hiện tại: " + String.format("%,.0f", old.getBaseSalary()) + " VNĐ");
        System.out.println("(Nhập 0 để giữ nguyên)");
        double newBaseSalary = handleDouleParam("lương cơ bản mới (VNĐ)");
        if (newBaseSalary <= 0) newBaseSalary = old.getBaseSalary();

        System.out.println("Phụ cấp hiện tại: " + String.format("%,.0f", old.getAllowance()) + " VNĐ");
        System.out.println("(Nhập -1 để giữ nguyên)");
        double newAllowance = handleDouleParam("phụ cấp mới (VNĐ, nhập -1 để giữ nguyên)");
        if (newAllowance < 0) newAllowance = old.getAllowance();

        String notes = handleParam("ghi chú (Enter để bỏ qua)");

        System.out.println("\n========== XÁC NHẬN GIA HẠN HỢP ĐỒNG ==========");
        System.out.println("Hợp đồng cũ    : " + old.getContractCode() + " → sẽ chuyển sang [Hết hạn]");
        System.out.println("-------------------------------------------------");
        System.out.println("Mã HĐ mới      : " + newContractCode.trim());
        System.out.println("Nhân viên      : " + old.getEmployeeName() + " (" + old.getEmployeeCode() + ")");
        System.out.println("Loại HĐ mới    : " + newContractType);
        System.out.println("Ngày bắt đầu   : " + newStartDate);
        System.out.println("Ngày kết thúc  : " + (newEndDate == null ? "Không xác định" : newEndDate));
        System.out.println("Lương cơ bản   : " + String.format("%,.0f", newBaseSalary) + " VNĐ");
        System.out.println("Phụ cấp        : " + String.format("%,.0f", newAllowance) + " VNĐ");
        System.out.println("Ghi chú        : " + (notes == null || notes.isEmpty() ? "(không có)" : notes));
        System.out.println("=================================================");

        String confirm = handleParam("Xác nhận gia hạn hợp đồng? (Y/N)");
        if (!confirm.trim().equalsIgnoreCase("Y")) {
            showMessage("Đã hủy thao tác gia hạn.");
            return;
        }

        controller.renewContract(
                oldContractId,
                newContractCode.trim(),
                newStartDate,
                newEndDate,
                newContractType,
                newBaseSalary,
                newAllowance,
                notes == null ? "" : notes.trim()
        );
    }

    private void printCurrentContract(Contract c) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     THÔNG TIN HỢP ĐỒNG HIỆN TẠI         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Mã hợp đồng   : " + c.getContractCode());
        System.out.println("  Nhân viên     : " + c.getEmployeeName() + " (" + c.getEmployeeCode() + ")");
        System.out.println("  Loại hợp đồng : " + c.getContractType());
        System.out.println("  Trạng thái    : " + c.getStatus());
        System.out.println("  Ngày bắt đầu  : " + c.getStartDate());
        System.out.println("  Ngày kết thúc : " + c.getDisplayEndDate());
        System.out.println("  Lương cơ bản  : " + String.format("%,.0f", c.getBaseSalary()) + " VNĐ");
        System.out.println("  Phụ cấp       : " + String.format("%,.0f", c.getAllowance()) + " VNĐ");
        System.out.println("═══════════════════════════════════════════");
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }
}