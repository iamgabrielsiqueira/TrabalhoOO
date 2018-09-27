package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ControllerSubcategoryAll {

    @FXML
    public Parent mainWindow;

    @FXML
    private TableView<Subcategory> tbSubcategory;

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

        tbSubcategory.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    cm.show(tbSubcategory, t.getScreenX(), t.getScreenY());
                }
            }
        });

        mi1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subcategory subcategory = tbSubcategory.getSelectionModel().getSelectedItem();
                try {
                    update(subcategory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mi2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subcategory subcategory = tbSubcategory.getSelectionModel().getSelectedItem();
                remove(subcategory);
            }
        });


        reloadList();
    }

    public void update(Subcategory subcategory) throws Exception {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("Update Subcategory");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/updateSubcategoryWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());

            ControllerUpdateSubcategoryWindow controle = fxmlLoader.getController();

            controle.setDados(subcategory);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK){

                Subcategory j = controle.processResult();

                if(j!=null){
                    try {
                        JDBCSubcategoryDAO.getInstance().update(subcategory, j);
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

    public void remove(Subcategory subcategory) {
        if(subcategory!=null){
            try {
                JDBCSubcategoryDAO.getInstance().delete(subcategory);
                reloadList();
            } catch (Exception e){
                message(Alert.AlertType.ERROR,e.getMessage());
            }
        }else{
            message(Alert.AlertType.ERROR,"Invalid data!!!");
        }

    }

    private void reloadList() {

        ObservableList<Subcategory> list;
        list = FXCollections.observableArrayList();

        list.clear();

        try {
            tbSubcategory.getItems().clear();
            for (Category category: JDBCCategoryDAO.getInstance().list(user)) {
                for(Subcategory subcategory:JDBCSubcategoryDAO.getInstance().list(user, category)) {
                    list.add(subcategory);
                }
            }
            tbSubcategory.setItems(list);
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
