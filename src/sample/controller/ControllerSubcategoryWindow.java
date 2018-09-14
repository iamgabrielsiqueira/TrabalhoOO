package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.model.Category;
import sample.model.JDBCCategoryDAO;
import sample.model.Subcategory;

public class ControllerSubcategoryWindow {

    @FXML
    private TextField tfSubcategoryName;

    @FXML
    private ComboBox<Category> cbCategory;

    private ObservableList<Category> categoryList;

    public Subcategory processResult() {

        String name = tfSubcategoryName.getText();

        if(name.isEmpty()) {
            return null;
        } else {
            Subcategory subcategory = new Subcategory();
            subcategory.setName(name);
            subcategory.setIdCategory(1);

            return subcategory;
        }

    }

    public void initialize() throws Exception {
        categoryList = JDBCCategoryDAO.getInstance().list(ControllerLoginWindow.user);
        cbCategory.setItems(categoryList);
    }

}
