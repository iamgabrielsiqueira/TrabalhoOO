package sample.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class JDBCCategoryDAO implements CategoryDAO {

    private static JDBCCategoryDAO instance;

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

    @Override
    public void delete(Category category) throws Exception {

    }

    @Override
    public void update(Category category) throws Exception {

    }
}
