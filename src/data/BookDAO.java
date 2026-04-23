package data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    //Inserts a new book into the database
    public boolean addBook(String title, String author, String isbn){
        String sql= "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            //Set values for the query
            ps.setString(1,title);
            ps.setString(2,author);
            ps.setString(3,isbn);

            ps.executeUpdate();
            System.out.println("Book added succesfully");
            return true;
        } catch (Exception e){
            System.out.println("Book addition failed");
            e.printStackTrace();
            return false;
        }
    }

    //Retrieves all books from the database
    public List<String[]> getAllBooks(){
        List<String[]> bookList = new ArrayList<>();
        String sql="SELECT * FROM books";
        try(Connection conn=DBConnection.getConnection();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql)){

            //Loop through all records
            while(rs.next()) {
                String[] book= new String[5];

                //Map database columns to array
                book[0] = String.valueOf(rs.getInt("id"));
                book[1] = rs.getString("title");
                book[2] = rs.getString("author");
                book[3] = rs.getString("isbn");
                book[4] = rs.getString("status");
                bookList.add(book);

            }
        } catch (Exception e){
                e.printStackTrace();
            }
        return bookList;

        }
    }

