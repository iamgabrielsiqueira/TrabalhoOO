package sample.model;

import javafx.collections.ObservableList;

public interface ExpenseDAO {
    void create(Expense expense) throws Exception;
    ObservableList<Expense> list(User user) throws Exception;
    void delete(Expense expense) throws Exception;
    void update(Expense expense, Expense j) throws Exception;
}
