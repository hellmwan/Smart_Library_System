package service;
import data.BookDAO;

public class BookService {

    private BookDAO dao = new BookDAO();

    public boolean addBook(String title, String author, String isbn){
        if(title.isEmpty() || author.isEmpty() || isbn.isEmpty()){
            return false;
        }
        return dao.addBook(title, author, isbn);
    }
}