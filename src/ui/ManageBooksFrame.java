package ui;
import data.BookDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.JOptionPane;
import service.BookService;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
public class ManageBooksFrame extends JFrame {
    private JTextField txtSearch;
    private JButton btnSearch;
    // UI components for the screen
    private JTextField txtTitle, txtAuthor, txtIsbn;
    private JButton btnAdd, btnUpdate, btnDelete, btnBack;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private BookService service= new BookService();
    public ManageBooksFrame() {
        // Set the window title
        setTitle("Smart Library - Manage Books");
        // Define the width and height of the frame
        setSize(850, 650);
        // Close the application when clicking the exit button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center the window on the screen
        setLocationRelativeTo(null);
        // Divide the screen into North, South, and Center areas with 10px gaps
        setLayout(new BorderLayout(10, 10));
        // Set the main background color to white
        getContentPane().setBackground(Color.WHITE);
        // Call methods to build the interface
        buildTopPanel();    // Top panel for input fields
        buildCenterPanel(); // Center panel for the data table
        loadBooks();
    }
    private void buildTopPanel() {
        // Create a panel for the top section
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(5, 2, 10, 10));
        // Add padding around the panel
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.setBackground(Color.WHITE);
        // Input labels and text fields for book details
        topPanel.add(new JLabel("Book Title:"));
        txtTitle = new JTextField();
        topPanel.add(txtTitle);
        topPanel.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        topPanel.add(txtAuthor);
        topPanel.add(new JLabel("ISBN Number:"));
        txtIsbn = new JTextField();
        topPanel.add(txtIsbn);
        topPanel.add(new JLabel("Search by Title:"));
        txtSearch = new JTextField();
        topPanel.add(txtSearch);
        btnSearch = new JButton("Search");
        topPanel.add(btnSearch);
        // A small sub-panel to align action buttons horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        // Create buttons for managing books
        btnAdd = new JButton("Add Book");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("← Back to Dashboard");
        // Add action buttons to the sub-panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        // Add the back button and the sub-panel to the top panel
        topPanel.add(btnBack);
        topPanel.add(new JLabel(""));
        topPanel.add(buttonPanel);
        topPanel.add(new JLabel(""));
        // Add this top panel to the North (top) area of the screen
        add(topPanel, BorderLayout.NORTH);
        //Handle add book button click event
        //Gets user input, inserts into database,refreshes table and clears fields
        btnAdd.addActionListener(e ->{
            String title=txtTitle.getText();
            String author=txtAuthor.getText();
            String isbn=txtIsbn.getText();
            //BookDAO dao=new BookDAO();
            //boolean success=dao.addBook(title,author,isbn);
            boolean success = service.addBook(title, author, isbn);
            if(success){
                JOptionPane.showMessageDialog(this,"Book added successfully");
                loadBooks();
                txtTitle.setText("");
                txtAuthor.setText("");
                txtIsbn.setText("");
            } else{
                JOptionPane.showMessageDialog(this,"Failed to add book");
            }
        });
        //Handle Delete button click event
        //Deletes selected book from database and refreshes the table
        btnDelete.addActionListener(e->{
            //Get selected row from table
            int selectedRow=bookTable.getSelectedRow();
            if(selectedRow==-1){
                JOptionPane.showMessageDialog(this,"Please select s book to delete");
                return;
            }
            //Ask user for confirmation before deleting
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            //Convert view index to model index (important for correct row)
            int modelRow = bookTable.convertRowIndexToModel(selectedRow);
            //Get book ID from selected row
            int id=Integer.parseInt(tableModel.getValueAt(modelRow,0).toString());
            //Call DAO to delete book from database
            BookDAO dao=new BookDAO();
            boolean success=dao.deleteBook(id);
            if(success) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully");
                //Refresh table data after deletion
                loadBooks();
            }else{
                JOptionPane.showMessageDialog(this,"Failed to delete book");
            }
        });
        //Handle Update button click event
        //Updates selected book information in database and refreshes the table
        btnUpdate.addActionListener(e->{
            //Get selected row from table
            int selectedRow=bookTable.getSelectedRow();
            //Check if no row is selected
            if(selectedRow==-1){
                JOptionPane.showMessageDialog(this,"Please select a book to update");
                return;
            }
            //Convert view index to model index (important for correct row)
            int modelRow=bookTable.convertRowIndexToModel(selectedRow);
            //Get book ID from selected row
            int id=Integer.parseInt((tableModel.getValueAt(modelRow,0).toString()));
            //Get updated values from input field
            String title=txtTitle.getText();
            String author=txtAuthor.getText();
            String isbn=txtIsbn.getText();
            //Call DAO to update book in database
            BookDAO dao = new BookDAO();
            boolean success = dao.updateBook(id, title, author, isbn);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book updated successfully");
                //Refresh table data after update
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book");
            }
        });
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText();
            DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            bookTable.setRowSorter(sorter);
            if(keyword.trim().isEmpty()){
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1));
            }
        });
    }
    private void buildCenterPanel() {
        // Column headers for the table
        String[] columns = {"ID", "Book Title", "Author", "ISBN", "Status"};
        // Table model to hold the data
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        // Put the table inside a scrollable pane to handle many rows
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        // Add the scrollable pane to the Center area of the screen
        add(scrollPane, BorderLayout.CENTER);
        // Return to the Main Menu when the back button is clicked
        btnBack.addActionListener(e -> {
            MainFrame dashboard = new MainFrame();
            dashboard.setVisible(true);
            // Close the current window
            this.dispose();
        });
        bookTable.getSelectionModel().addListSelectionListener(e ->{
            int selectedRow=bookTable.getSelectedRow();
            if(selectedRow!=-1){
                int modelRow= bookTable.convertRowIndexToModel(selectedRow);
                txtTitle.setText(tableModel.getValueAt(modelRow,1).toString());
                txtAuthor.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtIsbn.setText(tableModel.getValueAt(modelRow, 3).toString());
            }
        });
    }
    public static void main(String[] args) {
        // Start the application and show the frame
        new ManageBooksFrame().setVisible(true);
    }
    //Loads all books from database and displays them in the table
    public void loadBooks(){
        BookDAO dao= new BookDAO();
        tableModel.setRowCount(0);
        for(String[]book:dao.getAllBooks()){
            tableModel.addRow(book);
        }
    }
}