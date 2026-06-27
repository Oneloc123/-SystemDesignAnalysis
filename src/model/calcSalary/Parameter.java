package model.calcSalary;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private double socialInsuranceRate;
    private double healthInsuranceRate;
    private double unemploymentInsuranceRate;
    private double personalDeduction;
    private double dependentDeduction;
    private double overtimeRate;
    private int standardWorkingDays;
    private List<TaxBraket> taxBraket;


    public Parameter() {
        taxBraket = new ArrayList<>();
    }

    //copy tu tham so khac
    public Parameter(Parameter other) {
        this.socialInsuranceRate = other.socialInsuranceRate;
        this.healthInsuranceRate = other.healthInsuranceRate;
        this.unemploymentInsuranceRate = other.unemploymentInsuranceRate;
        this.personalDeduction = other.personalDeduction;
        this.dependentDeduction = other.dependentDeduction;
        this.overtimeRate = other.overtimeRate;
        this.standardWorkingDays = other.standardWorkingDays;
        this.taxBraket = new ArrayList<>();
        if (other.taxBraket != null) {
            for (int i = 0; i < other.taxBraket.size(); i++) {
                TaxBraket t = other.taxBraket.get(i);
                TaxBraket copy = new TaxBraket();
                copy.setMinIncome(t.getMinIncome());
                copy.setMaxIncome(t.getMaxIncome());
                copy.setTaxRate(t.getTaxRate());
                this.taxBraket.add(copy);
            }
        }
    }

    //kiem tra tham so
    public String validation() {
        String s = "";
        if (socialInsuranceRate < 0 || socialInsuranceRate > 1) {
            s += "Bao hiem xa hoi phai tu 0 den 1\n";
        }
        if (healthInsuranceRate < 0 || healthInsuranceRate > 1) {
            s += "Bao hiem y te phai tu 0 den 1\n";
        }
        if (unemploymentInsuranceRate < 0 || unemploymentInsuranceRate > 1) {
            s += "Bao hiem that nghiep phai tu 0 den 1\n";
        }
        if (personalDeduction <= 0) {
            s += "Giam tru ban than phai lon hon 0\n";
        }
        if (dependentDeduction <= 0) {
            s += "Giam tru nguoi phu thuoc phai lon hon 0\n";
        }
        if (overtimeRate < 1) {
            s += "He so OT phai >= 1\n";
        }
        if (standardWorkingDays <= 0) {
            s += "Ngay cong chuan phai lon hon 0\n";
        }
        if (taxBraket == null || taxBraket.isEmpty()) {
            s += "Danh sach muc thue ko duoc de trong\n";
        } else {
            for (int i = 0; i < taxBraket.size(); i++) {
                String e = taxBraket.get(i).validation();
                if (e != null) {
                    s += "Muc thue " + (i + 1) + ": " + e + "\n";
                }
            }
        }
        if (s.length() > 0) return s;
        return null;
    }

    public double getSocialInsuranceRate() {
        return socialInsuranceRate;
    }

    public void setSocialInsuranceRate(double socialInsuranceRate) {
        this.socialInsuranceRate = socialInsuranceRate;
    }

    public double getHealthInsuranceRate() {
        return healthInsuranceRate;
    }

    public void setHealthInsuranceRate(double healthInsuranceRate) {
        this.healthInsuranceRate = healthInsuranceRate;
    }

    public double getUnemploymentInsuranceRate() {
        return unemploymentInsuranceRate;
    }

    public void setUnemploymentInsuranceRate(double unemploymentInsuranceRate) {
        this.unemploymentInsuranceRate = unemploymentInsuranceRate;
    }

    public double getPersonalDeduction() {
        return personalDeduction;
    }

    public void setPersonalDeduction(double personalDeduction) {
        this.personalDeduction = personalDeduction;
    }

    public double getDependentDeduction() {
        return dependentDeduction;
    }

    public void setDependentDeduction(double dependentDeduction) {
        this.dependentDeduction = dependentDeduction;
    }

    public double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public int getStandardWorkingDays() {
        return standardWorkingDays;
    }

    public void setStandardWorkingDays(int standardWorkingDays) {
        this.standardWorkingDays = standardWorkingDays;
    }

    public List<TaxBraket> getTaxBraket() {
        return taxBraket;
    }

    public void setTaxBraket(List<TaxBraket> taxBraket) {
        this.taxBraket = taxBraket;
    }

    @Override
    public String toString() {
        return "Parameter\n" +
                "Bao hiem xa hoi: " + socialInsuranceRate +
                "\nBao hiem y te: " + healthInsuranceRate +
                "\nBao hiem that nghiep: " + unemploymentInsuranceRate +
                "\nGiam tru ban than: " + personalDeduction +
                "\nGiam tru nguoi phu thuoc: " + dependentDeduction +
                "\nNgay cong chuan: " + standardWorkingDays +
                "\nHe so OT: " + overtimeRate +
                "\nMuc thue: " + taxBraket.toString();
    }


}
