package view.contract;

import controller.MainController;
import controller.contract.ViewContractController;
import enumModel.AddressEnum;
import model.Contract;
import view.View;

import java.util.List;

public class ViewContractView extends View {
    private ViewContractController controller;
    private AddressEnum address = AddressEnum.ContractList;

    public ViewContractView(ViewContractController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        MainController.addresses.add(address);
        try {
            loop:
            while (true) {
                printWelcome("DANH SÁCH HỢP ĐỒNG");
                System.out.println("0. Quay lại");
                System.out.println("1. Xem tất cả hợp đồng");
                System.out.println("2. Tìm kiếm hợp đồng");
                System.out.println("3. Xem hợp đồng theo nhân viên");
                System.out.println("4. Xem chi tiết hợp đồng");
                System.out.println("5. Hủy hợp đồng");
                System.out.println("------------------------");
                printAddress();
                handleInput();

                switch (question) {
                    case "0": break loop;
                    case "1": showAllContracts();          break;
                    case "2": searchContracts();           break;
                    case "3": showContractsByEmployee();   break;
                    case "4": showContractDetail();        break;
                    case "5": cancelContract();            break;
                    case "6": renewContract();             break;
                    default:  showError("Lệnh không hợp lệ");
                }
            }
        } finally {
            MainController.addresses.remove(address);
        }
    }

    private void showAllContracts() throws Exception {
        List<Contract> list = controller.getAllContracts();
        printContractTable(list);
    }

    private void searchContracts() throws Exception {
        String keyword = handleParam("từ khóa tìm kiếm (tên/mã nhân viên/mã hợp đồng)");

        System.out.println("Lọc theo trạng thái: 0.Tất cả  1.Hiệu lực  2.Hết hạn  3.Đã hủy");
        String statusChoice = handleParam("trạng thái (0/1/2/3)");
        String statusFilter = "Tất cả";
        switch (statusChoice.trim()) {
            case "1": statusFilter = "Hiệu lực"; break;
            case "2": statusFilter = "Hết hạn";  break;
            case "3": statusFilter = "Đã hủy";   break;
        }

        System.out.println("Lọc theo loại: 0.Tất cả  1.Thử việc  2.Xác định thời hạn  3.Không xác định thời hạn");
        String typeChoice = handleParam("loại (0/1/2/3)");
        String typeFilter = "Tất cả";
        switch (typeChoice.trim()) {
            case "1": typeFilter = "Thử việc"; break;
            case "2": typeFilter = "Xác định thời hạn"; break;
            case "3": typeFilter = "Không xác định thời hạn"; break;
        }

        List<Contract> list = controller.searchContracts(keyword.trim(), statusFilter, typeFilter);
        printContractTable(list);
    }

    private void showContractsByEmployee() throws Exception {
        String employeeCode = handleParam("mã nhân viên");
        List<Contract> list = controller.getContractsByEmployeeCode(employeeCode.trim());
        if (list.isEmpty()) {
            showMessage("Không tìm thấy hợp đồng nào cho nhân viên: " + employeeCode);
        } else {
            printContractTable(list);
        }
    }

    private void showContractDetail() throws Exception {
        String idInput = handleParam("mã số hợp đồng (contract_id)");
        try {
            int id = Integer.parseInt(idInput.trim());
            Contract c = controller.getContractById(id);
            if (c == null) showError("Không tìm thấy hợp đồng với ID: " + id);
            else printContractDetail(c);
        } catch (NumberFormatException e) {
            showError("ID hợp đồng phải là số nguyên");
        }
    }

    private void cancelContract() throws Exception {
        String idInput = handleParam("mã số hợp đồng cần hủy (contract_id)");
        try {
            int id = Integer.parseInt(idInput.trim());
            Contract c = controller.getContractById(id);
            if (c == null) {
                showError("Không tìm thấy hợp đồng với ID: " + id);
                return;
            }
            if ("Đã hủy".equals(c.getStatus())) {
                showError("Hợp đồng này đã bị hủy trước đó");
                return;
            }
            System.out.println("Hợp đồng: " + c.getContractCode() + " | NV: " + c.getEmployeeName());
            String confirm = handleParam("Xác nhận hủy hợp đồng này? (Y/N)");
            if (confirm.trim().equalsIgnoreCase("Y")) {
                boolean ok = controller.cancelContract(id);
                if (ok) showMessage("✔ Đã hủy hợp đồng thành công.");
                else    showError("Không thể hủy hợp đồng. Vui lòng thử lại.");
            } else {
                showMessage("Đã bỏ qua thao tác hủy.");
            }
        } catch (NumberFormatException e) {
            showError("ID hợp đồng phải là số nguyên");
        }
    }

    private void printContractTable(List<Contract> list) {
        if (list == null || list.isEmpty()) {
            showMessage("Không có hợp đồng nào.");
            return;
        }
        System.out.println("\n" + "─".repeat(105));
        System.out.printf("%-5s %-14s %-20s %-24s %-12s %-16s %-10s%n",
                "ID", "Mã HĐ", "Tên nhân viên", "Loại hợp đồng", "Bắt đầu", "Kết thúc", "Trạng thái");
        System.out.println("─".repeat(105));
        for (Contract c : list) {
            System.out.printf("%-5d %-14s %-20s %-24s %-12s %-16s %-10s%n",
                    c.getContractId(),
                    c.getContractCode(),
                    truncate(c.getEmployeeName(), 20),
                    c.getContractType(),
                    c.getStartDate(),
                    c.getEndDate() == null ? "Không xác định" : c.getEndDate().toString(),
                    c.getStatus());
        }
        System.out.println("─".repeat(105));
        System.out.println("Tổng: " + list.size() + " hợp đồng");
    }

    private void printContractDetail(Contract c) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║       CHI TIẾT HỢP ĐỒNG LAO ĐỘNG        ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Mã hợp đồng    : " + c.getContractCode());
        System.out.println("  ID hợp đồng    : " + c.getContractId());
        System.out.println("─────────────────────────────────────────────");
        System.out.println("  THÔNG TIN NHÂN VIÊN");
        System.out.println("  Tên nhân viên  : " + c.getEmployeeName());
        System.out.println("  Mã nhân viên   : " + c.getEmployeeCode());
        System.out.println("  Chức vụ        : " + c.getPosition());
        System.out.println("  Phòng ban      : " + (c.getDepartmentName() != null ? c.getDepartmentName() : "Chưa xác định"));
        System.out.println("─────────────────────────────────────────────");
        System.out.println("  THÔNG TIN HỢP ĐỒNG");
        System.out.println("  Loại hợp đồng  : " + c.getContractType());
        System.out.println("  Trạng thái     : " + c.getStatus());
        System.out.println("  Ngày bắt đầu   : " + c.getStartDate());
        System.out.println("  Ngày kết thúc  : " + c.getDisplayEndDate());
        System.out.println("─────────────────────────────────────────────");
        System.out.println("  THÔNG TIN LƯƠNG");
        System.out.println("  Lương cơ bản   : " + String.format("%,.0f", c.getBaseSalary()) + " VNĐ");
        System.out.println("  Phụ cấp        : " + String.format("%,.0f", c.getAllowance()) + " VNĐ");
        System.out.println("  Tổng thu nhập  : " + String.format("%,.0f", c.getBaseSalary() + c.getAllowance()) + " VNĐ");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("  Người tạo      : " + (c.getCreatedBy() != null ? c.getCreatedBy() : "N/A"));
        System.out.println("  Ngày tạo       : " + c.getCreatedDate());
        System.out.println("  Ghi chú        : " + (c.getNotes() != null && !c.getNotes().isEmpty() ? c.getNotes() : "(không có)"));
        System.out.println("═════════════════════════════════════════════");
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}