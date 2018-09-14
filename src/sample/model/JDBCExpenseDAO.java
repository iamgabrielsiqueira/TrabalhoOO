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

        String sql = "insert into expense(date, cost, idCategory, idSubcategory, idUser) " +
                "values(?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setDate(1, (Date) expense.getDate());
        preparedStatement.setDouble(2, expense.getCost());
        preparedStatement.setInt(3, expense.getIdCategory());
        preparedStatement.setInt(4, expense.getIdSubcategory());
        preparedStatement.setInt(5, expense.getIdUser());

        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    private Expense loadExpense(ResultSet resultSet)throws SQLException{
        int id = resultSet.getInt("id");
        Date date = resultSet.getDate("date");
        Double cost = resultSet.getDouble("cost");
        int idCategory = resultSet.getInt("idCategory");
        int idSubcategory = resultSet.getInt("idSubcategory");
        int idUser = resultSet.getInt("idUser");

        Expense expense = new Expense();
        expense.setId(id);
        expense.setDate(date);
        expense.setCost(cost);
        expense.setIdCategory(idCategory);
        expense.setIdSubcategory(idSubcategory);
        expense.setIdUser(idUser);

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


    @Override
    public void delete(Expense expense) throws Exception {

    }

    @Override
    public void update(Expense expense) throws Exception {

    }
}