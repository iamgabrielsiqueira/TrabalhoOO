package sample.model;

public interface UserDAO {
    void create(User user) throws Exception;
    void delete(User user) throws Exception;
    void update(User user) throws Exception;
    User search(String username, String password) throws Exception;
}
