package controller.contract;

import controller.MainController;
import dao.ContractDAO;
import dao.EmployeeDAO;
import model.Contract;
import model.hr.Employee;
import view.contract.CreateContractView;

import java.sql.Date;

public class CreateContractController {
    private CreateContractView view;

    public CreateContractController() {
        this.view = new CreateContractView(this);
    }

    public void show() throws Exception {
        view.show();
    }

    public void createContract(String contractCode, String employeeCode,
                               String contractType, Date startDate, Date endDate,
                               double baseSalary, double allowance,
                               String position, String notes) {
        EmployeeDAO empDAO = new EmployeeDAO();
        Employee employee = empDAO.findByEmployeeCode(employeeCode);
        if (employee == null) {
            view.showError("Không tìm thấy nhân viên với mã: " + employeeCode);
            return;
        }

        if (isContractCodeExists(contractCode)) {
            view.showError("Mã hợp đồng '" + contractCode + "' đã tồn tại. Vui lòng dùng mã khác.");
            return;
        }

        Contract contract = new Contract();
        contract.setContractCode(contractCode);
        contract.setEmployeeId(employee.getUserId());
        contract.setContractType(contractType);
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setBaseSalary(baseSalary);
        contract.setAllowance(allowance);
        contract.setPosition(position.isEmpty() ? employee.getPosition() : position);
        contract.setDepartmentId(employee.getDepartmentId() != null ? employee.getDepartmentId() : 0);
        contract.setStatus("Hiệu lực");
        contract.setNotes(notes);
        contract.setCreatedBy(
                MainController.currentUser != null ? MainController.currentUser.getUsername() : "system"
        );

        boolean ok = contract.save();
        if (ok) {
            view.showMessage("\n✔ Tạo hợp đồng thành công!");
            view.showMessage("  Mã hợp đồng : " + contractCode);
            view.showMessage("  Nhân viên   : " + employee.getFullName());
            view.showMessage("  ID hợp đồng : " + contract.getContractId());
        } else {
            view.showError("Không thể lưu hợp đồng. Vui lòng kiểm tra lại dữ liệu hoặc kết nối DB.");
        }
    }

    private boolean isContractCodeExists(String code) {
        ContractDAO dao = new ContractDAO();
        return dao.findByContractCode(code) != null;
    }
}
