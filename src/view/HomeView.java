package view;

import controller.HomeController;
import controller.MainController;
import enumModel.AddressEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HomeView extends View {
    HomeController hc;
    String question;
    static BufferedReader netIn;
    AddressEnum address = AddressEnum.Home;

    public HomeView(HomeController hc) {
        // cật nhật địa chỉ
        MainController.addresses.add(address);
        //
        netIn = new BufferedReader(new InputStreamReader(System.in));
        //
        this.hc = hc;
    }
    //
    public String[] funcs = {"Tính Lương",
            "Quản lý tuyển dụng"};

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

    public  void printList(String[] list){
        System.out.println("------------------------");
        System.out.println("Danh Sách Chức Năng");
        System.out.println("0. Thoát");
        for(int i=1;i<list.length+1;i++){
            System.out.println((i)+": "+list[i-1]);
        }
        System.out.println("------------------------");
        System.out.println("Vui lòng chọn chức năng:");
    }

    public void handleInput() throws IOException {
        String input = netIn.readLine();
        question = input.toUpperCase();
    }

    public void printAddress(){
        String result = "";
        for(AddressEnum add: MainController.addresses){
            result += add+"/";
        }
        result += ">";
        System.out.print(result);
    }

}
