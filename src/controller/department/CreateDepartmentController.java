package controller.department;

import controller.MainController;
import enumModel.AddressEnum;
import model.Department;
import view.department.CreateDepartmentView;

public class CreateDepartmentController {
    private CreateDepartmentView view;
    private AddressEnum address = AddressEnum.CreateDepartment;

    public CreateDepartmentController() {
        this.view = new CreateDepartmentView(this);
    }

    public void show() throws Exception {
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
    }

    public String createDepartment(String name, String code, String managerName) {

        if (name == null || name.trim().isEmpty()) {
            return "Tên phòng ban không được để trống.";
        }
        if (code == null || code.trim().isEmpty()) {
            return "Mã phòng ban không được để trống.";
        }

        for (Department d : Department.findAll()) {
            if (d.getCode().equalsIgnoreCase(code.trim())) {
                return "Mã phòng ban \"" + code.trim() + "\" đã tồn tại.";
            }
        }

        Department dept = new Department();
        dept.setName(name.trim());
        dept.setCode(code.trim().toUpperCase());
        dept.setManagerName(managerName == null ? "" : managerName.trim());

        boolean ok = dept.save();
        if (!ok) {
            return "Lỗi khi lưu vào cơ sở dữ liệu.";
        }
        return null;
    }
}
