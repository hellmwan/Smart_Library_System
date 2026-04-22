package data;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    //Creates all required database tables if they do not exist
    public static void createTables(){
        try (Connection conn=DBConnection.getConnection()){

            //Ensure connection is available
            if(conn==null){
                System.out.println("Connection is NULL");
                return;
            }
            try(Statement stmt=conn.createStatement()) {

                //Enable foreign key constraints in SQLite
                stmt.execute("PRAGMA foreign_keys = ON");

                String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "author TEXT NOT NULL, " +
                        "isbn TEXT UNIQUE, " +
                        "status TEXT DEFAULT 'Available'" +
                        ");";
                stmt.execute(booksTable);
                System.out.println("Books table created");

                String membersTable = "CREATE TABLE IF NOT EXISTS members (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "phone TEXT, " +
                        "address TEXT, " + //email->address
                        "join_date TEXT " +
                        ");";
                stmt.execute(membersTable);
                System.out.println("Members table created");

                String loansTable = "CREATE TABLE IF NOT EXISTS loans (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "member_id INTEGER, " + //renamed for JOIN operations
                        "book_id INTEGER, " + //renamed for JOIN operations
                        "issue_date TEXT, " +
                        "due_date TEXT, " + //not used in UI yet
                        "return_date TEXT, " + //not used in UI yet
                        "status TEXT DEFAULT 'Active', " +
                        "FOREIGN KEY(member_id) REFERENCES members(id)," +
                        "FOREIGN KEY(book_id) REFERENCES books(id)" +
                        ");";
                stmt.execute(loansTable);
                System.out.println("Loans table created");
            }

        } catch(Exception e ){
            System.out.println("Table creation failed");
            e.printStackTrace();
        }
    }
}
