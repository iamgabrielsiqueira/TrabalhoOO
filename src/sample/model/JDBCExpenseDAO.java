package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class JDBCExpenseDAO implements ExpenseDAO {

    private static JDBCExpenseDAO instance;
    private ObservableList<Expense> list;

    private JDBCExpenseDAO(){
        list = FXCollections.observableArrayList();
    }

    public static JDBCExpenseDAO getInstance() {
        if(instance == null) {
            instance = new JDBCExpenseDAO();
        }
        return instance;
    }

    @Override
    public void create(Expense expense) throws Exception {
        Connection connection = ConnectionFactory.getConnection();

        String sql = "insert into expense(date, cost, idCategory, idSubcategory, idUser, status) " +
                "values(?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setDate(1, (Date) expense.getDate());
        preparedStatement.setDouble(2, expense.getCost());
        preparedStatement.setInt(3, expense.getCategory().getId());
        preparedStatement.setInt(4, expense.getSubcategory().getId());
        preparedStatement.setInt(5, expense.getIdUser());
        preparedStatement.setInt(6, expense.getStatus());
        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    private Expense loadExpense(ResultSet resultSet) throws SQLException, Exception {
        int id = resultSet.getInt("id");
        Date date = resultSet.getDate("date");
        Double cost = resultSet.getDouble("cost");
        int idCategory = resultSet.getInt("idCategory");
        int idSubcategory = resultSet.getInt("idSubcategory");
        int idUser = resultSet.getInt("idUser");
        int status = resultSet.getInt("status");

        Category category = JDBCCategoryDAO.getInstance().search(idCategory);
        Subcategory subcategory = JDBCSubcategoryDAO.getInstance().search(idSubcategory);

        Expense expense = new Expense();
        expense.setId(id);
        expense.setDate(date);
        expense.setCost(cost);
        expense.setCategory(category);
        expense.setSubcategory(subcategory);
        expense.setIdUser(idUser);
        expense.setStatus(status);

        return expense;
    }

    @Override
    public ObservableList<Expense> list(User user) throws Exception {

        list.clear();

        try {
            Connection connection = ConnectionFactory.getConnection();
            String sql = "select * from expense where idUser = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Expense expense = loadExpense(resultSet);
                list.add(expense);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList<Expense> listCategory(User user, Category category) throws Exception {

        list.clear();

        try {
            Connection connection = ConnectionFactory.getConnection();
            String sql = "select * from expense where idUser = ? and idCategory = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, category.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Expense expense = loadExpense(resultSet);
                list.add(expense);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public Double getTotalExpense(User user) throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "SELECT SUM(cost) AS total FROM expense where expense.idUser = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        Double total = resultSet.getDouble("total");


        resultSet.close();
        preparedStatement.close();
        connection.close();

        return total;
    }

    public Double getTotalExpenseCategory(User user, Category category) throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "SELECT SUM(cost) AS total FROM expense where expense.idUser = ? " +
                "and expense.idCategory = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setInt(2, category.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        Double total = resultSet.getDouble("total");


        resultSet.close();
        preparedStatement.close();
        connection.close();

        return total;
    }

    @Override
    public void delete(Expense expense) throws Exception {
        String sql = "delete from expense where id=?";

        Connection c = ConnectionFactory.getConnection();
        PreparedStatement statement = c.prepareStatement(sql);

        statement.setInt(1, expense.getId());

        statement.execute();
        statement.close();
        c.close();
    }

    @Override
    public void update(Expense expense, Expense j) throws Exception {
        String sql = "update expense set date=?, cost=?, idCategory=?, idSubcategory=?, " +
                "status=? where id=?";

        Connection c = ConnectionFactory.getConnection();
        PreparedStatement statement = c.prepareStatement(sql);

        Date newDate = (Date) j.getDate();

        statement.setDate(1, newDate);
        statement.setDouble(2, j.getCost());
        statement.setInt(3, j.getCategory().getId());
        statement.setInt(4, j.getSubcategory().getId());
        statement.setInt(5, j.getStatus());
        statement.setInt(6, expense.getId());

        statement.execute();
        statement.close();
        c.close();
    }

    public void updateStatus(Expense expense) throws Exception {

        String sql = "update expense set status=? where id=?";

        Connection c = ConnectionFactory.getConnection();
        PreparedStatement statement = c.prepareStatement(sql);

        statement.setInt(1, 1);
        statement.setInt(2, expense.getId());

        statement.execute();
        statement.close();
        c.close();

    }
}
