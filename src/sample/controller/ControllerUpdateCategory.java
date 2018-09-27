package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.model.Category;

public class ControllerUpdateCategory {

    @FXML
    private TextField tfName;

    public void setDados(Category category) {
        tfName.setText(category.getName());
    }

    public Category processResult() {

        if(tfName.getText() != null) {
            Category category = new Category();
            category.setName(tfName.getText());

            return category;
        } else {
            return null;
        }

    }
}
