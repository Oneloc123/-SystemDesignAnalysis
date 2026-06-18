package view;

public abstract class View {
    public  abstract void show() throws Exception;
    public  void showError(String error){
        System.out.println(error);
    }
}
