package controller.contract;

import dao.EmployeeDAO;
import model.Contract;
import model.hr.Employee;
import view.contract.ViewContractView;

import java.util.List;

public class ViewContractController {
    private ViewContractView view;

    public ViewContractController() {
        this.view = new ViewContractView(this);
    }

    public void show() throws Exception {
        view.show();
    }

    public List<Contract> getAllContracts() {
        return Contract.findAll();
    }

    public List<Contract> searchContracts(String keyword, String statusFilter, String typeFilter) {
        return Contract.search(keyword, statusFilter, typeFilter);
    }

    public Contract getContractById(int id) {
        return Contract.findById(id);
    }

    public List<Contract> getContractsByEmployeeCode(String employeeCode) {
        EmployeeDAO empDAO = new EmployeeDAO();
        Employee emp = empDAO.findByEmployeeCode(employeeCode);
        if (emp == null) return List.of();
        return Contract.findByEmployee(emp.getUserId());
    }

    public boolean cancelContract(int contractId) {
        Contract c = Contract.findById(contractId);
        if (c == null) return false;
        c.setStatus("Đã hủy");
        return c.delete();
    }


    public void navigateToRenew() throws Exception {
        RenewContractController rcc = new RenewContractController();
        rcc.show();
    }
}