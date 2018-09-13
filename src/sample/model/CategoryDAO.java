package sample.model;

public interface CategoryDAO {
    void create(Category category) throws Exception;
    void delete(Category category) throws Exception;
    void update(Category category) throws Exception;
}
