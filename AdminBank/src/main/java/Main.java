import Login.Admin;
import Login.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args){
        Admin admin = new Admin();
        admin.ActivateAdmin();
    }
}
