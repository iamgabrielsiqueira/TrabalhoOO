package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Subcategory {

    private int id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty idCategory;

    public Subcategory() {
        name = new SimpleStringProperty();
        idCategory = new SimpleIntegerProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getIdCategory() {
        return idCategory.get();
    }

    public SimpleIntegerProperty idCategoryProperty() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory.set(idCategory);
    }

    @Override
    public String toString() {
        return this.name.getValue();
    }
}