package data;
//Import classes for database connection
import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {
    //This URL defines the SQLite database file.
    //If the file does not exist, SQLite will create it automatically
    private static final String URL="jdbc:sqlite:smart_library.db";
    public static Connection getConnection(){
        try{
            //try to establish a connection to the database using the URL
            return DriverManager.getConnection(URL);
        } catch(Exception e){
            // If an error occurs, print the error details to the console
            e.printStackTrace();
            //return null if connection fails
            return null;
        }
    }
}
