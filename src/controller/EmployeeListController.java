package controller;

import controller.base.Controller;
import view.EmployeeListView;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListController extends Controller {
    EmployeeListView employeeListView;
    private int selectedDeptId;
    private String searchKeyword = "";
    private String filterStatus = "";


    public EmployeeListController() {
        this.employeeListView = new EmployeeListView(this);
        this.view = this.employeeListView;
    }

    @Override
    public void showOn() {
        employeeListView.showDeptSelection();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public String[][] getMockDepartments() {
        return new String[][]{
            {"1", "Phòng Kỹ Thuật", "KT"},
            {"2", "Phòng Nhân Sự", "NS"},
            {"3", "Phòng Kế Toán", "KT"},
        };
    }

    public String[][] getAllEmployees() {
        return new String[][]{
            {"NV001", "Nguyễn Văn Lộc", "Nhân viên", "0901111111", "loc@company.com", "1", "12", "Đang làm"},
            {"NV002", "Trần Thị Ánh", "Nhân viên", "0902222222", "anh@company.com", "2", "7", "Đang làm"},
            {"NV003", "Lê Đình Cương", "Trưởng phòng", "0903333333", "cuong@company.com", "1", "10", "Đang làm"},
            {"NV004", "Phạm Thị Hoa", "Kế toán", "0904444444", "hoa@company.com", "3", "6", "Đang làm"},
            {"NV005", "Hoàng Văn Nam", "Nhân viên", "0905555555", "nam@company.com", "2", "3", "Đã nghỉ"},
        };
    }

    public String[][] getEmployeesByDepartment(int deptId) {
        List<String[]> result = new ArrayList<>();
        for (String[] emp : getAllEmployees()) {
            if (Integer.parseInt(emp[5]) == deptId) {
                result.add(emp);
            }
        }
        return result.toArray(new String[0][0]);
    }

    public String[][] getFilteredEmployees() {
        String[][] deptEmps = getEmployeesByDepartment(selectedDeptId);
        List<String[]> result = new ArrayList<>();
        for (String[] emp : deptEmps) {
            if (!filterStatus.isEmpty() && !emp[7].equals(filterStatus)) continue;
            if (!searchKeyword.isEmpty()) {
                String kw = searchKeyword.toLowerCase();
                if (!emp[0].toLowerCase().contains(kw) &&
                    !emp[1].toLowerCase().contains(kw) &&
                    !emp[4].toLowerCase().contains(kw)) continue;
            }
            result.add(emp);
        }
        return result.toArray(new String[0][0]);
    }

    public String getEmployeeDetail(int index) {
        String[][] list = getFilteredEmployees();
        if (index < 1 || index > list.length) return "Không hợp lệ.";
        String[] emp = list[index - 1];
        return "Họ tên: " + emp[1] + "\nMã NV: " + emp[0] + "\nChức danh: " + emp[2]
             + "\nSĐT: " + emp[3] + "\nEmail: " + emp[4] + "\nSố ngày phép còn lại: " + emp[6] + " ngày"
             + "\nTrạng thái: " + emp[7] + "\n";
    }

    public boolean selectDepartment(int id) {
        for (String[] d : getMockDepartments()) {
            if (Integer.parseInt(d[0]) == id) {
                selectedDeptId = id;
                searchKeyword = "";
                filterStatus = "";
                return true;
            }
        }
        return false;
    }

    public void setSearchKeyword(String kw) { this.searchKeyword = kw; }
    public void setFilterStatus(String s) { this.filterStatus = s; }
    public int getSelectedDeptId() { return selectedDeptId; }
}
