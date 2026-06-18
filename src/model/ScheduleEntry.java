package model;

public class ScheduleEntry {
    private String date;        // dd/MM/yyyy
    private String shiftType;   // Sáng, Chiều, Tối, Công tác
    private String startTime;   // HH:mm
    private String endTime;     // HH:mm
    private String status;      // Xác nhận, Tạm thời
    private String eventName;   // Tên sự kiện (chỉ cho Công tác)
    private String location;    // Địa điểm (chỉ cho Công tác)

    public ScheduleEntry() {}

    public ScheduleEntry(String date, String shiftType, String startTime, String endTime, String status) {
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public ScheduleEntry(String date, String shiftType, String startTime, String endTime, String status,
                         String eventName, String location) {
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.eventName = eventName;
        this.location = location;
    }

    public String getShiftLabel() {
        switch (shiftType) {
            case "Sáng": return "[S]";
            case "Chiều": return "[C]";
            case "Tối": return "[T]";
            case "Công tác": return "[CT]";
            default: return "[?]";
        }
    }

    public String getStatusLabel() {
        if ("Tạm thời".equals(status)) return "Tạm thời";
        return "Xác nhận";
    }

    public boolean hasTimeConflict(ScheduleEntry other) {
        if (other == null || !this.date.equals(other.date)) return false;
        int thisStart = timeToMinutes(this.startTime);
        int thisEnd = timeToMinutes(this.endTime);
        int otherStart = timeToMinutes(other.startTime);
        int otherEnd = timeToMinutes(other.endTime);
        return thisStart < otherEnd && otherStart < thisEnd;
    }

    public boolean isInvalidTime() {
        return timeToMinutes(this.startTime) >= timeToMinutes(this.endTime);
    }

    private int timeToMinutes(String time) {
        if (time == null || !time.contains(":")) return 0;
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
