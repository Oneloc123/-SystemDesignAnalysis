package data;

import model.Employee;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static List<Employee> employees = new ArrayList<>();

    static {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setEmployeeCode("NV001");
        emp.setFullName("Nguyễn Văn Lộc");
        emp.setEmail("loc@company.com");
        emp.setPhone("0901111111");
        emp.setIdCard("001123456789");
        emp.setDateOfBirth("15/03/1990");
        emp.setGender("Nam");
        emp.setAddress("Số 1, Đường Láng, Hà Nội");
        emp.setHometown("Hà Nội");
        emp.setBaseSalary(15000000.0);
        emp.setFixedAllowance(2000000.0);
        emp.setBankAccount("0123456789");
        emp.setBankName("Vietcombank");
        emp.setBankAccountHolder("Nguyễn Văn Lộc");
        emp.setTaxCode("TN0001");
        emp.setSocialInsuranceNumber("BHXH0001");
        emp.setPosition("Nhân viên");
        emp.setDepartmentName("Phòng Kỹ Thuật");
        emp.setStartDate("01/01/2020");
        emp.setContractType("Chính thức");
        emp.setStatus("Đang làm");
        emp.setQualification("Đại học");
        emp.setMajor("Công nghệ thông tin");
        emp.setExperience("8 năm");
        employees.add(emp);

        emp = new Employee();
        emp.setId(2L);
        emp.setEmployeeCode("NV002");
        emp.setFullName("Trần Thị Ánh");
        emp.setEmail("anh@company.com");
        emp.setPhone("0902222222");
        emp.setIdCard("002987654321");
        emp.setDateOfBirth("20/07/1995");
        emp.setGender("Nữ");
        emp.setAddress("Số 2, Nguyễn Huệ, HCM");
        emp.setHometown("TP Hồ Chí Minh");
        emp.setBaseSalary(12000000.0);
        emp.setFixedAllowance(1500000.0);
        emp.setBankAccount("9876543210");
        emp.setBankName("Techcombank");
        emp.setBankAccountHolder("Trần Thị Ánh");
        emp.setTaxCode("TN0002");
        emp.setSocialInsuranceNumber("BHXH0002");
        emp.setPosition("Nhân viên");
        emp.setDepartmentName("Phòng Nhân Sự");
        emp.setStartDate("01/03/2021");
        emp.setContractType("Chính thức");
        emp.setStatus("Đang làm");
        emp.setQualification("Đại học");
        emp.setMajor("Quản trị nhân sự");
        emp.setExperience("5 năm");
        employees.add(emp);

        emp = new Employee();
        emp.setId(3L);
        emp.setEmployeeCode("NV003");
        emp.setFullName("Lê Đình Cương");
        emp.setEmail("cuong@company.com");
        emp.setPhone("0903333333");
        emp.setIdCard(null);
        emp.setDateOfBirth("10/01/1992");
        emp.setGender("Nam");
        emp.setAddress("Đà Nẵng");
        emp.setHometown(null);
        emp.setBaseSalary(18000000.0);
        emp.setFixedAllowance(3000000.0);
        emp.setBankAccount(null);
        emp.setBankName(null);
        emp.setBankAccountHolder(null);
        emp.setTaxCode("TN0003");
        emp.setSocialInsuranceNumber(null);
        emp.setPosition("Trưởng phòng");
        emp.setDepartmentName("Phòng Kỹ Thuật");
        emp.setStartDate("01/06/2019");
        emp.setContractType("Chính thức");
        emp.setStatus("Đang làm");
        emp.setQualification("Thạc sĩ");
        emp.setMajor("Khoa học máy tính");
        emp.setExperience("10 năm");
        employees.add(emp);
    }

    public static Employee findById(Long id) {
        for (Employee e : employees) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
}
