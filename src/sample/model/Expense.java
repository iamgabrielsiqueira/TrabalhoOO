package sample.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Date;

public class Expense {

    private int id;
    private Date date;
    private SimpleDoubleProperty cost;
    private Category category;
    private Subcategory subcategory;
    private SimpleIntegerProperty idUser;
    private SimpleIntegerProperty status;

    public Expense() {
        cost = new SimpleDoubleProperty();
        category = new Category();
        subcategory = new Subcategory();
        idUser = new SimpleIntegerProperty();
        status = new SimpleIntegerProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost.get();
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }
}