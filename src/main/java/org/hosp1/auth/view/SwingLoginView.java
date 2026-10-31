package org.hosp1.auth.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class SwingLoginView extends JFrame implements LoginView {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton registerButton = new JButton("Register");

    private Runnable loginAction;
    private Runnable registerAction;

    public SwingLoginView() {
        super("Hospital Management System - Login");
        buildUi();
        wireButtons();
    }

    private void buildUi() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Email:"), c);
        c.gridx = 1;
        panel.add(emailField, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Password:"), c);
        c.gridx = 1;
        panel.add(passwordField, c);

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(registerButton);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(buttons, c);

        setContentPane(panel);
        getRootPane().setDefaultButton(loginButton); // Enter key = Login
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void wireButtons() {
        loginButton.addActionListener(e -> {
            if (loginAction != null) {
                loginAction.run();
            }
        });
        registerButton.addActionListener(e -> {
            if (registerAction != null) {
                registerAction.run();
            }
        });
    }

    @Override
    public String getEmail() {
        return emailField.getText();
    }

    @Override
    public char[] getPassword() {
        return passwordField.getPassword();
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void clearPassword() {
        passwordField.setText("");
    }

    @Override
    public void showView() {
        emailField.setText("");
        passwordField.setText("");
        setVisible(true);
    }

    @Override
    public void hideView() {
        setVisible(false);
    }

    @Override
    public void setLoginAction(Runnable action) {
        this.loginAction = action;
    }

    @Override
    public void setRegisterAction(Runnable action) {
        this.registerAction = action;
    }
}