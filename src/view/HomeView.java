package view;

import controller.HomeController;
import controller.MainController;
import enumModel.AddressEnum;

import static controller.MainController.printList;

public class HomeView extends View {
    HomeController hc;
    AddressEnum address = AddressEnum.Home;

    public HomeView(HomeController hc) {
        MainController.addresses.add(address);
        this.hc = hc;
    }

    public String[] funcs = {
        "Xem thông tin cá nhân",
        "Xem lịch ca làm việc",
        "Đổi mật khẩu",
        "Danh sách nhân viên phòng ban",
        "Bảng chấm công phòng ban",
        "Quản lý tuyển dụng",
        "Tính lương"
    };

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            printAddress();
            printList(funcs);
            printAddress();
            handleInput();
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            hc.excuteComent(question);
        }
    }
}
