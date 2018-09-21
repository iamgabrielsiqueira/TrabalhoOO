package sample.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Expense;
import sample.model.JDBCExpenseDAO;
import sample.model.User;
import java.io.IOException;
import java.sql.Date;

public class ControllerExpensePeriod {

    @FXML
    private ListView<Expense> ltvwExpensePeriod;

    private ObservableList<Expense> lista;

    @FXML
    private ContextMenu listContextMenu;

    public User user;

    @FXML
    public Parent mainWindow;

    @FXML
    private DatePicker tfDate2;

    @FXML
    private DatePicker tfDate1;

    @FXML
    public void filter() throws Exception {

        user = ControllerLoginWindow.user;

        if(tfDate1.getValue() == null && tfDate2.getValue() != null) {
            lista.clear();
            lista = JDBCExpenseDAO.getInstance().listPeriod(user, Date.valueOf(tfDate2.getValue()));
            ltvwExpensePeriod.getItems().clear();
            ltvwExpensePeriod.setItems(lista);
            System.out.println("Não marcou");
            System.out.println(tfDate2.getValue());

        }

        if(tfDate1.getValue() != null && tfDate2.getValue() == null) {
            System.out.println(tfDate1.getValue());
            System.out.println("Não marcou");
        }

        if(tfDate1.getValue() != null && tfDate2.getValue() != null) {
            System.out.println(tfDate1.getValue());
            System.out.println(tfDate2.getValue());
        }

    }

    public void initialize() throws Exception {

        user = ControllerLoginWindow.user;
        lista = JDBCExpenseDAO.getInstance().list(user);
        listContextMenu = new ContextMenu();

        MenuItem setAsDone = new MenuItem("Set as done");

        setAsDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Expense item = ltvwExpensePeriod.getSelectionModel().getSelectedItem();
                //setAsDone(item);
            }
        });

        listContextMenu.getItems().addAll(setAsDone);

        ltvwExpensePeriod.setItems(lista);

        ltvwExpensePeriod
                .getSelectionModel()
                .setSelectionMode(SelectionMode.SINGLE);

        ltvwExpensePeriod.setCellFactory(new Callback<ListView<Expense>, ListCell<Expense>>() {
            @Override
            public ListCell<Expense> call(ListView<Expense> param) {
                ListCell<Expense> cell = new ListCell<Expense>(){
                    @Override
                    protected void updateItem(Expense item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else{
                            setText("R$" + String.valueOf(item.getCost()));
                        }
                    }
                };

                cell.emptyProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasEmpty, Boolean isNowEmpty) {

                        if(isNowEmpty){
                            cell.setContextMenu(null);
                        }else{
                            cell.setContextMenu(listContextMenu);
                        }
                    }});


                return cell;

            }
        });

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
}
