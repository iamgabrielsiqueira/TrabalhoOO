package sample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection[] pool;
    private static String CONNECTION_STR = "jdbc:sqlite:bank.sqlite";
    private static int MAX_CONNECTIONS=5;

    static {
        pool = new Connection[5];
    }

    public static Connection getConnection() throws SQLException {

        for(int i=0;i<pool.length;i++){
            if((pool[i]==null) || (pool[i].isClosed())){
                pool[i] = DriverManager.getConnection(CONNECTION_STR);
                return pool[i];
            }
        }

        throw new SQLException("Muitas conexões abertas!!!");

    }
}
