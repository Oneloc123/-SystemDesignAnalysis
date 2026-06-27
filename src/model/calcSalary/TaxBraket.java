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

    public String validation() {
        String s = "";

        if (minIncome < 0) {
            s += "Thu nhap toi thieu ko duoc am";
        }

        if (maxIncome <= minIncome) {
            if (s.length() > 0) s += "; ";
            s += "Thu nhap toi da phai lon hon thu nhap toi thieu";
        }

        if (taxRate < 0 || taxRate > 1) {
            if (s.length() > 0) s += "; ";
            s += "Thue suat phai tu 0 den 1";
        }

        if (s.length() > 0) {
            return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return "[" + minIncome + " : " + maxIncome + "] -> " + taxRate;
    }
}
