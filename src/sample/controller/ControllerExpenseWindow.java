package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.model.*;
import java.sql.Date;

public class ControllerExpenseWindow {

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

    User user = ControllerLoginWindow.user;

    public Expense processResult() {

        if(tfCost.getText().isEmpty()) {
            return null;
        } else {
            Date data = Date.valueOf(tfDate.getValue());
            Double cost = Double.valueOf(tfCost.getText());
            Category category = cbCategory.getSelectionModel().getSelectedItem();
            Subcategory subcategory = cbSubcategory.getSelectionModel().getSelectedItem();

            Expense expense = new Expense();
            expense.setDate(data);
            expense.setCost(cost);
            expense.setCategory(category);
            expense.setSubcategory(subcategory);
            expense.setIdUser(user.getId());

            System.out.println(data);

            return expense;
        }

    }

    @FXML
    public void loadSubcategory() throws Exception {
        Category c1 = cbCategory.getSelectionModel().getSelectedItem();
        subcategoryList = JDBCSubcategoryDAO.getInstance().list(user, c1);

        cbSubcategory.setItems(subcategoryList);
    }

    public void initialize() throws Exception {
        categoryList = FXCollections.observableArrayList(JDBCCategoryDAO.getInstance().listCb(ControllerLoginWindow.user));
        categoryList.remove(0);
        cbCategory.setItems(categoryList);
    }

}
