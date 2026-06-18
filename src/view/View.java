package view;

import controller.MainController;
import enumModel.AddressEnum;

public abstract class View {

    public abstract void show() throws Exception;
    public void showError(String error){
        System.out.println("------------------------");
        System.out.println("SYSTEM ERROR :"+error);
        System.out.println("------------------------");
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
