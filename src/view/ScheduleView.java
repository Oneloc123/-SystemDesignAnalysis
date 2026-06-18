package view;

import controller.MainController;
import controller.ScheduleController;
import enumModel.AddressEnum;
import model.ScheduleEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScheduleView extends View {
    ScheduleController controller;
    AddressEnum address = AddressEnum.Schedule;

    private static final Set<String> HOLIDAYS = new HashSet<>(Arrays.asList(
        "01/01",     // Tet Duong lich
        "30/04",     // Ngay Giai phong
        "01/05",     // Quoc te Lao dong
        "02/09"      // Quoc khanh
    ));

    private boolean isHoliday(String dateStr) {
        // dateStr format: dd/MM/yyyy
        if (dateStr == null || dateStr.length() < 5) return false;
        String ddMM = dateStr.substring(0, 5);
        return HOLIDAYS.contains(ddMM);
    }

    public ScheduleView(ScheduleController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showSchedule() {
        if (!controller.checkAuth()) {
            showError("Không có quyền truy cập");
            return;
        }

        while (true) {
            try {
                renderCurrentView();

                System.out.println("\n--- Chuc nang ---");
                System.out.println("1. Xem chi tiet ngay");
                System.out.println("2. Chon thang khac");
                System.out.println("3. Chuyen che do xem (Ngay/Tuan/Thang)");
                System.out.println("4. Hom nay");
                System.out.println("0. Quay lai");
                System.out.print("Chon: ");
                handleInput();

                if ("0".equals(question)) {
                    break;
                }

                switch (question) {
                    case "1":
                        handleViewDayDetail();
                        break;
                    case "2":
                        handleChangeMonth();
                        break;
                    case "3":
                        handleChangeViewMode();
                        break;
                    case "4":
                        controller.goToToday();
                        break;
                    default:
                        showError("Lenh khong hop le");
                        break;
                }
            } catch (Exception e) {
                showError("Khong the tai lich lam viec. Vui long thu lai sau.");
                break;
            }
        }

        MainController.addresses.remove(address);
    }

    private void renderCurrentView() {
        List<ScheduleEntry> entries = controller.getFilteredSchedule();
        String mode = controller.getViewMode();

        if ("THANG".equals(mode)) {
            System.out.println("\n=== LICH CA LAM VIEC - Thang " + controller.getCurrentMonthYear() + " ===");
            renderMonthView(entries);
        } else if ("TUAN".equals(mode)) {
            String[] weekRange = controller.getWeekRange();
            System.out.println("\n=== LICH CA LAM VIEC - Tuan " + weekRange[0] + " - " + weekRange[1] + " ===");
            renderWeekView(entries);
        } else {
            System.out.println("\n=== LICH CA LAM VIEC - Ngay " + controller.getSelectedDateStr() + " ===");
            renderDetailDayView(entries);
        }
    }

    // ---- Month View (grouped per A1.1) ----

    private void renderMonthView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Thang " + controller.getCurrentMonthYear()
                    + " chua co lich lam viec. Vui long lien he HR neu can cap nhat.");
            return;
        }

        Map<String, List<ScheduleEntry>> grouped = groupByDate(entries);
        int totalDays = 0;

        for (Map.Entry<String, List<ScheduleEntry>> dayEntry : grouped.entrySet()) {
            String date = dayEntry.getKey();
            List<ScheduleEntry> dayEntries = dayEntry.getValue();
            totalDays++;

            boolean holiday = isHoliday(date);
            String dateHeader = "--- " + date + " ---" + (holiday ? " [NGHI LE]" : "");
            System.out.println("\n" + dateHeader);

            // Check conflict/invalid
            if (controller.hasConflicts(dayEntries)) {
                System.out.println("  !! Xung dot lich");
            }
            if (controller.hasInvalidTime(dayEntries)) {
                System.out.println("  !! Du lieu ca khong hop le");
            }

            // Grouped summary (A1.1)
            int shiftCount = dayEntries.size();
            StringBuilder summary = new StringBuilder();
            summary.append("  [").append(shiftCount).append(" ca: ");

            List<String> types = new ArrayList<>();
            for (ScheduleEntry e : dayEntries) {
                String typeLabel = e.getShiftType();
                if ("Cong tac".equals(e.getShiftType()) || "Công tác".equals(e.getShiftType())) {
                    String loc = (e.getLocation() != null) ? " (" + e.getLocation() + ")" : "";
                    typeLabel = "Cong tac" + loc;
                }
                if (!types.contains(typeLabel)) types.add(typeLabel);
            }
            summary.append(String.join(" + ", types));
            summary.append("]");

            // Status of latest entry
            String status = "Xac nhan";
            for (ScheduleEntry e : dayEntries) {
                if ("Tam thoi".equals(e.getStatusLabel())) status = "Tam thoi";
            }
            summary.append(" [").append(status).append("]");

            System.out.println(summary.toString());
        }

        System.out.println("\n--- Tong: " + totalDays + " ngay, " + entries.size() + " ca ---");
    }

    // ---- Week View (grouped per A1.1) ----

    private void renderWeekView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Tuan nay khong co lich lam viec.");
            return;
        }

        Map<String, List<ScheduleEntry>> grouped = groupByDate(entries);

        for (Map.Entry<String, List<ScheduleEntry>> dayEntry : grouped.entrySet()) {
            String date = dayEntry.getKey();
            List<ScheduleEntry> dayEntries = dayEntry.getValue();

            boolean holiday = isHoliday(date);
            String dateHeader = "--- " + date + " ---" + (holiday ? " [NGHI LE]" : "");
            System.out.println("\n" + dateHeader);
            if (controller.hasConflicts(dayEntries)) {
                System.out.println("  !! Xung dot lich");
            }
            if (controller.hasInvalidTime(dayEntries)) {
                System.out.println("  !! Du lieu ca khong hop le");
            }

            int shiftCount = dayEntries.size();
            StringBuilder summary = new StringBuilder();
            summary.append("  [").append(shiftCount).append(" ca: ");
            List<String> types = new ArrayList<>();
            for (ScheduleEntry e : dayEntries) {
                String typeLabel = e.getShiftType();
                if ("Cong tac".equals(e.getShiftType()) || "Công tác".equals(e.getShiftType())) {
                    String loc = (e.getLocation() != null) ? " (" + e.getLocation() + ")" : "";
                    typeLabel = "Cong tac" + loc;
                }
                if (!types.contains(typeLabel)) types.add(typeLabel);
            }
            summary.append(String.join(" + ", types));
            summary.append("]");

            String status = "Xac nhan";
            for (ScheduleEntry e : dayEntries) {
                if ("Tam thoi".equals(e.getStatusLabel())) status = "Tam thoi";
            }
            summary.append(" [").append(status).append("]");
            System.out.println(summary.toString());
        }

        System.out.println("\n--- Tong: " + entries.size() + " ca ---");
    }

    // ---- Day View (full detail - A1.3) ----

    private void renderDetailDayView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Ngay " + controller.getSelectedDateStr() + " khong co lich lam viec.");
            return;
        }

        System.out.println("\n--- Chi tiet ngay " + controller.getSelectedDateStr() + " ---");
        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry e = entries.get(i);
            String label = "  " + (i + 1) + ". " + e.getShiftLabel() + " " + e.getShiftType()
                    + " (" + e.getStartTime() + " - " + e.getEndTime() + ") [" + e.getStatusLabel() + "]";
            System.out.println(label);

            if ("Cong tac".equals(e.getShiftType()) || "Công tác".equals(e.getShiftType())) {
                if (e.getEventName() != null) {
                    System.out.println("     Su kien: " + e.getEventName());
                }
                if (e.getLocation() != null) {
                    System.out.println("     Dia diem: " + e.getLocation());
                }
            }

            if (e.isInvalidTime()) {
                System.out.println("     !! Gio vao > gio ra, du lieu khong hop le.");
            }
        }

        if (controller.hasConflicts(entries)) {
            System.out.println("\n  !! Phat hien xung dot lich. Vui long bao voi Truong phong de dieu chinh.");
        }
    }

    // ---- Helpers ----

    private Map<String, List<ScheduleEntry>> groupByDate(List<ScheduleEntry> entries) {
        Map<String, List<ScheduleEntry>> grouped = new LinkedHashMap<>();
        for (ScheduleEntry e : entries) {
            grouped.computeIfAbsent(e.getDate(), k -> new ArrayList<>()).add(e);
        }
        return grouped;
    }

    // ---- Handlers ----

    private void handleViewDayDetail() {
        try {
            int savedMonth = controller.getCurrentMonth();
            int savedYear = controller.getCurrentYear();
            String savedMode = controller.getViewMode();

            System.out.print("Nhap ngay (dd/MM/yyyy) hoac Enter de xem ngay "
                    + controller.getSelectedDateStr() + ": ");
            String input = netIn.readLine();
            if (input == null || input.isEmpty()) {
                input = controller.getSelectedDateStr();
            }
            if (controller.setSelectedDate(input)) {
                controller.setViewMode("NGAY");
                renderDetailDayView(controller.getFilteredSchedule());
                controller.setViewMode(savedMode);
                controller.setSelectedDate("01/" + String.format("%02d", savedMonth) + "/" + savedYear);
            } else {
                showError("Ngay khong hop le.");
            }
            System.out.println("\nNhan Enter de tiep tuc...");
            netIn.readLine();
        } catch (Exception e) {
            showError("Loi xu ly.");
        }
    }

    private void handleChangeMonth() {
        try {
            System.out.println("Gioi han: toi da 6 thang truoc va 3 thang sau.");
            System.out.print("Nhap thang (1-12): ");
            String inputMonth = netIn.readLine();
            System.out.print("Nhap nam (yyyy): ");
            String inputYear = netIn.readLine();

            if (inputMonth == null || inputYear == null) return;

            int month = Integer.parseInt(inputMonth.trim());
            int year = Integer.parseInt(inputYear.trim());

            if (month < 1 || month > 12) {
                showError("Thang khong hop le.");
                return;
            }

            if (!controller.validateRange(month, year)) {
                showError(controller.checkRangeError(month, year));
                return;
            }

            String dateStr = "01/" + String.format("%02d", month) + "/" + year;
            controller.setSelectedDate(dateStr);
        } catch (NumberFormatException e) {
            showError("Vui long nhap so.");
        } catch (Exception e) {
            showError("Loi xu ly.");
        }
    }

    private void handleChangeViewMode() {
        try {
            System.out.print("Chon che do xem (1. Ngay / 2. Tuan / 3. Thang): ");
            String input = netIn.readLine();
            if (input == null) return;

            switch (input.trim()) {
                case "1":
                    controller.setViewMode("NGAY");
                    break;
                case "2":
                    controller.setViewMode("TUAN");
                    break;
                case "3":
                    controller.setViewMode("THANG");
                    break;
                default:
                    showError("Lua chon khong hop le.");
                    break;
            }
        } catch (Exception e) {
            showError("Loi xu ly.");
        }
    }
}
