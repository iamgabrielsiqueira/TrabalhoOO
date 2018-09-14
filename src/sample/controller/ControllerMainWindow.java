package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import sample.model.Category;
import sample.model.JDBCCategoryDAO;
import sample.model.JDBCSubcategoryDAO;
import sample.model.Subcategory;

import java.io.IOException;
import java.util.Optional;

public class ControllerMainWindow {

    @FXML
    public Parent mainWindow;

    @FXML
    private void logout() {
        ControllerLoginWindow.user = null;
        switchWindow("../view/loginWindow.fxml");
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
    private void addCategory() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Category");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/addCategoryWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK) {
                ControllerCategoryWindow controllerCategoryWindow = fxmlLoader.getController();

                Category category = controllerCategoryWindow.processResult();

                if(category!=null) {
                    try {
                        JDBCCategoryDAO.getInstance().create(category);
                        message(Alert.AlertType.INFORMATION, "Category created!");
                    } catch (Exception e) {
                        message(Alert.AlertType.ERROR, "This category already exists!");
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

    @FXML
    private void addSubcategory() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Subcategory");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/addSubcategoryWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK) {
                ControllerSubcategoryWindow controllerSubcategoryWindow = fxmlLoader.getController();

                Subcategory subcategory = controllerSubcategoryWindow.processResult();

                if(subcategory!=null) {
                    try {
                        JDBCSubcategoryDAO.getInstance().create(subcategory);
                        message(Alert.AlertType.INFORMATION, "Subcategory created!");
                    } catch (Exception e) {
                        message(Alert.AlertType.ERROR, "This subcategory already exists!");
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
