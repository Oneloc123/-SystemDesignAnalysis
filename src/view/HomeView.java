package view;

import controller.HomeController;
import controller.MainController;
import enumModel.AddressEnum;


import static controller.MainController.printList;


public class HomeView extends View {
    HomeController hc;

    AddressEnum address = AddressEnum.Home;

    public HomeView(HomeController hc) {
        // cật nhật địa chỉ
        MainController.addresses.add(address);
        //
        //
        this.hc = hc;
    }
    //
    public String[] funcs = {"Tính Lương",
            "Quản lý tuyển dụng", "Quản lý hồ sơ", "Quản lý admin"};

    @Override
    public void show() throws Exception{
        loop:
        while(true) {
            // in danh sach
            printAddress();
            printList(funcs);
            // doc dau vao
            printAddress();
            handleInput();
            //exit
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            // thuc thi func
            hc.excuteComent(question);
        }
    }



}
