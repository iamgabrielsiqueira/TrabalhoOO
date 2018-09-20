package sample.controller;

import javafx.collections.FXCollections;
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
        Category category = cbCategory.getSelectionModel().getSelectedItem();

        if(name.isEmpty() || category == null) {
            return null;
        } else {
            Subcategory subcategory = new Subcategory();
            subcategory.setName(name);
            subcategory.setIdCategory(category.getId());

            return subcategory;
        }

    }

    public void initialize() throws Exception {
        categoryList = FXCollections.observableArrayList(JDBCCategoryDAO.getInstance().listCb(ControllerLoginWindow.user));
        categoryList.remove(0);
        cbCategory.setItems(categoryList);
    }

}
