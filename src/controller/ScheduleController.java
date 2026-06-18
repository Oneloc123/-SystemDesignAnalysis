package controller;

import controller.base.Controller;
import view.ScheduleView;

public class ScheduleController extends Controller {
    ScheduleView scheduleView;

    public ScheduleController() {
        this.scheduleView = new ScheduleView(this);
        this.view = this.scheduleView;
    }

    @Override
    public void showOn() {
        scheduleView.showSchedule();
    }

    @Override
    public boolean checkAuth() {
        return ScreenManager.getCurrentUser() != null;
    }

    public String[][] getMockShifts() {
        return new String[][]{
            {"01/06/2024", "Ca sáng", "08:00-12:00", "Xác nhận"},
            {"01/06/2024", "Ca chiều", "13:30-17:30", "Xác nhận"},
            {"02/06/2024", "Ca sáng", "08:00-12:00", "Xác nhận"},
            {"03/06/2024", "Ca tối", "18:00-22:00", "Tạm thời"},
            {"04/06/2024", "Công tác", "08:00-17:00", "Xác nhận"},
            {"05/06/2024", "Ca sáng", "08:00-12:00", "Xác nhận"},
            {"06/06/2024", "Ca chiều", "13:30-17:30", "Xác nhận"},
            {"07/06/2024", "Ca sáng", "08:00-12:00", "Xác nhận"},
            {"08/06/2024", "Ca tối", "18:00-22:00", "Xác nhận"},
        };
    }
}
