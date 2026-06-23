package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;

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
        question = input == null ? "0" : input.trim().toUpperCase();
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
        while(true){
            try{
                result = Double.parseDouble(handleParam(nameParam));
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

    public Date handleDate(String nameParam){
        Date result;
        while(true){
            try{
                result = java.sql.Date.valueOf(handleParam(nameParam));
                break;
            } catch (IllegalArgumentException e) {
                showError("Ngày không hợp lệ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    public Timestamp handleDateTime(String nameParam) {
        Timestamp result;
        while (true) {
            try {
                String input = handleParam(nameParam);
                // Nếu người dùng nhập thiếu giây (chỉ HH:mm), tự động thêm :00
                if (input.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                    input += ":00";
                }
                result = Timestamp.valueOf(input);
                break;
            } catch (IllegalArgumentException e) {
                showError("Thời gian không hợp lệ (định dạng yyyy-MM-dd HH:mm:ss)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public void printWelcome(String tile){
        System.out.println("---------------------");
        System.out.println(tile);
        System.out.println("---------------------");
    }
    public void showMessage(String text){
        System.out.println(text);
    }
}
