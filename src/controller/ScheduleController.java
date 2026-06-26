package controller;

import controller.base.Controller;
import dao.ScheduleDAO;
import model.ScheduleEntry;
import view.ScheduleView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleController extends Controller {
    ScheduleView scheduleView;
    private ScheduleDAO scheduleDAO;

    private int currentMonth;
    private int currentYear;
    private String viewMode = "THANG";
    private LocalDate selectedDate;

    public ScheduleController() {
        LocalDate now = LocalDate.now();
        this.currentMonth = now.getMonthValue();
        this.currentYear = now.getYear();
        this.selectedDate = now;
        this.scheduleView = new ScheduleView(this);
        this.view = this.scheduleView;
        this.scheduleDAO = new ScheduleDAO();
    }

    @Override
    public void showOn() {
        scheduleView.showSchedule();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }



    public String getCurrentMonthYear() {
        return String.format("%02d/%d", currentMonth, currentYear);
    }

    public String getViewMode() {
        return viewMode;
    }

    public String getSelectedDateStr() {
        return selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public boolean goToToday() {
        LocalDate now = LocalDate.now();
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();
        selectedDate = now;
        return true;
    }

    public boolean setViewMode(String mode) {
        if ("NGAY".equals(mode) || "TUAN".equals(mode) || "THANG".equals(mode)) {
            viewMode = mode;
            return true;
        }
        return false;
    }

    public boolean setSelectedDate(String dateStr) {
        try {
            selectedDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            currentMonth = selectedDate.getMonthValue();
            currentYear = selectedDate.getYear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<ScheduleEntry> getSchedule() {
        return scheduleDAO.findByEmployeeAndMonth(
                (int) ScreenManager.getCurrentUser().getUserId(),
                currentMonth, currentYear);
    }

    public List<ScheduleEntry> getFilteredSchedule() {
        List<ScheduleEntry> all = getSchedule();
        if ("THANG".equals(viewMode)) {
            return all;
        } else if ("TUAN".equals(viewMode)) {
            LocalDate weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate weekEnd = weekStart.plusDays(6);
            return all.stream()
                    .filter(e -> {
                        LocalDate d = LocalDate.parse(e.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        return !d.isBefore(weekStart) && !d.isAfter(weekEnd);
                    })
                    .collect(Collectors.toList());
        } else {
            String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return all.stream()
                    .filter(e -> e.getDate().equals(dateStr))
                    .collect(Collectors.toList());
        }
    }

    public List<ScheduleEntry> getDayEntries(String date) {
        return getSchedule().stream()
                .filter(e -> e.getDate().equals(date))
                .collect(Collectors.toList());
    }


    public boolean validateRange(int month, int year) {
        LocalDate now = LocalDate.now();
        LocalDate selected = LocalDate.of(year, month, 1);
        LocalDate minDate = now.minusMonths(6).withDayOfMonth(1);
        LocalDate maxDate = now.plusMonths(3).withDayOfMonth(1);
        return !selected.isBefore(minDate) && !selected.isAfter(maxDate);
    }

    public String checkRangeError(int month, int year) {
        return "Tháng không hợp lệ. Chỉ được xem tối đa 6 tháng trước và 3 tháng sau.";
    }

    public boolean hasConflicts(List<ScheduleEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                if (entries.get(i).hasTimeConflict(entries.get(j))) return true;
            }
        }
        return false;
    }

    public boolean hasInvalidTime(List<ScheduleEntry> entries) {
        for (ScheduleEntry e : entries) {
            if (e.isInvalidTime()) return true;
        }
        return false;
    }

    public List<String[]> getConflictDates() {
        List<String[]> result = new ArrayList<>();
        List<ScheduleEntry> all = getSchedule();
        for (int i = 0; i < all.size(); i++) {
            for (int j = i + 1; j < all.size(); j++) {
                if (all.get(i).hasTimeConflict(all.get(j))) {
                    result.add(new String[]{all.get(i).getDate(), all.get(i).getShiftType(),
                            all.get(j).getShiftType()});
                }
            }
        }
        return result;
    }

    public List<String> getInvalidDates() {
        List<String> result = new ArrayList<>();
        for (ScheduleEntry e : getSchedule()) {
            if (e.isInvalidTime() && !result.contains(e.getDate())) {
                result.add(e.getDate());
            }
        }
        return result;
    }

    public String[] getWeekRange() {
        LocalDate weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);
        return new String[]{
                weekStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                weekEnd.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        };
    }

    public int getDaysInMonth(int month, int year) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
    }

    public int getCurrentMonth() { return currentMonth; }
    public int getCurrentYear() { return currentYear; }
}
