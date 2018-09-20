package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class JDBCCategoryDAO implements CategoryDAO {

    private static JDBCCategoryDAO instance;
    private ObservableList<Category> list;

    private JDBCCategoryDAO(){
        list = FXCollections.observableArrayList();
    }

    public static JDBCCategoryDAO getInstance() {
        if(instance == null) {
            instance = new JDBCCategoryDAO();
        }
        return instance;
    }

    @Override
    public void create(Category category) throws Exception {
        Connection connection = ConnectionFactory.getConnection();

        String sql = "insert into category(name, idUser) values(?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, category.getName());
        preparedStatement.setInt(2, category.getIdUser());

        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    private Category loadCategory(ResultSet resultSet)throws SQLException{
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int idUser = resultSet.getInt("idUser");

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setIdUser(idUser);

        return category;
    }

    @Override
    public ObservableList<Category> list(User user) throws Exception {

        list.clear();

        try {
            Connection connection = ConnectionFactory.getConnection();
            String sql = "select * from category where idUser = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Category category = loadCategory(resultSet);
                list.add(category);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList<Category> listCb(User user) throws Exception {

        list.clear();

        Category categoryAll = new Category();
        categoryAll.setName("All");

        list.add(categoryAll);

        try {
            Connection connection = ConnectionFactory.getConnection();
            String sql = "select * from category where idUser = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Category category = loadCategory(resultSet);
                list.add(category);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void delete(Category category) throws Exception {

    }

    @Override
    public void update(Category category) throws Exception {

    }

    @Override
    public Category search(int id) throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "select * from category where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        Category category = loadCategory(resultSet);

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return category;
    }
}
