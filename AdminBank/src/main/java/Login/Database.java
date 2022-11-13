package Login;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Database {
    private static final String JDBC_driver = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/bankdata";

    private static final String user = "postgres";
    private static final String password = "Ol2701Eg";

    private Statement statement;

    private Connection conn;

    public ResultSet getInfo(String sql_query){
        try {
            return statement.executeQuery(sql_query);
        } catch (Exception ex){
            System.out.println("Execute failed...");
            System.out.println(ex);
            return null;
        }
    }

    public int execute(String sql_query) {
        try {
            if (statement.execute(sql_query)){
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception ex){
            System.out.println("Execute failed...");
            System.out.println(ex);
            return 2;
        }
    }

    public void connect(){
        try {
            Class.forName(JDBC_driver).getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(DB_URL, user, password);
            statement = conn.createStatement();
        } catch (Exception ex){
            System.out.println("Conection failed...");
            System.out.println(ex);
        }
    }

}
