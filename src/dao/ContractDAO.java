package dao;

import model.Contract;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    public boolean save(Contract c) {
        String sql = "INSERT INTO contracts (employee_id, contract_code, contract_type, start_date, end_date, " +
                "base_salary, allowance, position, department_id, status, created_by, created_date, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, c.getEmployeeId());
            stmt.setString(2, c.getContractCode());
            stmt.setString(3, c.getContractType());
            stmt.setDate(4, c.getStartDate());
            if (c.getEndDate() != null) stmt.setDate(5, c.getEndDate());
            else stmt.setNull(5, Types.DATE);
            stmt.setDouble(6, c.getBaseSalary());
            stmt.setDouble(7, c.getAllowance());
            stmt.setString(8, c.getPosition());
            stmt.setInt(9, c.getDepartmentId());
            stmt.setString(10, c.getStatus());
            stmt.setString(11, c.getCreatedBy());
            stmt.setString(12, c.getNotes());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) c.setContractId(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean update(Contract c) {
        String sql = "UPDATE contracts SET contract_type=?, start_date=?, end_date=?, base_salary=?, " +
                "allowance=?, position=?, department_id=?, status=?, notes=? WHERE contract_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, c.getContractType());
            stmt.setDate(2, c.getStartDate());
            if (c.getEndDate() != null) stmt.setDate(3, c.getEndDate());
            else stmt.setNull(3, Types.DATE);
            stmt.setDouble(4, c.getBaseSalary());
            stmt.setDouble(5, c.getAllowance());
            stmt.setString(6, c.getPosition());
            stmt.setInt(7, c.getDepartmentId());
            stmt.setString(8, c.getStatus());
            stmt.setString(9, c.getNotes());
            stmt.setInt(10, c.getContractId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int contractId) {
        String sql = "UPDATE contracts SET status='Đã hủy' WHERE contract_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, contractId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Contract findById(int id) {
        String sql = "SELECT c.*, u.full_name AS employee_name, e.employee_code, d.department_name " +
                "FROM contracts c " +
                "JOIN employees e ON c.employee_id = e.employee_id " +
                "JOIN users u ON e.employee_id = u.user_id " +
                "LEFT JOIN departments d ON c.department_id = d.department_id " +
                "WHERE c.contract_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Contract> findAll() {
        return query("SELECT c.*, u.full_name AS employee_name, e.employee_code, d.department_name " +
                "FROM contracts c " +
                "JOIN employees e ON c.employee_id = e.employee_id " +
                "JOIN users u ON e.employee_id = u.user_id " +
                "LEFT JOIN departments d ON c.department_id = d.department_id " +
                "ORDER BY c.created_date DESC", null);
    }


    public List<Contract> findByEmployee(int employeeId) {
        String sql = "SELECT c.*, u.full_name AS employee_name, e.employee_code, d.department_name " +
                "FROM contracts c " +
                "JOIN employees e ON c.employee_id = e.employee_id " +
                "JOIN users u ON e.employee_id = u.user_id " +
                "LEFT JOIN departments d ON c.department_id = d.department_id " +
                "WHERE c.employee_id = ? ORDER BY c.start_date DESC";
        return query(sql, new Object[]{employeeId});
    }

    public List<Contract> search(String keyword, String statusFilter, String typeFilter) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, u.full_name AS employee_name, e.employee_code, d.department_name " +
                        "FROM contracts c " +
                        "JOIN employees e ON c.employee_id = e.employee_id " +
                        "JOIN users u ON e.employee_id = u.user_id " +
                        "LEFT JOIN departments d ON c.department_id = d.department_id WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (u.full_name LIKE ? OR c.contract_code LIKE ? OR e.employee_code LIKE ?)");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (statusFilter != null && !statusFilter.equals("Tất cả")) {
            sql.append(" AND c.status = ?");
            params.add(statusFilter);
        }
        if (typeFilter != null && !typeFilter.equals("Tất cả")) {
            sql.append(" AND c.contract_type = ?");
            params.add(typeFilter);
        }
        sql.append(" ORDER BY c.created_date DESC");

        return query(sql.toString(), params.toArray());
    }



    public Contract findByContractCode(String contractCode) {
        String sql = "SELECT c.*, u.full_name AS employee_name, e.employee_code, d.department_name " +
                "FROM contracts c " +
                "JOIN employees e ON c.employee_id = e.employee_id " +
                "JOIN users u ON e.employee_id = u.user_id " +
                "LEFT JOIN departments d ON c.department_id = d.department_id " +
                "WHERE c.contract_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, contractCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Contract> query(String sql, Object[] params) {
        List<Contract> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Contract mapRow(ResultSet rs) throws SQLException {
        Contract c = new Contract();
        c.setContractId(rs.getInt("contract_id"));
        c.setEmployeeId(rs.getInt("employee_id"));
        c.setEmployeeName(rs.getString("employee_name"));
        c.setEmployeeCode(rs.getString("employee_code"));
        c.setContractCode(rs.getString("contract_code"));
        c.setContractType(rs.getString("contract_type"));
        c.setStartDate(rs.getDate("start_date"));
        c.setEndDate(rs.getDate("end_date"));
        c.setBaseSalary(rs.getDouble("base_salary"));
        c.setAllowance(rs.getDouble("allowance"));
        c.setPosition(rs.getString("position"));
        c.setDepartmentId(rs.getInt("department_id"));
        c.setDepartmentName(rs.getString("department_name"));
        c.setStatus(rs.getString("status"));
        c.setCreatedBy(rs.getString("created_by"));
        c.setCreatedDate(rs.getDate("created_date"));
        c.setNotes(rs.getString("notes"));
        return c;
    }
}
