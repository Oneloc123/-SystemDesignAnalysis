package dao;

import model.calcSalary.Parameter;
import model.calcSalary.TaxBracket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParameterDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public boolean save(Parameter parameter) {
        String sql = "INSERT INTO salary_parameters (social_insurance_rate, health_insurance_rate, " +
                "unemployment_insurance_rate, personal_deduction, dependent_deduction, " +
                "overtime_rate, standard_working_days) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, parameter.getSocialInsuranceRate());
            stmt.setDouble(2, parameter.getHealthInsuranceRate());
            stmt.setDouble(3, parameter.getUnemploymentInsuranceRate());
            stmt.setDouble(4, parameter.getPersonalDeduction());
            stmt.setDouble(5, parameter.getDependentDeduction());
            stmt.setDouble(6, parameter.getOvertimeRate());
            stmt.setInt(7, parameter.getStandardWorkingDays());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    long paramId = rs.getLong(1);
                    saveTaxBrackets(paramId, parameter.getTaxBracket());
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Parameter parameter, long paramId) {
        String sql = "UPDATE salary_parameters SET social_insurance_rate=?, health_insurance_rate=?, " +
                "unemployment_insurance_rate=?, personal_deduction=?, dependent_deduction=?, " +
                "overtime_rate=?, standard_working_days=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, parameter.getSocialInsuranceRate());
            stmt.setDouble(2, parameter.getHealthInsuranceRate());
            stmt.setDouble(3, parameter.getUnemploymentInsuranceRate());
            stmt.setDouble(4, parameter.getPersonalDeduction());
            stmt.setDouble(5, parameter.getDependentDeduction());
            stmt.setDouble(6, parameter.getOvertimeRate());
            stmt.setInt(7, parameter.getStandardWorkingDays());
            stmt.setLong(8, paramId);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                // Delete old tax brackets and re-insert
                deleteTaxBrackets(paramId);
                saveTaxBrackets(paramId, parameter.getTaxBracket());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Parameter load() {
        String sql = "SELECT * FROM salary_parameters ORDER BY id DESC LIMIT 1";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Parameter param = new Parameter();
                long paramId = rs.getLong("id");
                param.setSocialInsuranceRate(rs.getDouble("social_insurance_rate"));
                param.setHealthInsuranceRate(rs.getDouble("health_insurance_rate"));
                param.setUnemploymentInsuranceRate(rs.getDouble("unemployment_insurance_rate"));
                param.setPersonalDeduction(rs.getDouble("personal_deduction"));
                param.setDependentDeduction(rs.getDouble("dependent_deduction"));
                param.setOvertimeRate(rs.getDouble("overtime_rate"));
                param.setStandardWorkingDays(rs.getInt("standard_working_days"));
                param.setTaxBracket(loadTaxBrackets(paramId));
                return param;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveTaxBrackets(long paramId, List<TaxBracket> brackets) {
        String sql = "INSERT INTO tax_brackets (param_id, min_income, max_income, tax_rate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (TaxBracket b : brackets) {
                stmt.setLong(1, paramId);
                stmt.setDouble(2, b.getMinIncome());
                stmt.setDouble(3, b.getMaxIncome());
                stmt.setDouble(4, b.getTaxRate());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTaxBrackets(long paramId) {
        String sql = "DELETE FROM tax_brackets WHERE param_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, paramId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<TaxBracket> loadTaxBrackets(long paramId) {
        List<TaxBracket> list = new ArrayList<>();
        String sql = "SELECT * FROM tax_brackets WHERE param_id=? ORDER BY id ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, paramId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TaxBracket b = new TaxBracket();
                b.setId(rs.getLong("id"));
                b.setMinIncome(rs.getDouble("min_income"));
                b.setMaxIncome(rs.getDouble("max_income"));
                b.setTaxRate(rs.getDouble("tax_rate"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
