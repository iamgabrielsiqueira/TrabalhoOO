package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.model.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ControllerUpdateExpense {

    @FXML
    private ComboBox<Category> cbCategory;

    private ObservableList<Category> categoryList;

    @FXML
    private ComboBox<Subcategory> cbSubcategory;

    private ObservableList<Subcategory> subcategoryList;

    @FXML
    private DatePicker tfDate;

    @FXML
    private TextField tfCost;

    @FXML
    private CheckBox checkBox1;

    User user = ControllerLoginWindow.user;

    public void setDados(Expense expense) throws Exception {
        categoryList = FXCollections.observableArrayList(JDBCCategoryDAO.getInstance().listCb(user));
        categoryList.remove(0);
        cbCategory.setItems(categoryList);

        Date date = expense.getDate();
        LocalDate d1 = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        tfDate.setValue(d1);
        tfCost.setText(String.valueOf(expense.getCost()));

        if(expense.getStatus() == 1) {
            checkBox1.setSelected(true);
        }

        cbCategory.getSelectionModel().select(expense.getCategory());
        cbSubcategory.getSelectionModel().select(expense.getSubcategory());

    }

    public Expense processResult() {
        java.sql.Date data = java.sql.Date.valueOf(tfDate.getValue());
        Double cost = Double.valueOf(tfCost.getText());
        Category category = cbCategory.getSelectionModel().getSelectedItem();
        Subcategory subcategory = cbSubcategory.getSelectionModel().getSelectedItem();

        Expense expense = new Expense();
        expense.setDate(data);
        expense.setCost(cost);
        expense.setCategory(category);
        expense.setSubcategory(subcategory);
        expense.setIdUser(user.getId());

        if(checkBox1.isSelected()) {
            expense.setStatus(1);
        } else {
            expense.setStatus(0);
        }

        System.out.println(data);

        return expense;

    }

    @FXML
    public void loadSubcategory() throws Exception {
        Category c1 = cbCategory.getSelectionModel().getSelectedItem();
        cbSubcategory.getItems().clear();

        subcategoryList = JDBCSubcategoryDAO.getInstance().list(user, c1);

        cbSubcategory.setItems(subcategoryList);
    }
}
