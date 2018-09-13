package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.model.Category;

public class ControllerCategoryWindow {

    @FXML
    private TextField tfCategoryName;

    public Category processResult() {

        String name = tfCategoryName.getText();

        if(name.isEmpty()) {
            return null;
        } else {
            Category category = new Category();
            category.setName(name);
            category.setIdUser(ControllerLoginWindow.user.getId());

            return category;
        }

    }
}
