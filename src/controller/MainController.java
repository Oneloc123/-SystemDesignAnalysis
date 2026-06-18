package controller;

import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static User currentUser;
    void setCurrentUser(User currentUser) {
        MainController.currentUser = currentUser;
    }
    public static List<AddressEnum> addresses = new ArrayList<>();
    public static void printList(String[] list){
        System.out.println("------------------------");
        System.out.println("Danh Sách Chức Năng");
        System.out.println("0. Thoát");
        for(int i=1;i<list.length+1;i++){
            System.out.println((i)+": "+list[i-1]);
        }
        System.out.println("------------------------");
        System.out.println("Vui lòng chọn chức năng:");
    }
}
