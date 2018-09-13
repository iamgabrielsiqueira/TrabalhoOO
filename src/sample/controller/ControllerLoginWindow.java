package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.model.JDBCUserDAO;
import sample.model.User;
import java.io.IOException;
import java.util.Optional;

public class ControllerLoginWindow {
    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tfPassword;

    @FXML
    public Parent mainWindow;

    public static User user;

    @FXML
    private void login() throws Exception {

        String username = tfUsername.getText();
        String password = tfPassword.getText();

        user = JDBCUserDAO.getInstance().search(username, password);

        if(user != null) {
            switchWindow("../view/mainWindow.fxml");
        } else {
            message(Alert.AlertType.ERROR, "Authentication error");
        }

    }

    public void switchWindow(String address){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(address));

                try {
                    Parent layoutWindow = loader.load();

                    Stage stage=(Stage)mainWindow.getScene().getWindow();

                    stage.setScene(new Scene(layoutWindow,500, 575));
                    stage.setResizable(false);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    private void createAccount() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Account");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/createAccountWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK) {
                ControllerCreateAccountWindow controllerCreateAccountWindow = fxmlLoader.getController();

                User user = controllerCreateAccountWindow.processResult();

                if(user!=null) {
                    try {
                        JDBCUserDAO.getInstance().create(user);
                        message(Alert.AlertType.INFORMATION, "Account created!");
                    } catch (Exception e) {
                        message(Alert.AlertType.ERROR, "This username already exists!");
                    }
                } else {
                    message(Alert.AlertType.ERROR, "Error!");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void message(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message!");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
