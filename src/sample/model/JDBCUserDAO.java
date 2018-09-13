package sample.model;

import java.sql.*;

public class JDBCUserDAO implements UserDAO {

    private static JDBCUserDAO instance;

    public static JDBCUserDAO getInstance() {
        if(instance == null) {
            instance = new JDBCUserDAO();
        }
        return instance;
    }

    @Override
    public void create(User user) throws Exception {
        Connection connection = ConnectionFactory.getConnection();

        String sql = "insert into user(name, login, password) values(?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getLogin());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    @Override
    public void delete(User user) throws Exception {

    }

    @Override
    public void update(User user) throws Exception {

    }

    private User getUser(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");

        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setLogin(login);
        user.setPassword(password);

        return user;
    }

    @Override
    public User search(String username, String password) throws Exception {

        Connection connection = ConnectionFactory.getConnection();
        String sql = "select * from user where login = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            User user = getUser(resultSet);

            resultSet.close();
            statement.close();
            connection.close();

            if(user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
