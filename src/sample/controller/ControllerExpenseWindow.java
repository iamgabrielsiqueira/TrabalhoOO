package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import sample.model.Category;
import sample.model.JDBCCategoryDAO;
import sample.model.Subcategory;

public class ControllerExpenseWindow {

    @FXML
    private ComboBox<Category> cbCategory;

    private ObservableList<Category> categoryList;

    @FXML
    private ComboBox<Subcategory> cbSubcategory;

    private ObservableList<Subcategory> subcategoryList;

    public void initialize() throws Exception {
        categoryList = JDBCCategoryDAO.getInstance().list(ControllerLoginWindow.user);
        cbCategory.setItems(categoryList);


/*        cbSubcategory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    subcategoryList = JDBCSubcategoryDAO.getInstance().list(ControllerLoginWindow.user, cbCategory.getSelectionModel().getSelectedItem());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cbSubcategory.setItems(subcategoryList);
            }
        });*/

    }

}
