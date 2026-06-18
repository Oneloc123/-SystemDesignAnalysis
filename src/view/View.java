package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import controller.MainController;
import enumModel.AddressEnum;

public abstract class View {
    protected String question;
    protected static BufferedReader netIn = new BufferedReader(new InputStreamReader(System.in));;
    public  abstract void show() throws Exception;
    public  void showError(String error){
        System.out.println("------------------------");
        System.out.println("LỖI HỆ THỐNG: "+error);
        System.out.println("------------------------");
    }
    public void handleInput() throws IOException {
        String input = netIn.readLine();
        if (input == null) { question = "0"; return; }
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
    public double handleDouleParam(String nameParam){
        double result = 0;
        System.out.print("nhập "+ nameParam+" : ");
        while(true){
            try{
                result = Double.parseDouble(handleParam("lương"));
                break;
            }catch (Exception e){
                showError("lương phải là số");
            }
        }
        return  result;
    }

    public String handleParam(String nameParam) throws IOException {
        printAddress();
        System.out.print("nhập "+ nameParam+" : ");
        return netIn.readLine();
    }
}
