package controller.contract;

import dao.ContractDAO;
import dao.EmployeeDAO;
import model.Contract;
import model.hr.Employee;
import view.contract.ViewContractView;

import java.util.List;

public class ViewContractController {
    private ViewContractView view;
    private ContractDAO contractDAO;
    private EmployeeDAO employeeDAO;

    public ViewContractController() {
        this.view = new ViewContractView(this);
        this.contractDAO = new ContractDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    public void show() throws Exception {
        view.show();
    }

    public List<Contract> getAllContracts() {
        return contractDAO.findAll();
    }

    public List<Contract> searchContracts(String keyword, String statusFilter, String typeFilter) {
        return contractDAO.search(keyword, statusFilter, typeFilter);
    }

    public Contract getContractById(int id) {
        return contractDAO.findById(id);
    }


    public List<Contract> getContractsByEmployeeCode(String employeeCode) {
        Employee emp = employeeDAO.findByEmployeeCode(employeeCode);
        if (emp == null) return List.of();
        return contractDAO.findByEmployee(emp.getUserId());
    }


    public boolean cancelContract(int contractId) {
        return contractDAO.delete(contractId);
    }
}
