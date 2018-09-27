package sample.controller;

import javafx.application.Platform;
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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

public class ControllerMainWindow {

    @FXML
    public Parent mainWindow;

    @FXML
    private TableView<Expense> tbExpenses;

    @FXML
    private TableColumn<Expense, Integer> tcCategory;

    @FXML
    private TableColumn<Expense, Integer> tcSubcategory;

    @FXML
    private TableColumn<Expense, Date> tcDate;

    @FXML
    private TableColumn<Expense, Double> tcCost;

    @FXML
    private ComboBox<Category> cbCategory;

    private ObservableList<Category> categoryList;

    private User user;

    @FXML
    private Label lbTotal;

    private Double total;

    private String stringTotal;

    @FXML
    private void switchCategory() throws Exception {
        Category category = cbCategory.getSelectionModel().getSelectedItem();
        if(category != null) {
            if(category.getName().equals("All")) {
                tbExpenses.getItems().clear();
                tbExpenses.setItems(JDBCExpenseDAO.getInstance().list(user));

                total = JDBCExpenseDAO.getInstance().getTotalExpense(user);
                DecimalFormat numberFormat = new DecimalFormat("#.00");

                if(total == null || total == 0) {
                    stringTotal = "Total expenditure: R$0.00";
                } else {
                    stringTotal = "Total expenditure: R$" + numberFormat.format(total);
                }

                lbTotal.setText(stringTotal);
            } else {
                tbExpenses.getItems().clear();
                tbExpenses.setItems(JDBCExpenseDAO.getInstance().listCategory(user, category));

                total = JDBCExpenseDAO.getInstance().getTotalExpenseCategory(user, category);
                DecimalFormat numberFormat = new DecimalFormat("#.00");

                if(total == null || total == 0) {
                    stringTotal = "Total expenditure: R$0.00";
                } else {
                    stringTotal = "Total expenditure: R$" + numberFormat.format(total);
                }

                lbTotal.setText(stringTotal);
            }


        }
    }

    @FXML
    public void expensePeriod() {
        switchWindow("../view/viewExpensePeriod.fxml");
    }

    @FXML
    public void expenseDays() {
        switchWindow("../view/viewExpenseDay.fxml");
    }

    @FXML
    private void logout() {
        ControllerLoginWindow.user = null;
        switchWindow("../view/loginWindow.fxml");
    }

    public void initialize() throws Exception {

        user = ControllerLoginWindow.user;

        total = JDBCExpenseDAO.getInstance().getTotalExpense(user);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if(total == null || total == 0) {
            stringTotal = "Total expenditure: R$0.00";
        } else {
            stringTotal = "Total expenditure: R$" + numberFormat.format(total);
        }

        lbTotal.setText(stringTotal);

        try {
            categoryList = JDBCCategoryDAO.getInstance().listCb(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cbCategory.setItems(categoryList);

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

        ContextMenu cm = new ContextMenu();

        MenuItem mi1 = new MenuItem("Edit");
        cm.getItems().add(mi1);

        MenuItem mi2 = new MenuItem("Remove");
        cm.getItems().add(mi2);

        MenuItem mi3 = new MenuItem("Set as Done");
        cm.getItems().add(mi3);

        tbExpenses.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    cm.show(tbExpenses, t.getScreenX(), t.getScreenY());
                }
            }
        });

        mi1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Expense expense = tbExpenses.getSelectionModel().getSelectedItem();
                try {
                    update(expense);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mi2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Expense expense = tbExpenses.getSelectionModel().getSelectedItem();
                remove(expense);
            }
        });

        mi3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Expense expense = tbExpenses.getSelectionModel().getSelectedItem();
                setasDone(expense);
            }
        });

        reloadList();
    }

    public void update(Expense expense) throws Exception {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("Update Expense");

        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/updateExpenseWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());

            ControllerUpdateExpense controle = fxmlLoader.getController();

            controle.setDados(expense);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK){

                Expense j = controle.processResult();

                if(j!=null){
                    try {
                        JDBCExpenseDAO.getInstance().update(expense, j);
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

    public void remove(Expense expense) {
        if(expense!=null){
            try {
                JDBCExpenseDAO.getInstance().delete(expense);
                reloadList();
            } catch (Exception e){
                message(Alert.AlertType.ERROR,e.getMessage());
            }
        }else{
            message(Alert.AlertType.ERROR,"Invalid data!!!");
        }

    }

    public void setasDone(Expense expense) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Set as done");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/setDoneWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            ControllerSetDone controllerSetDone = fxmlLoader.getController();
            controllerSetDone.setDados(expense);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK) {
                boolean ok = controllerSetDone.processResult();

                if(ok) {
                    try {
                        JDBCExpenseDAO.getInstance().updateStatus(expense);
                        message(Alert.AlertType.INFORMATION, "Done!");
                    } catch (Exception e) {
                        message(Alert.AlertType.ERROR, "Can't set this expense!");
                    }
                } else {
                    message(Alert.AlertType.ERROR, "Error!");
                }
                reloadList();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadList() {
        try {
            tbExpenses.getItems().clear();
            tbExpenses.setItems(JDBCExpenseDAO.getInstance().list(user));
        } catch (Exception e) {
            e.printStackTrace();
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
    private void addExpense() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Expense");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/addExpenseWindow.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get()==ButtonType.OK) {
                ControllerExpenseWindow controllerExpenseWindow = fxmlLoader.getController();

                Expense expense = controllerExpenseWindow.processResult();

                if(expense!=null) {
                    try {
                        JDBCExpenseDAO.getInstance().create(expense);
                        message(Alert.AlertType.INFORMATION, "Expense added!");
                    } catch (Exception e) {
                        message(Alert.AlertType.ERROR, "Can't register this expense!");
                    }
                } else {
                    message(Alert.AlertType.ERROR, "Error!");
                }

                reloadList();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                reloadList();

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

                reloadList();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void listCategoryAll() {
        switchWindow("../view/viewCategoryAll.fxml");
    }

    @FXML
    public void listSubcategoryAll() {
        switchWindow("../view/viewSubcategoryAll.fxml");
    }

    protected void message(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message!");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
