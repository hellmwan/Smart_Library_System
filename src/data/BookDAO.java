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

        //Deletes a book from database using its ID
        public boolean deleteBook(int id){

        String sql="DELETE FROM books WHERE id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            //Set ID parameter for WHERE condition
            ps.setInt(1,id);

            //Execute delete operation and get affected row count
            int affected=ps.executeUpdate();
            //Return true if at least one row is deleted
            return affected>0;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //Updates book information in database using its ID
    public boolean updateBook(int id, String title, String author, String isbn) {
        String sql = "UPDATE books SET title=?, author=?, isbn=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            //Set new values
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, isbn);
            ps.setInt(4, id);
            //Execute update and check affected rows
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

