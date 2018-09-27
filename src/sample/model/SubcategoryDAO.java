package sample.model;

import javafx.collections.ObservableList;

public interface SubcategoryDAO {
    void create(Subcategory subcategory) throws Exception;
    ObservableList<Subcategory> list(User user, Category category) throws Exception;
    void delete(Subcategory subcategory) throws Exception;
    void update(Subcategory subcategory, Subcategory j) throws Exception;
    Subcategory search(int id) throws Exception;
}
