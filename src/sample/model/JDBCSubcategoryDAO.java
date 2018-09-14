package sample.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class JDBCSubcategoryDAO implements SubcategoryDAO {

    private static JDBCSubcategoryDAO instance;

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

    @Override
    public void delete(Subcategory subcategory) throws Exception {

    }

    @Override
    public void update(Subcategory subcategory) throws Exception {

    }
}
