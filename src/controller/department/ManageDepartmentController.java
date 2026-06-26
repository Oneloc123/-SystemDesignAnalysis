package controller.department;

import controller.MainController;
import enumModel.AddressEnum;
import model.Department;
import view.department.ManageDepartmentView;

import java.util.List;

public class ManageDepartmentController {
    private ManageDepartmentView view;
    private AddressEnum address = AddressEnum.ManageDepartment;

    public ManageDepartmentController() {
        this.view = new ManageDepartmentView(this);
    }

    public void show() throws Exception {
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
    }

    public List<Department> getAllDepartments() {
        return Department.findAll();
    }

    public Department getDepartmentById(int id) {
        return Department.findById(id);
    }

    public String updateDepartment(int id, String name, String code, String managerName) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên phòng ban không được để trống.";
        }
        if (code == null || code.trim().isEmpty()) {
            return "Mã phòng ban không được để trống.";
        }

        for (Department d : Department.findAll()) {
            if (d.getCode().equalsIgnoreCase(code.trim()) && d.getDepartmentId() != id) {
                return "Mã phòng ban \"" + code.trim() + "\" đã tồn tại ở phòng ban khác.";
            }
        }

        Department dept = Department.findById(id);
        if (dept == null) {
            return "Không tìm thấy phòng ban với ID = " + id;
        }
        dept.setName(name.trim());
        dept.setCode(code.trim().toUpperCase());
        dept.setManagerName(managerName == null ? "" : managerName.trim());

        boolean ok = dept.update();
        if (!ok) {
            return "Lỗi khi cập nhật cơ sở dữ liệu.";
        }
        return null; // success
    }

    public String deleteDepartment(int id) {
        Department dept = Department.findById(id);
        if (dept == null) {
            return "Không tìm thấy phòng ban với ID = " + id;
        }

        if (dept.getEmployees() != null && !dept.getEmployees().isEmpty()) {
            return "Không thể xóa: phòng ban \"" + dept.getName() + "\" vẫn còn nhân viên.";
        }
        boolean ok = dept.delete();
        if (!ok) {
            return "Lỗi khi xóa khỏi cơ sở dữ liệu.";
        }
        return null;
    }
}
