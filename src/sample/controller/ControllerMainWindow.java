package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import sample.model.Category;
import sample.model.JDBCCategoryDAO;

import java.io.IOException;
import java.util.Optional;

public class ControllerMainWindow {

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

    }

    protected void message(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message!");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
