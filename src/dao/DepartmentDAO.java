package dao;

import model.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO implements DAO<Department> {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(Department dept) {
        String sql = "INSERT INTO departments (name, code, manager_name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dept.getName());
            stmt.setString(2, dept.getCode());
            stmt.setString(3, dept.getManagerName());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) dept.setDepartmentId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Department dept) {
        String sql = "UPDATE departments SET name=?, code=?, manager_name=? WHERE department_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dept.getName());
            stmt.setString(2, dept.getCode());
            stmt.setString(3, dept.getManagerName());
            stmt.setInt(4, dept.getDepartmentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM departments WHERE department_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Department findById(int id) {
        String sql = "SELECT * FROM departments WHERE department_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractDepartment(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(extractDepartment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Department extractDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setDepartmentId(rs.getInt("department_id"));
        dept.setName(rs.getString("name"));
        dept.setCode(rs.getString("code"));
        dept.setManagerName(rs.getString("manager_name"));
        return dept;
    }
}
