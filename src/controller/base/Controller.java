package controller.base;

import view.View;

public abstract class Controller {
    protected View view;

    public abstract void showOn();

    public void showOff() {
    }

    public void showError(String message) {
        if (view != null) {
            view.showError(message);
        }
    }

    public boolean checkAuth() {
        return true;
    }

    public void showSuccess(String message) {
        System.out.println("------------------------");
        System.out.println(message);
        System.out.println("------------------------");
    }
}
