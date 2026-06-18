package model.calcSalary;

public class TaxBraket {
    private long id;

    private double minIncome;
    private double maxIncome;

    private double taxRate;

    public TaxBraket() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMinIncome() {
        return minIncome;
    }

    public void setMinIncome(double minIncome) {
        this.minIncome = minIncome;
    }

    public double getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(double maxIncome) {
        this.maxIncome = maxIncome;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
}
