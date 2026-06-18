package view;

import controller.MainController;
import controller.ScheduleController;
import enumModel.AddressEnum;

public class ScheduleView extends View {
    ScheduleController controller;
    AddressEnum address = AddressEnum.Schedule;

    public ScheduleView(ScheduleController controller) {
        MainController.addresses.add(address);
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
    }

    public void showSchedule() {
        System.out.println("\n========== LỊCH CA LÀM VIỆC ==========");
        System.out.println("Tháng 06/2024\n");

        String[][] shifts = controller.getMockShifts();
        String currentDate = "";

        for (String[] s : shifts) {
            if (!s[0].equals(currentDate)) {
                currentDate = s[0];
                System.out.println("--- " + currentDate + " ---");
            }
            String icon = s[1].contains("sáng") ? "🌅" : s[1].contains("chiều") ? "🌤" : s[1].contains("tối") ? "🌙" : "🚗";
            System.out.println("  " + icon + " " + s[1] + " (" + s[2] + ") [" + s[3] + "]");
        }

        System.out.println("\n======================================");
        System.out.println("Nhấn Enter để quay lại...");
        try {
            netIn.readLine();
        } catch (Exception e) {
        } finally {
            MainController.addresses.remove(address);
        }
    }
}
