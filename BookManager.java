package FullStackProject;

import java.sql.*;
import java.util.Scanner;

public class BookManager {

    private static Connection connection;

    // Static block to initialize the database connection
    static {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/LibraryDB", "root", "akash2003");
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    // Method to add a book
    public void addBook(int id, String title, String author, int year) {
        String query = "INSERT INTO books (id, title, author, year) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setInt(4, year);
            statement.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Method to update a book's details
    public void updateBook(int id, String newTitle, String newAuthor, int newYear) {
        String query = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newTitle);
            statement.setString(2, newAuthor);
            statement.setInt(3, newYear);
            statement.setInt(4, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book updated successfully!");
            } else {
                System.out.println("No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to delete a book
    public void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Book deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all books
 // Method to view all books with dynamic serial numbers
    public void viewAllBooks() {
        String query = "SELECT * FROM books ORDER BY id ASC"; // Order by ID for consistent sorting
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            int serialNumber = 1; // Initialize serial number
            System.out.printf("%-5s %-10s %-20s %-20s %-5s%n", "S.No", "ID", "Title", "Author", "Year");
            System.out.println("---------------------------------------------------------------");

            while (resultSet.next()) {
                System.out.printf("%-5d %-10d %-20s %-20s %-5d%n",
                        serialNumber++,
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


 // Search books by title, author, or year
    private static void searchBooks(Connection connection, Scanner scanner) {
        try {
            System.out.println("Choose search criteria:");
            System.out.println("1. Search by Title");
            System.out.println("2. Search by Author");
            System.out.println("3. Search by Year");
            System.out.print("Enter your choice: ");

            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String query = "";
            PreparedStatement preparedStatement = null;

            switch (searchChoice) {
                case 1: // Search by Title
                    System.out.print("Enter title to search: ");
                    String title = scanner.nextLine();
                    query = "SELECT * FROM books WHERE title LIKE ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, "%" + title + "%");
                    break;

                case 2: // Search by Author
                    System.out.print("Enter author to search: ");
                    String author = scanner.nextLine();
                    query = "SELECT * FROM books WHERE author LIKE ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, "%" + author + "%");
                    break;

                case 3: // Search by Year
                    System.out.print("Enter year to search: ");
                    int year = scanner.nextInt();
                    query = "SELECT * FROM books WHERE year = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, year);
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    return; // Exit the method if an invalid choice is made
            }

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the results
            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                int id = resultSet.getInt("id");
                String resultTitle = resultSet.getString("title");
                String resultAuthor = resultSet.getString("author");
                int resultYear = resultSet.getInt("year");
                System.out.println("ID: " + id + ", Title: " + resultTitle + ", Author: " + resultAuthor + ", Year: " + resultYear);
            }

            if (!hasResults) {
                System.out.println("No records found matching your criteria.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // Main method to test functionality
    public static void main(String[] args) {
        BookManager manager = new BookManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Book Manager ---");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Books");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Year: ");
                    int year = scanner.nextInt();
                    manager.addBook(id, title, author, year);
                    break;

                case 2:
                	System.out.print("Enter ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new Title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new Author: ");
                    String newAuthor = scanner.nextLine();
                    System.out.print("Enter new Year: ");
                    int newYear = scanner.nextInt();
                    manager.updateBook(updateId, newTitle, newAuthor, newYear);
                    break;

                case 3:
                    System.out.print("Enter ID to delete: ");
                    int deleteId = scanner.nextInt();
                    manager.deleteBook(deleteId);
                    break;

                case 4:
                    manager.viewAllBooks();
                    break;
       
                case 5:
                	BookManager.searchBooks(connection, scanner);
                	break;
                	
                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
