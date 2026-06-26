package controller.contract;

import controller.MainController;
import dao.ContractDAO;
import model.Contract;
import view.contract.RenewContractView;

import java.sql.Date;


public class RenewContractController {

    private RenewContractView view;

    public RenewContractController() {
        this.view = new RenewContractView(this);
    }

    public void show() throws Exception {
        view.show();
    }

    public Contract getContractById(int id) {
        return Contract.findById(id);
    }

    public boolean renewContract(int oldContractId,
                                 String newContractCode,
                                 Date newStartDate,
                                 Date newEndDate,
                                 String newContractType,
                                 double newBaseSalary,
                                 double newAllowance,
                                 String notes) {

        Contract oldContract = Contract.findById(oldContractId);
        if (oldContract == null) {
            view.showError("Không tìm thấy hợp đồng với ID: " + oldContractId);
            return false;
        }

        if ("Đã hủy".equals(oldContract.getStatus())) {
            view.showError("Không thể gia hạn hợp đồng đã bị hủy.");
            return false;
        }

        if (isContractCodeExists(newContractCode)) {
            view.showError("Mã hợp đồng '" + newContractCode + "' đã tồn tại. Vui lòng dùng mã khác.");
            return false;
        }

        if (newEndDate != null && !newEndDate.after(newStartDate)) {
            view.showError("Ngày kết thúc phải sau ngày bắt đầu.");
            return false;
        }

        oldContract.setStatus("Hết hạn");
        boolean expireOk = oldContract.update();
        if (!expireOk) {
            view.showError("Không thể cập nhật trạng thái hợp đồng cũ. Vui lòng kiểm tra kết nối DB.");
            return false;
        }

        Contract newContract = new Contract();
        newContract.setContractCode(newContractCode);
        newContract.setEmployeeId(oldContract.getEmployeeId());
        newContract.setContractType(newContractType);
        newContract.setStartDate(newStartDate);
        newContract.setEndDate(newEndDate);
        newContract.setBaseSalary(newBaseSalary);
        newContract.setAllowance(newAllowance);
        newContract.setPosition(oldContract.getPosition());
        newContract.setDepartmentId(oldContract.getDepartmentId());
        newContract.setStatus("Hiệu lực");
        newContract.setNotes(notes == null ? "" : notes);
        newContract.setCreatedBy(
                MainController.currentUser != null
                        ? MainController.currentUser.getUsername()
                        : "system"
        );

        boolean saveOk = newContract.save();
        if (!saveOk) {
            oldContract.setStatus("Hiệu lực");
            oldContract.update();
            view.showError("Không thể lưu hợp đồng mới. Vui lòng kiểm tra lại dữ liệu.");
            return false;
        }

        view.showMessage("\n✔ Gia hạn hợp đồng thành công!");
        view.showMessage("  Hợp đồng cũ  : " + oldContract.getContractCode() + "  → Hết hạn");
        view.showMessage("  Hợp đồng mới : " + newContractCode + "  (ID: " + newContract.getContractId() + ")");
        return true;
    }

    private boolean isContractCodeExists(String code) {
        ContractDAO dao = new ContractDAO();
        return dao.findByContractCode(code) != null;
    }
}