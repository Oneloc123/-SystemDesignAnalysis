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
        System.out.println("SYSTEM ERROR :"+error);
        System.out.println("------------------------");
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
