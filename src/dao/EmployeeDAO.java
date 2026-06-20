package dao;

import enumModel.RoleEnum;
import model.User;
import model.hr.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements DAO<Employee> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    private UserDAO userDAO = new UserDAO();

    @Override
    public boolean save(Employee emp) {
        if (!userDAO.save(emp)) return false;

        String sql = "INSERT INTO employees (employee_id, employee_code, hometown, base_salary, fixed_allowance, " +
                "bank_account, bank_name, bank_holder, tax_code, social_insurance, position, department_id, " +
                "start_date, contract_type, status, qualification, major, experience) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, emp.getUserId());
            stmt.setString(2, emp.getEmployeeCode());
            stmt.setString(3, emp.getHometown());
            setDouble(stmt, 4, emp.getBaseSalary());
            setDouble(stmt, 5, emp.getFixedAllowance());
            stmt.setString(6, emp.getBankAccount());
            stmt.setString(7, emp.getBankName());
            stmt.setString(8, emp.getBankAccountHolder());
            stmt.setString(9, emp.getTaxCode());
            stmt.setString(10, emp.getSocialInsuranceNumber());
            stmt.setString(11, emp.getPosition());
            if (emp.getDepartmentId() != null) stmt.setInt(12, emp.getDepartmentId());
            else stmt.setNull(12, Types.INTEGER);
            stmt.setString(13, emp.getStartDate());
            stmt.setString(14, emp.getContractType());
            stmt.setString(15, emp.getEmployeeStatus());
            stmt.setString(16, emp.getQualification());
            stmt.setString(17, emp.getMajor());
            stmt.setString(18, emp.getExperience());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Employee emp) {
        userDAO.update(emp);
        String sql = "UPDATE employees SET employee_code=?, hometown=?, base_salary=?, fixed_allowance=?, " +
                "bank_account=?, bank_name=?, bank_holder=?, tax_code=?, social_insurance=?, position=?, " +
                "department_id=?, start_date=?, contract_type=?, status=?, qualification=?, major=?, experience=? " +
                "WHERE employee_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emp.getEmployeeCode());
            stmt.setString(2, emp.getHometown());
            setDouble(stmt, 3, emp.getBaseSalary());
            setDouble(stmt, 4, emp.getFixedAllowance());
            stmt.setString(5, emp.getBankAccount());
            stmt.setString(6, emp.getBankName());
            stmt.setString(7, emp.getBankAccountHolder());
            stmt.setString(8, emp.getTaxCode());
            stmt.setString(9, emp.getSocialInsuranceNumber());
            stmt.setString(10, emp.getPosition());
            if (emp.getDepartmentId() != null) stmt.setInt(11, emp.getDepartmentId());
            else stmt.setNull(11, Types.INTEGER);
            stmt.setString(12, emp.getStartDate());
            stmt.setString(13, emp.getContractType());
            stmt.setString(14, emp.getEmployeeStatus());
            stmt.setString(15, emp.getQualification());
            stmt.setString(16, emp.getMajor());
            stmt.setString(17, emp.getExperience());
            stmt.setInt(18, emp.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDAO.delete(id);
    }

    @Override
    public Employee findById(int id) {
        return findByUserId(id);
    }

    public Employee findByUserId(int userId) {
        String sql = "SELECT u.*, e.* FROM users u JOIN employees e ON u.user_id = e.employee_id WHERE u.user_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractEmployee(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT u.*, e.* FROM users u JOIN employees e ON u.user_id = e.employee_id";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractEmployee(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> findByDepartment(int deptId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT u.*, e.* FROM users u JOIN employees e ON u.user_id = e.employee_id WHERE e.department_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, deptId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractEmployee(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> search(String keyword, String statusFilter, int deptId) {
        List<Employee> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT u.*, e.* FROM users u JOIN employees e ON u.user_id = e.employee_id WHERE e.department_id=?");
        List<Object> params = new ArrayList<>();
        params.add(deptId);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (u.full_name LIKE ? OR e.employee_code LIKE ? OR u.email LIKE ?)");
            String kw = "%" + keyword + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND e.status=?");
            params.add(statusFilter);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) stmt.setString(i + 1, (String) params.get(i));
                else if (params.get(i) instanceof Integer) stmt.setInt(i + 1, (Integer) params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractEmployee(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setUserId(rs.getInt("user_id"));
        emp.setUsername(rs.getString("username"));
        emp.setPassword(rs.getString("password"));
        emp.setEmail(rs.getString("email"));
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            emp.setRole(RoleEnum.valueOf(roleStr));
        }
        emp.setFullName(rs.getString("full_name"));
        emp.setDateOfBirth(rs.getDate("date_of_birth"));
        emp.setGender(rs.getString("gender"));
        emp.setPhone(rs.getString("phone"));
        emp.setCitizenIdentificationCard(rs.getString("citizen_id"));
        emp.setAddress(rs.getString("address"));
        emp.setEmployeeCode(rs.getString("employee_code"));
        emp.setHometown(rs.getString("hometown"));
        emp.setBaseSalary(rs.getDouble("base_salary"));
        if (rs.wasNull()) emp.setBaseSalary(null);
        emp.setFixedAllowance(rs.getDouble("fixed_allowance"));
        if (rs.wasNull()) emp.setFixedAllowance(null);
        emp.setBankAccount(rs.getString("bank_account"));
        emp.setBankName(rs.getString("bank_name"));
        emp.setBankAccountHolder(rs.getString("bank_holder"));
        emp.setTaxCode(rs.getString("tax_code"));
        emp.setSocialInsuranceNumber(rs.getString("social_insurance"));
        emp.setPosition(rs.getString("position"));
        emp.setDepartmentId(rs.getInt("department_id"));
        if (rs.wasNull()) emp.setDepartmentId(null);
        emp.setStartDate(rs.getString("start_date"));
        emp.setContractType(rs.getString("contract_type"));
        emp.setEmployeeStatus(rs.getString("status"));
        emp.setQualification(rs.getString("qualification"));
        emp.setMajor(rs.getString("major"));
        emp.setExperience(rs.getString("experience"));
        return emp;
    }

    private void setDouble(PreparedStatement stmt, int index, Double value) throws SQLException {
        if (value != null) stmt.setDouble(index, value);
        else stmt.setNull(index, Types.DOUBLE);
    }
    public Employee findByEmployeeCode(String employeeCode) {
        String sql = "SELECT e.*, u.username, u.password, u.email, u.role, " +
                "u.full_name, u.date_of_birth, u.gender, u.phone, u.citizen_id, u.address " +
                "FROM employees e JOIN users u ON e.employee_id = u.user_id " +
                "WHERE e.employee_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employeeCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractEmployee(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
