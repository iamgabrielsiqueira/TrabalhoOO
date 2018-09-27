package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.model.*;
import java.sql.Date;

public class ControllerSetDone {

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
    private Text lbCategory;

    @FXML
    private Text lbCost;

    @FXML
    private Text lbSubcategory;

    User user = ControllerLoginWindow.user;

    public void setDados(Expense expense) throws Exception {
        lbCost.setText(String.valueOf(expense.getCost()));
        lbCategory.setText("Category: " + expense.getCategory().getName());
        lbSubcategory.setText("Subcategory: " + expense.getSubcategory().getName());
    }

    public void setExpenseAsDone() {

    }

    public boolean processResult() {

        boolean ok = true;

        return ok;
    }

}
