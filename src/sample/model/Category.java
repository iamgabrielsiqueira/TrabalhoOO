package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Category {

    private int id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty idUser;

    public Category() {
        name = new SimpleStringProperty();
        idUser = new SimpleIntegerProperty();
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

    public int getIdUser() {
        return idUser.get();
    }

    public SimpleIntegerProperty idUserProperty() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser.set(idUser);
    }

    @Override
    public String toString() {
        return this.name.getValue();
    }
}
