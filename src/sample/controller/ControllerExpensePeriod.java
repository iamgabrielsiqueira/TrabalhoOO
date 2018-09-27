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
import sample.model.Expense;
import sample.model.JDBCExpenseDAO;
import sample.model.User;
import java.io.IOException;
import java.sql.Date;

public class ControllerExpensePeriod {

    private ObservableList<Expense> listaFiltrada;

    public User user;

    @FXML
    public Parent mainWindow;

    @FXML
    private DatePicker tfDate2;

    @FXML
    private DatePicker tfDate1;

    @FXML
    private TableView<Expense> tbExpenses;

    @FXML
    private TableColumn<Expense, Integer> tcCategory;

    @FXML
    private TableColumn<Expense, Integer> tcSubcategory;

    @FXML
    private TableColumn<Expense, java.util.Date> tcDate;

    @FXML
    private TableColumn<Expense, Double> tcCost;

    @FXML
    public void filter() throws Exception {

        user = ControllerLoginWindow.user;

        if(tfDate1.getValue() != null && tfDate2.getValue() != null) {
            Date data1 = Date.valueOf(tfDate1.getValue());
            Date data2 = Date.valueOf(tfDate2.getValue());
            listaFiltrada = FXCollections.observableArrayList();
            tbExpenses.getItems().clear();
            filtraLista(data1, data2);
        } else {
            reloadList();
        }

    }

    public void filtraLista(Date data1, Date data2) throws Exception {

        user = ControllerLoginWindow.user;

        listaFiltrada.clear();

        for (Expense expense: JDBCExpenseDAO.getInstance().list(user)) {
            if(expense.getDate().after(data1) && expense.getDate().before(data2)) {
                if(expense != null) {
                    listaFiltrada.add(expense);
                }
            }
        }

        tbExpenses.setItems(listaFiltrada);

    }

    public void initialize() {

        user = ControllerLoginWindow.user;

        tcCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        tcSubcategory.setCellValueFactory(new PropertyValueFactory<>("Subcategory"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tcCost.setCellFactory(tc -> new TableCell<Expense, Double>() {

            @Override
            protected void updateItem(Double cost, boolean empty) {
                super.updateItem(cost, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("R$" + String.valueOf(cost));
                }
            }
        });

        tcCost.setStyle("-fx-alignment: CENTER;");
        tcDate.setStyle("-fx-alignment: CENTER;");
        tcSubcategory.setStyle("-fx-alignment: CENTER;");
        tcCategory.setStyle("-fx-alignment: CENTER;");

        reloadList();
    }

    private void reloadList() {
        try {
            tbExpenses.getItems().clear();
            tbExpenses.setItems(JDBCExpenseDAO.getInstance().list(user));
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
}
