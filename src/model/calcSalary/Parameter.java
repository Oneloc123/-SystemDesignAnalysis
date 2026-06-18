package model.calcSalary;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private double
            socialInsuranceRate, //Bảo hiểm xã hội
            healthInsuranceRate, //Bảo hiểm y tế
            unemploymentInsuranceRate, //Bảo hiểm thất nghiệp
            personalDeduction, //Giảm trừ bản thân
            dependentDeduction, //Giảm trừ người phụ thuộc
            overtimeRate ; //Hệ số OT
    private List<TaxBraket> taxBraket;


    public Parameter() {

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

    public List<TaxBraket> getTaxBraket() {
        return taxBraket;
    }

    public void setTaxBraket(List<TaxBraket> taxBraket) {
        this.taxBraket = taxBraket;
    }
}
