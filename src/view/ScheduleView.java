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
        "01/01",
        "30/04",
        "01/05",
        "02/09"
    ));

    private boolean isHoliday(String dateStr) {
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

                System.out.println("\n--- Chức năng ---");
                System.out.println("1. Xem chi tiết ngày");
                System.out.println("2. Chọn tháng khác");
                System.out.println("3. Chuyển chế độ xem (Ngày/Tuần/Tháng)");
                System.out.println("4. Hôm nay");
                System.out.println("0. Quay lại");
                System.out.print("Chọn: ");
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
                        showError("Lệnh không hợp lệ");
                        break;
                }
            } catch (Exception e) {
                showError("Không thể tải lịch làm việc. Vui lòng thử lại sau.");
                break;
            }
        }

        MainController.addresses.remove(address);
    }

    private void renderCurrentView() {
        List<ScheduleEntry> entries = controller.getFilteredSchedule();
        String mode = controller.getViewMode();

        if ("THANG".equals(mode)) {
            System.out.println("\n=== LỊCH CA LÀM VIỆC - Tháng " + controller.getCurrentMonthYear() + " ===");
            renderMonthView(entries);
        } else if ("TUAN".equals(mode)) {
            String[] weekRange = controller.getWeekRange();
            System.out.println("\n=== LỊCH CA LÀM VIỆC - Tuần " + weekRange[0] + " - " + weekRange[1] + " ===");
            renderWeekView(entries);
        } else {
            System.out.println("\n=== LỊCH CA LÀM VIỆC - Ngày " + controller.getSelectedDateStr() + " ===");
            renderDetailDayView(entries);
        }
    }

    private void renderMonthView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Tháng " + controller.getCurrentMonthYear()
                    + " chưa có lịch làm việc. Vui lòng liên hệ HR nếu cần cập nhật.");
            return;
        }

        Map<String, List<ScheduleEntry>> grouped = groupByDate(entries);
        int totalDays = 0;

        for (Map.Entry<String, List<ScheduleEntry>> dayEntry : grouped.entrySet()) {
            String date = dayEntry.getKey();
            List<ScheduleEntry> dayEntries = dayEntry.getValue();
            totalDays++;

            boolean holiday = isHoliday(date);
            String dateHeader = "--- " + date + " ---" + (holiday ? " [NGHỈ LỄ]" : "");
            System.out.println("\n" + dateHeader);

            if (controller.hasConflicts(dayEntries)) {
                System.out.println("  !! Xung đột lịch");
            }
            if (controller.hasInvalidTime(dayEntries)) {
                System.out.println("  !! Dữ liệu ca không hợp lệ");
            }

            int shiftCount = dayEntries.size();
            StringBuilder summary = new StringBuilder();
            summary.append("  [").append(shiftCount).append(" ca: ");

            List<String> types = new ArrayList<>();
            for (ScheduleEntry e : dayEntries) {
                String typeLabel = e.getShiftType();
                if ("Công tác".equals(e.getShiftType())) {
                    String loc = (e.getLocation() != null) ? " (" + e.getLocation() + ")" : "";
                    typeLabel = "Công tác" + loc;
                }
                if (!types.contains(typeLabel)) types.add(typeLabel);
            }
            summary.append(String.join(" + ", types));
            summary.append("]");

            String status = "Xác nhận";
            for (ScheduleEntry e : dayEntries) {
                if ("Tạm thời".equals(e.getStatusLabel())) status = "Tạm thời";
            }
            summary.append(" [").append(status).append("]");

            System.out.println(summary.toString());
        }

        System.out.println("\n--- Tổng: " + totalDays + " ngày, " + entries.size() + " ca ---");
    }

    private void renderWeekView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Tuần này không có lịch làm việc.");
            return;
        }

        Map<String, List<ScheduleEntry>> grouped = groupByDate(entries);

        for (Map.Entry<String, List<ScheduleEntry>> dayEntry : grouped.entrySet()) {
            String date = dayEntry.getKey();
            List<ScheduleEntry> dayEntries = dayEntry.getValue();

            boolean holiday = isHoliday(date);
            String dateHeader = "--- " + date + " ---" + (holiday ? " [NGHỈ LỄ]" : "");
            System.out.println("\n" + dateHeader);
            if (controller.hasConflicts(dayEntries)) {
                System.out.println("  !! Xung đột lịch");
            }
            if (controller.hasInvalidTime(dayEntries)) {
                System.out.println("  !! Dữ liệu ca không hợp lệ");
            }

            int shiftCount = dayEntries.size();
            StringBuilder summary = new StringBuilder();
            summary.append("  [").append(shiftCount).append(" ca: ");
            List<String> types = new ArrayList<>();
            for (ScheduleEntry e : dayEntries) {
                String typeLabel = e.getShiftType();
                if ("Công tác".equals(e.getShiftType())) {
                    String loc = (e.getLocation() != null) ? " (" + e.getLocation() + ")" : "";
                    typeLabel = "Công tác" + loc;
                }
                if (!types.contains(typeLabel)) types.add(typeLabel);
            }
            summary.append(String.join(" + ", types));
            summary.append("]");

            String status = "Xác nhận";
            for (ScheduleEntry e : dayEntries) {
                if ("Tạm thời".equals(e.getStatusLabel())) status = "Tạm thời";
            }
            summary.append(" [").append(status).append("]");
            System.out.println(summary.toString());
        }

        System.out.println("\n--- Tổng: " + entries.size() + " ca ---");
    }

    private void renderDetailDayView(List<ScheduleEntry> entries) {
        if (entries.isEmpty()) {
            System.out.println("Ngày " + controller.getSelectedDateStr() + " không có lịch làm việc.");
            return;
        }

        System.out.println("\n--- Chi tiết ngày " + controller.getSelectedDateStr() + " ---");
        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry e = entries.get(i);
            String label = "  " + (i + 1) + ". " + e.getShiftLabel() + " " + e.getShiftType()
                    + " (" + e.getStartTime() + " - " + e.getEndTime() + ") [" + e.getStatusLabel() + "]";
            System.out.println(label);

            if ("Công tác".equals(e.getShiftType())) {
                if (e.getEventName() != null) {
                    System.out.println("     Sự kiện: " + e.getEventName());
                }
                if (e.getLocation() != null) {
                    System.out.println("     Địa điểm: " + e.getLocation());
                }
            }

            if (e.isInvalidTime()) {
                System.out.println("     !! Giờ vào > Giờ ra, dữ liệu không hợp lệ.");
            }
        }

        if (controller.hasConflicts(entries)) {
            System.out.println("\n  !! Phát hiện xung đột lịch. Vui lòng báo với Trưởng phòng để điều chỉnh.");
        }
    }

    private Map<String, List<ScheduleEntry>> groupByDate(List<ScheduleEntry> entries) {
        Map<String, List<ScheduleEntry>> grouped = new LinkedHashMap<>();
        for (ScheduleEntry e : entries) {
            grouped.computeIfAbsent(e.getDate(), k -> new ArrayList<>()).add(e);
        }
        return grouped;
    }

    private void handleViewDayDetail() {
        try {
            int savedMonth = controller.getCurrentMonth();
            int savedYear = controller.getCurrentYear();
            String savedMode = controller.getViewMode();

            System.out.print("Nhập ngày (dd/MM/yyyy) hoặc Enter để xem ngày "
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
                showError("Ngày không hợp lệ.");
            }
            System.out.println("\nNhấn Enter để tiếp tục...");
            netIn.readLine();
        } catch (Exception e) {
            showError("Lỗi xử lý.");
        }
    }

    private void handleChangeMonth() {
        try {
            System.out.println("Giới hạn: tối đa 6 tháng trước và 3 tháng sau.");
            System.out.print("Nhập tháng (1-12): ");
            String inputMonth = netIn.readLine();
            System.out.print("Nhập năm (yyyy): ");
            String inputYear = netIn.readLine();

            if (inputMonth == null || inputYear == null) return;

            int month = Integer.parseInt(inputMonth.trim());
            int year = Integer.parseInt(inputYear.trim());

            if (month < 1 || month > 12) {
                showError("Tháng không hợp lệ.");
                return;
            }

            if (!controller.validateRange(month, year)) {
                showError(controller.checkRangeError(month, year));
                return;
            }

            String dateStr = "01/" + String.format("%02d", month) + "/" + year;
            controller.setSelectedDate(dateStr);
        } catch (NumberFormatException e) {
            showError("Vui lòng nhập số.");
        } catch (Exception e) {
            showError("Lỗi xử lý.");
        }
    }

    private void handleChangeViewMode() {
        try {
            System.out.print("Chọn chế độ xem (1. Ngày / 2. Tuần / 3. Tháng): ");
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
                    showError("Lựa chọn không hợp lệ.");
                    break;
            }
        } catch (Exception e) {
            showError("Lỗi xử lý.");
        }
    }
}
