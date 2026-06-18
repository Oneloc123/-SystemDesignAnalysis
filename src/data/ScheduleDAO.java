package data;

import model.ScheduleEntry;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public static List<ScheduleEntry> findSchedule(Long userId, int month, int year) {
        List<ScheduleEntry> result = new ArrayList<>();

        if (month == 6 && year == 2024) {
            // 01/06 - Ca sáng + Ca chiều (nhiều ca trong ngày - test A1)
            result.add(new ScheduleEntry("01/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("01/06/2024", "Chiều", "13:30", "17:30", "Xác nhận"));
            // 01/06 - Công tác trùng giờ với ca sáng (test E1 - xung đột)
            result.add(new ScheduleEntry("01/06/2024", "Công tác", "09:00", "15:00", "Xác nhận",
                    "Họp đối tác", "Hà Nội"));

            // 02/06 - Ca tối
            result.add(new ScheduleEntry("02/06/2024", "Tối", "18:00", "22:00", "Tạm thời"));

            // 03/06 - Công tác
            result.add(new ScheduleEntry("03/06/2024", "Công tác", "08:00", "17:00", "Xác nhận",
                    "Đào tạo nghiệp vụ", "Hồ Chí Minh"));

            // 05/06 - Ca sáng + Ca chiều
            result.add(new ScheduleEntry("05/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("05/06/2024", "Chiều", "13:30", "17:30", "Xác nhận"));

            // 07/06 - Dữ liệu sai giờ (test E4 - giờ vào > giờ ra)
            result.add(new ScheduleEntry("07/06/2024", "Sáng", "12:00", "08:00", "Xác nhận"));

            // 08/06 - Ca sáng
            result.add(new ScheduleEntry("08/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));

            // 10/06 - Ca chiều + Ca tối (nhiều ca - test A1)
            result.add(new ScheduleEntry("10/06/2024", "Chiều", "13:30", "17:30", "Xác nhận"));
            result.add(new ScheduleEntry("10/06/2024", "Tối", "18:00", "22:00", "Xác nhận"));

            // 12/06 - Ca sáng
            result.add(new ScheduleEntry("12/06/2024", "Sáng", "08:00", "12:00", "Tạm thời"));

            // 15/06 - Ca sáng + Công tác (vừa ca vừa công tác - test A1)
            result.add(new ScheduleEntry("15/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("15/06/2024", "Công tác", "14:00", "17:00", "Xác nhận",
                    "Gặp khách hàng", "Đà Nẵng"));

            // 18/06 - Ca tối
            result.add(new ScheduleEntry("18/06/2024", "Tối", "18:00", "22:00", "Xác nhận"));

            // 20/06 - Ca sáng
            result.add(new ScheduleEntry("20/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));

            // 22/06 - Ca chiều
            result.add(new ScheduleEntry("22/06/2024", "Chiều", "13:30", "17:30", "Tạm thời"));

            // 25/06 - Ca sáng + Ca tối (xung đột - test E1)
            result.add(new ScheduleEntry("25/06/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("25/06/2024", "Tối", "18:00", "22:00", "Xác nhận"));

        } else if (month == 5 && year == 2024) {
            // Tháng 05/2024 - có dữ liệu
            result.add(new ScheduleEntry("10/05/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("10/05/2024", "Chiều", "13:30", "17:30", "Xác nhận"));
            result.add(new ScheduleEntry("12/05/2024", "Tối", "18:00", "22:00", "Xác nhận"));
            result.add(new ScheduleEntry("15/05/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("15/05/2024", "Công tác", "08:00", "17:00", "Xác nhận",
                    "Hội thảo", "Hà Nội"));
            result.add(new ScheduleEntry("20/05/2024", "Chiều", "13:30", "17:30", "Tạm thời"));
            result.add(new ScheduleEntry("22/05/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("25/05/2024", "Tối", "18:00", "22:00", "Xác nhận"));
            result.add(new ScheduleEntry("28/05/2024", "Sáng", "08:00", "12:00", "Xác nhận"));

        } else if (month == 7 && year == 2024) {
            // Tháng 07/2024 - có dữ liệu
            result.add(new ScheduleEntry("02/07/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("02/07/2024", "Chiều", "13:30", "17:30", "Xác nhận"));
            result.add(new ScheduleEntry("05/07/2024", "Tối", "18:00", "22:00", "Xác nhận"));
            result.add(new ScheduleEntry("08/07/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("08/07/2024", "Công tác", "08:00", "12:00", "Xác nhận",
                    "Kiểm tra dự án", "Đà Nẵng"));
            result.add(new ScheduleEntry("12/07/2024", "Chiều", "13:30", "17:30", "Xác nhận"));
            result.add(new ScheduleEntry("15/07/2024", "Tối", "18:00", "22:00", "Tạm thời"));
            result.add(new ScheduleEntry("18/07/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("20/07/2024", "Sáng", "08:00", "12:00", "Xác nhận"));
            result.add(new ScheduleEntry("20/07/2024", "Chiều", "13:30", "17:30", "Xác nhận"));

        }
        // Tháng khác trống (test A2)

        return result;
    }
}
