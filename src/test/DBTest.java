package test;
import java.sql.Connection;
import data.DBConnection;
public class DBTest {

    public static void main(String[] args) {

        //test database connection
        Connection conn= DBConnection.getConnection();

        if(conn!=null){
            System.out.println("Connected Succesfully");
        } else{
            System.out.println("Connection Failed");
        }
    }
}
