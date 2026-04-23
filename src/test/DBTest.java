package test;
import java.sql.Connection;
import data.DBConnection;
import data.DatabaseInitializer;
import data.BookDAO;

public class DBTest {

    public static void main(String[] args) {

        //Test database connection
        Connection conn= DBConnection.getConnection();

        if(conn!=null){
            System.out.println("Connected Successfully");
            //Create required database tables
            DatabaseInitializer.createTables();

            //Create bookDao object
            BookDAO dao= new BookDAO();

            //Insert sample books for testing
            //dao.addBook("Java Programming", "John Doe","581");
            //dao.addBook("Data Structures", "Jane Smith", "582");

            //Display all books from database
            System.out.println("-----BOOK LIST-----");
            for(String[]book:dao.getAllBooks()){
                System.out.println(
                                book[0]+"|"+
                                book[1]+"|"+
                                book[2]+"|"+
                                book[3]+"|"+
                                book[4]+"|"
                );
            }
        } else{
            System.out.println("Connection Failed");
        }
    }
}
