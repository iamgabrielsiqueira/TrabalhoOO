package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.model.Subcategory;

public class ControllerUpdateSubcategoryWindow {

    @FXML
    private TextField tfName;

    public void setDados(Subcategory subcategory) {
        tfName.setText(subcategory.getName());
    }

    public Subcategory processResult() {

        if(tfName.getText() != null) {
            Subcategory subcategory = new Subcategory();
            subcategory.setName(tfName.getText());

            return subcategory;
        } else {
            return null;
        }

    }
}
