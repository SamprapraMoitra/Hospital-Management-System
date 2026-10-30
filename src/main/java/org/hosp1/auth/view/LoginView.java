package org.hosp1.auth.view;


public interface LoginView {

    String getEmail();
    char[] getPassword();

    void showMessage(String message);

    void showError(String message);

    void clearPassword();

    void showView();

    void hideView();

    void setLoginAction(Runnable action);

    void setRegisterAction(Runnable action);
}