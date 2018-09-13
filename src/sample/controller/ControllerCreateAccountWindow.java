package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.model.JDBCUserDAO;
import sample.model.User;

public class ControllerCreateAccountWindow {

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfLogin;

    @FXML
    private PasswordField tfPassword;

    public User processResult() {

        String name = tfName.getText();
        String login = tfLogin.getText();
        String password = tfPassword.getText();

        if(name.isEmpty() || login.isEmpty() || password.isEmpty()) {
            return null;
        } else {
            User user = new User();

            user.setName(name);
            user.setLogin(login);
            user.setPassword(password);

            return user;
        }

    }

}
