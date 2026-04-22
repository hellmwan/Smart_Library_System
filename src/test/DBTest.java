package test;
import java.sql.Connection;
import data.DBConnection;
import data.DatabaseInitializer;

public class DBTest {

    public static void main(String[] args) {

        //test database connection
        Connection conn= DBConnection.getConnection();

        if(conn!=null){
            System.out.println("Connected Successfully");
            //Create required database tables
            DatabaseInitializer.createTables();
        } else{
            System.out.println("Connection Failed");
        }
    }
}
