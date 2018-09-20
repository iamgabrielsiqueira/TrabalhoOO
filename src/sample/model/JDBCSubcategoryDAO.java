package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCSubcategoryDAO implements SubcategoryDAO {

    private static JDBCSubcategoryDAO instance;
    private ObservableList<Subcategory> list;

    private JDBCSubcategoryDAO(){
        list = FXCollections.observableArrayList();
    }

    public static JDBCSubcategoryDAO getInstance() {
        if(instance == null) {
            instance = new JDBCSubcategoryDAO();
        }
        return instance;
    }

    @Override
    public void create(Subcategory subcategory) throws Exception {
        Connection connection = ConnectionFactory.getConnection();

        String sql = "insert into subcategory(name, idCategory) values(?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, subcategory.getName());
        preparedStatement.setInt(2, subcategory.getIdCategory());

        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    private Subcategory loadSubcategory(ResultSet resultSet)throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int idCategory = resultSet.getInt("idCategory");

        Subcategory subcategory = new Subcategory();
        subcategory.setId(id);
        subcategory.setName(name);
        subcategory.setIdCategory(idCategory);

        return subcategory;
    }

    @Override
    public ObservableList<Subcategory> list(User user, Category category) throws Exception {

        list.clear();

        try {
            Connection connection = ConnectionFactory.getConnection();
            String sql = "select * from subcategory where idCategory = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(1, category.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Subcategory subcategory = loadSubcategory(resultSet);
                list.add(subcategory);
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
    public void delete(Subcategory subcategory) throws Exception {

    }

    @Override
    public void update(Subcategory subcategory) throws Exception {

    }

    @Override
    public Subcategory search(int id) throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "select * from subcategory where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        Subcategory subcategory = loadSubcategory(resultSet);

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return subcategory;
    }
}