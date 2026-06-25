package view.calcSalary;

import controller.CalcSalaryController;
import model.calcSalary.Parameter;
import model.calcSalary.TaxBracket;
import view.View;

import java.io.IOException;
import java.util.List;

public class ParameterSettingsView extends View {
    private CalcSalaryController calcSalaryController;

    public ParameterSettingsView(CalcSalaryController calcSalaryController) {
        this.calcSalaryController = calcSalaryController;
    }

    @Override
    public void show() throws Exception {
        //lay param tu controller ve de chinh sua
        Parameter workingCopy = new Parameter(calcSalaryController.getParameter());
        editParameter(workingCopy);
    }

    //menu chinh sua tham so
    public void editParameter(Parameter workingCopy) throws Exception {
        loop:
        while (true) {
            System.out.println("\n========== CHINH SUA THAM SO ==========");
            printParameter(workingCopy);
            System.out.println("-----------------------------------------");
            System.out.println("0. Luu tham so");
            System.out.println("00. Bo qua, ko luu");
            System.out.println("-----------------------------------------");
            System.out.print("Chon truong can sua (0-8): ");

            String input = netIn.readLine().trim();

            if (input.equals("0")) {
                //gui len controller de validate va luu
                String errors = calcSalaryController.saveParameter(workingCopy);
                if (errors == null) {
                    System.out.println("\nDa luu tham so thanh cong!");
                    break;
                } else {
                    System.out.println("\nLOI: Tham so ko hop le:");
                    System.out.println(errors);
                    System.out.println("Vui long sua lai truoc khi luu.\n");
                    continue;
                }
            } else if (input.equals("00")) {
                break loop;
            }

            switch (input) {
                case "1":
                    workingCopy.setSocialInsuranceRate(
                            readDouble("Bao hiem xa hoi (0-1)", 0, 1));
                    break;
                case "2":
                    workingCopy.setHealthInsuranceRate(
                            readDouble("Bao hiem y te (0-1)", 0, 1));
                    break;
                case "3":
                    workingCopy.setUnemploymentInsuranceRate(
                            readDouble("Bao hiem that nghiep (0-1)", 0, 1));
                    break;
                case "4":
                    workingCopy.setPersonalDeduction(
                            readDouble("Giam tru ban than (VND)", 0, Double.MAX_VALUE));
                    break;
                case "5":
                    workingCopy.setDependentDeduction(
                            readDouble("Giam tru nguoi phu thuoc (VND)", 0, Double.MAX_VALUE));
                    break;
                case "6":
                    workingCopy.setOvertimeRate(
                            readDouble("He so OT (>=1)", 1, Double.MAX_VALUE));
                    break;
                case "7":
                    editTaxBrackets(workingCopy);
                    break;
                case "8":
                    workingCopy.setStandardWorkingDays(
                            (int) readDouble("Ngay cong chuan", 1, 31));
                    break;
                default:
                    System.out.println("Lua chon ko hop le. Nhap 0-8.");
            }
        }
    }

    //in thong tin tham so hien tai
    private void printParameter(Parameter p) {
        System.out.println("--- Gia tri hien tai ---");
        System.out.println("1. Bao hiem xa hoi: " + p.getSocialInsuranceRate());
        System.out.println("2. Bao hiem y te: " + p.getHealthInsuranceRate());
        System.out.println("3. Bao hiem that nghiep: " + p.getUnemploymentInsuranceRate());
        System.out.println("4. Giam tru ban than: " + p.getPersonalDeduction() + " VND");
        System.out.println("5. Giam tru nguoi phu thuoc: " + p.getDependentDeduction() + " VND");
        System.out.println("6. He so OT: " + p.getOvertimeRate());
        System.out.println("7. Ngay cong chuan: " + p.getStandardWorkingDays());

        List<TaxBracket> taxList = p.getTaxBracket();
        System.out.print("8. Cac muc thue: ");
        if (taxList == null || taxList.isEmpty()) {
            System.out.println("(trong)");
        } else {
            System.out.println();
            for (int i = 0; i < taxList.size(); i++) {
                TaxBracket t = taxList.get(i);
                System.out.println("   [" + t.getMinIncome() + " : " + t.getMaxIncome() + "] -> " + (t.getTaxRate() * 100) + "%");
            }
        }
    }

    //doc so tu ban phim, co kiem tra
    private double readDouble(String label, double min, double max) throws IOException {
        while (true) {
            System.out.print("Nhap " + label + ": ");
            String input = netIn.readLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < min) {
                    System.out.println("Gia tri phai >= " + min);
                    continue;
                }
                if (value > max && max != Double.MAX_VALUE) {
                    System.out.println("Gia tri phai <= " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Nhap so hop le (vd: 0.08).");
            }
        }
    }

    //menu quan ly cac muc thue
    private void editTaxBrackets(Parameter parameter) throws IOException {
        while (true) {
            List<TaxBracket> taxList = parameter.getTaxBracket();
            System.out.println("\n--- CAC MUC THUE ---");
            if (taxList.isEmpty()) {
                System.out.println("(chua co muc thue nao)");
            } else {
                for (int i = 0; i < taxList.size(); i++) {
                    TaxBracket t = taxList.get(i);
                    System.out.println((i + 1) + ". [" + t.getMinIncome() + " : " + t.getMaxIncome() + "] -> " + (t.getTaxRate() * 100) + "%");
                }
            }
            System.out.println("------------------------");
            System.out.println("a. Them muc thue moi");
            if (!taxList.isEmpty()) {
                System.out.println("s. Sua muc thue");
                System.out.println("x. Xoa muc thue");
            }
            System.out.println("q. Quay lai");
            System.out.print("Chon: ");

            String input = netIn.readLine().trim().toLowerCase();

            if (input.equals("q")) {
                break;
            }

            //them moi
            if (input.equals("a")) {
                TaxBracket newBracket = new TaxBracket();
                newBracket.setMinIncome(readDouble("Gioi han duoi (VND)", 0, Double.MAX_VALUE));
                newBracket.setMaxIncome(readDouble("Gioi han tren (VND)", 0, Double.MAX_VALUE));
                newBracket.setTaxRate(readDouble("Thue suat (0-1)", 0, 1));

                String err = newBracket.validation();
                if (err != null) {
                    System.out.println(" " + err);
                } else {
                    taxList.add(newBracket);
                    System.out.println("Da them muc thue.");
                }
                continue;
            }

            //sua/xoa theo so thu tu
            if (input.equals("s") || input.equals("x")) {
                System.out.print("Nhap so thu tu: ");
                String idxInput = netIn.readLine().trim();
                try {
                    int idx = Integer.parseInt(idxInput) - 1;
                    if (idx < 0 || idx >= taxList.size()) {
                        System.out.println("So thu tu ko hop le.");
                        continue;
                    }
                    if (input.equals("x")) {
                        taxList.remove(idx);
                        System.out.println("Da xoa muc thue.");
                    } else {
                        //sua
                        TaxBracket bracket = taxList.get(idx);
                        System.out.println("--- Sua muc thue " + (idx + 1) + " ---");
                        bracket.setMinIncome(readDouble("Gioi han duoi (VND)", 0, Double.MAX_VALUE));
                        bracket.setMaxIncome(readDouble("Gioi han tren (VND)", 0, Double.MAX_VALUE));
                        bracket.setTaxRate(readDouble("Thue suat (0-1)", 0, 1));

                        String err = bracket.validation();
                        if (err != null) {
                            System.out.println(" " + err);
                        } else {
                            System.out.println("Da sua muc thue.");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Nhap so nguyen.");
                }
                continue;
            }

            System.out.println("Lua chon ko hop le.");
        }
    }

}
