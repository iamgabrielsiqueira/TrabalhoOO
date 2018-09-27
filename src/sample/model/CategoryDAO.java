package sample.model;

import javafx.collections.ObservableList;

public interface CategoryDAO {
    void create(Category category) throws Exception;
    ObservableList<Category> list(User user) throws Exception;
    void delete(Category category) throws Exception;
    void update(Category category, Category j) throws Exception;
    Category search(int id) throws Exception;
}
