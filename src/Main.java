import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main class to run the Project Management System application.
 */
public class Main {
  
  /**
   * Main method to initialize the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    // Initialize database connection
    Connection connection = null;
    try {
      // Connect to database
    	connection = Project.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true",
                "otheruser", "yusufrazak10");


      // Create an instance of ProjectOperations to handle database operations
      ProjectOperations operations = new ProjectOperations(connection);

      // Scanner for user input
      try (Scanner scanner = new Scanner(System.in)) {
        // Control variable for the menu loop
        boolean running = true;
        while (running) {
          // Display the menu options to the user
          System.out.println("\nMenu:");
          System.out.println("\nSelect an option: ");
          System.out.println("1. Capture and add information about new projects");
          System.out.println("2. Update existing projects");
          System.out.println("3. Delete projects and associated data");
          System.out.println("4. Finalize existing projects");
          System.out.println("5. View incomplete projects");
          System.out.println("6. Find past due date projects");
          System.out.println("7. Find project by number or name");
          System.out.println("8. Exit");

          // Get a valid integer input from the user
          int choice = operations.getValidInt(scanner);

          // Handle the user's menu selection
          switch (choice) {
            case 1:
              operations.captureInfoAndAdd(scanner);
              break;
            case 2:
              operations.captureAndUpdate(scanner);
              break;
            case 3:
              operations.captureAndDelete(scanner);
              break;
            case 4:
              operations.finaliseProject(scanner);
              break;
            case 5:
              operations.incompleteProjects();
              break;
            case 6:
              operations.pastDueDate(scanner);
              break;
            case 7:
              operations.findByNumberOrName(scanner);
              break;
            case 8:
              // Exit the loop and terminate program.
              running = false;
              break;
            default:
              System.out.println("Invalid option. Please try again.");
          }
        }
      }
    } catch (SQLException e) {
      // Handle database connection errors
      System.err.println("Database error: " + e.getMessage());
    } finally {
      // Ensure the database connection is closed
      if (connection != null) {
        try {
          // Handle database connection errors
          connection.close();
        } catch (SQLException e) {
          // Handle any errors that may occur while closing the connection
          System.err.println("Error closing connection: " + e.getMessage());
        }
      }
    }
  }
}

