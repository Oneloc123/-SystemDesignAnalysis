package view;

import controller.HomeController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HomeView extends View {
    HomeController hc;
    String question;
    static BufferedReader netIn;
    String address = "Home";

    public HomeView(HomeController hc) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.hc = hc;
    }
     String[] funcs = {"Tính Lương",
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
        System.out.println("Danh Sách Chức Năng");
        System.out.println("0. Thoát");
        for(int i=1;i<list.length+1;i++){
            System.out.println(i+": "+list[i-1]);
        }
    }

    public void handleInput() throws IOException {
        String input = netIn.readLine();
        question = input.toUpperCase();
    }

    public void printAddress(){
        System.out.print(address+"> ");
    }
}
