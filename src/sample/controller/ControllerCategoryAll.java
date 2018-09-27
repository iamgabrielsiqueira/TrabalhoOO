package sample.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.model.*;

import java.io.IOException;
import java.util.Optional;

public class ControllerCategoryAll {

    @FXML
    public Parent mainWindow;

    @FXML
    private TableView<Category> tbCategory;

    @FXML
    private TableColumn<Category, Integer> tcId;

    @FXML
    private TableColumn<Category, String> tcName;

    public User user;

    public void initialize() {

        user = ControllerLoginWindow.user;

        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tcId.setStyle("-fx-alignment: CENTER;");
        tcName.setStyle("-fx-alignment: CENTER;");

        ContextMenu cm = new ContextMenu();

        MenuItem mi1 = new MenuItem("Edit");
        cm.getItems().add(mi1);

        MenuItem mi2 = new MenuItem("Remove");
        cm.getItems().add(mi2);

        tbCategory.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    cm.show(tbCategory, t.getScreenX(), t.getScreenY());
                }
            }
        });

        mi1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Category category = tbCategory.getSelectionModel().getSelectedItem();
                System.out.println(category.getName());
                try {
                    update(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mi2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Category category = tbCategory.getSelectionModel().getSelectedItem();
                System.out.println(category.getName());
                remove(category);
            }
        });

        reloadList();
    }

    public void update(Category category) throws Exception {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("Update Category");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/updateCategoryWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());

            ControllerUpdateCategory controle = fxmlLoader.getController();

            controle.setDados(category);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK){

                Category j = controle.processResult();

                if(j!=null){
                    try {
                        JDBCCategoryDAO.getInstance().update(category, j);
                        message(Alert.AlertType.INFORMATION, "Done!");
                        reloadList();
                    } catch (Exception e){
                        message(Alert.AlertType.ERROR,e.getMessage());
                    }
                }else{
                    message(Alert.AlertType.ERROR,"Invalid data!!!");
                }

            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void remove(Category category) {
        if(category!=null){
            try {
                JDBCCategoryDAO.getInstance().delete(category);
                reloadList();
            } catch (Exception e){
                message(Alert.AlertType.ERROR,e.getMessage());
            }
        }else{
            message(Alert.AlertType.ERROR,"Invalid data!!!");
        }

    }

    private void reloadList() {
        try {
            tbCategory.getItems().clear();
            tbCategory.setItems(JDBCCategoryDAO.getInstance().list(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getBack() {
        switchWindow("../view/mainWindow.fxml");
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

    protected void message(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message!");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
