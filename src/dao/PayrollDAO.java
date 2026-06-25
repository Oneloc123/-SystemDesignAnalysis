package dao;

import model.calcSalary.Payroll;
import model.calcSalary.PayrollDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public boolean save(Payroll payroll) {
        String sql = "INSERT INTO payrolls (month, year, total_gross, total_net, total_tax, total_insurance) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payroll.getMonth());
            stmt.setInt(2, payroll.getYear());
            stmt.setDouble(3, payroll.getTotalGross());
            stmt.setDouble(4, payroll.getTotalNet());
            stmt.setDouble(5, payroll.getTotalTax());
            stmt.setDouble(6, payroll.getTotalInsurance());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    long payrollId = rs.getLong(1);
                    payroll.setId(payrollId);
                    for (PayrollDetail d : payroll.getDetails()) {
                        saveDetail(d, payrollId);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveDetail(PayrollDetail detail, long payrollId) {
        String sql = "INSERT INTO payroll_details (payroll_id, employee_id, basic_salary, allowance, " +
                "actual_working_days, standard_working_days, overtime_hours, gross_salary, " +
                "social_insurance, health_insurance, unemployment_insurance, taxable_income, income_tax, net_salary) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, payrollId);
            stmt.setLong(2, detail.getEmployeeId());
            stmt.setDouble(3, detail.getBasicSalary());
            stmt.setDouble(4, detail.getAllowance());
            stmt.setInt(5, detail.getActualWorkingDays());
            stmt.setInt(6, detail.getStandardWorkingDays());
            stmt.setInt(7, detail.getOvertimeHours());
            stmt.setDouble(8, detail.getGrossSalary());
            stmt.setDouble(9, detail.getSocialInsurance());
            stmt.setDouble(10, detail.getHealthInsurance());
            stmt.setDouble(11, detail.getUnemploymentInsurance());
            stmt.setDouble(12, detail.getTaxableIncome());
            stmt.setDouble(13, detail.getIncomeTax());
            stmt.setDouble(14, detail.getNetSalary());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) detail.setId(rs.getLong(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Payroll findByMonth(int month, int year) {
        String sql = "SELECT * FROM payrolls WHERE month=? AND year=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Payroll payroll = extractPayroll(rs);
                payroll.setDetails(findDetailsByPayroll(payroll.getId()));
                return payroll;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Payroll> findAll() {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payrolls ORDER BY year DESC, month DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Payroll payroll = extractPayroll(rs);
                payroll.setDetails(findDetailsByPayroll(payroll.getId()));
                list.add(payroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<PayrollDetail> findDetailsByPayroll(long payrollId) {
        List<PayrollDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM payroll_details WHERE payroll_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, payrollId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractDetail(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Payroll extractPayroll(ResultSet rs) throws SQLException {
        Payroll payroll = new Payroll();
        payroll.setId(rs.getLong("id"));
        payroll.setMonth(rs.getInt("month"));
        payroll.setYear(rs.getInt("year"));
        payroll.setTotalGross(rs.getDouble("total_gross"));
        payroll.setTotalNet(rs.getDouble("total_net"));
        payroll.setTotalTax(rs.getDouble("total_tax"));
        payroll.setTotalInsurance(rs.getDouble("total_insurance"));
        return payroll;
    }

    private PayrollDetail extractDetail(ResultSet rs) throws SQLException {
        PayrollDetail d = new PayrollDetail();
        d.setId(rs.getLong("id"));
        d.setEmployeeId(rs.getLong("employee_id"));
        d.setBasicSalary(rs.getDouble("basic_salary"));
        d.setAllowance(rs.getDouble("allowance"));
        d.setActualWorkingDays(rs.getInt("actual_working_days"));
        d.setStandardWorkingDays(rs.getInt("standard_working_days"));
        d.setOvertimeHours(rs.getInt("overtime_hours"));
        d.setGrossSalary(rs.getDouble("gross_salary"));
        d.setSocialInsurance(rs.getDouble("social_insurance"));
        d.setHealthInsurance(rs.getDouble("health_insurance"));
        d.setUnemploymentInsurance(rs.getDouble("unemployment_insurance"));
        d.setTaxableIncome(rs.getDouble("taxable_income"));
        d.setIncomeTax(rs.getDouble("income_tax"));
        d.setNetSalary(rs.getDouble("net_salary"));
        return d;
    }
}
