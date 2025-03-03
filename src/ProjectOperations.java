import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Class responsible for handling project operations, including 
 * executing updates, reading data from the database, and managing 
 * customer, architect, engineer, and project data.
 */
public class ProjectOperations {
  
  private Connection connection;

  /**
   * Constructor to initialize the connection.
   *
   * @param connection the database connection to be used.
   */
  public ProjectOperations(Connection connection) {
    this.connection = connection;
  }

  /**
   * Execute an update operation (INSERT, UPDATE, DELETE) using a 
   * prepared statement.
   *
   * @param preparedStatement the prepared statement to execute.
   */
  public void executeUpdate(PreparedStatement preparedStatement) {
    try {
      int rowsAffected = preparedStatement.executeUpdate();
      System.out.println("Query complete, " + rowsAffected + " rows affected.");
    } catch (SQLException e) {
      System.err.println("SQL error: " + e.getMessage());
    }
  }

  /**
   * Method to read and display people and projects from the database.
   */
  public void readPeopleAndProjects() {
    // SQL statement to select all the data.
    String sqlQuery = """
        SELECT
          Project.ProjectID,
          Project.ProjectName,
          Project.BuildingType,
          Project.PhysicalAddress AS ProjectAddress,
          Project.ERFNumber,
          Project.TotalFee,
          Project.TotalPaid,
          Project.Deadline,
          Project.EngineerID,
          Project.ArchitectID,
          Project.CustomerID,
          Project.Finalized,
          Project.CompletionDate,
          Customer.Name AS CustomerName,
          Customer.PhoneNumber AS CustomerPhone,
          Customer.Email AS CustomerEmail,
          Customer.PhysicalAddress AS CustomerAddress,
          Architect.Name AS ArchitectName,
          Architect.PhoneNumber AS ArchitectPhone,
          Architect.Email AS ArchitectEmail,
          Architect.PhysicalAddress AS ArchitectAddress,
          Engineer.Name AS EngineerName,
          Engineer.PhoneNumber AS EngineerPhone,
          Engineer.Email AS EngineerEmail,
          Engineer.PhysicalAddress AS EngineerAddress
        FROM Project
        INNER JOIN Customer ON Project.CustomerID = Customer.CustomerID
        INNER JOIN Architect ON Project.ArchitectID = Architect.ArchitectID
        INNER JOIN Engineer ON Project.EngineerID = Engineer.EngineerID;
        """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
         ResultSet results = preparedStatement.executeQuery()) {

      // Iterate through the result set and extract data
      while (results.next()) {
        // Extract data for Project
        int projectId = results.getInt("ProjectID");
        String projectName = results.getString("ProjectName");
        String buildingType = results.getString("BuildingType");
        String projectAddress = results.getString("ProjectAddress");
        String erfNumber = results.getString("ERFNumber");
        double totalFee = results.getDouble("TotalFee");
        double totalPaid = results.getDouble("TotalPaid");
        LocalDate deadline = results.getDate("Deadline") != null ? 
          results.getDate("Deadline").toLocalDate() : null;
        boolean finalized = results.getBoolean("Finalized");
        LocalDate completionDate = results.getDate("CompletionDate") != null ? 
          results.getDate("CompletionDate").toLocalDate() : null;

        // Extract data for Customer
        int customerId = results.getInt("CustomerID");
        String customerName = results.getString("CustomerName");
        String customerPhone = results.getString("CustomerPhone");
        String customerEmail = results.getString("CustomerEmail");
        String customerAddress = results.getString("CustomerAddress");

        // Extract data for Architect
        int architectId = results.getInt("ArchitectID");
        String architectName = results.getString("ArchitectName");
        String architectPhone = results.getString("ArchitectPhone");
        String architectEmail = results.getString("ArchitectEmail");
        String architectAddress = results.getString("ArchitectAddress");

        // Extract data for Engineer
        int engineerId = results.getInt("EngineerID");
        String engineerName = results.getString("EngineerName");
        String engineerPhone = results.getString("EngineerPhone");
        String engineerEmail = results.getString("EngineerEmail");
        String engineerAddress = results.getString("EngineerAddress");

        // Print all information
        System.out.printf(
          "Project ID: %d, Project Name: %s, Building Type: %s, Project Address: %s, " +
          "ERF Number: %s, Total Fee: %.2f, Total Paid: %.2f, Deadline: %s, Finalized: %b, " +
          "Completion Date: %s, Customer ID: %d, Customer: %s, Phone: %s, Email: %s, " +
          "Architect ID: %d, Architect: %s, Phone: %s, Email: %s, Engineer ID: %d, " +
          "Engineer: %s, Phone: %s, Email: %s%n",
          projectId, projectName, buildingType, projectAddress, erfNumber, totalFee, totalPaid, 
          deadline, finalized, completionDate, customerId, customerName, customerPhone, 
          customerEmail, architectId, architectName, architectPhone, architectEmail, engineerId, 
          engineerName, engineerPhone, engineerEmail);
      }
    } catch (SQLException e) {
      System.err.println("SQL error: " + e.getMessage());
    }
  }

  /**
   * Handles the insertion of a customer, architect, engineer, and project into the database.
   *
   * @param customer the customer to be inserted.
   * @param architect the architect to be inserted.
   * @param engineer the engineer to be inserted.
   * @param project the project to be inserted.
   */
  private void handleInsert(Customer customer, Architect architect, Engineer engineer, Project project) {
	// Start a transaction
	try {
	    // Disable auto-commit to manage the transaction manually
	    connection.setAutoCommit(false);  
	
	    // Initialize variables to return the appropriate entities after checking or inserting
	    Customer returnedCustomer = null;
	    Architect returnedArchitect = null;
	    Engineer returnedEngineer = null;
	
	    // Check if Customer exists by ID or Name
	    String checkCustomerSQL = "SELECT * FROM Customer WHERE Name = ? OR CustomerID = ?";
	    try (PreparedStatement pstmtCheckCustomer = connection.prepareStatement(checkCustomerSQL)) {
	        pstmtCheckCustomer.setString(1, customer.getCustomerName());
	        pstmtCheckCustomer.setInt(2, customer.getCustomerId());
	        ResultSet rsCustomer = pstmtCheckCustomer.executeQuery();
	
	        if (rsCustomer.next()) {
	            // Customer exists, use the existing one
	            returnedCustomer = new Customer(
	                rsCustomer.getInt("CustomerID"),
	                rsCustomer.getString("Name"),
	                rsCustomer.getString("PhoneNumber"),
	                rsCustomer.getString("Email"),
	                rsCustomer.getString("PhysicalAddress")
	            );
	            System.out.println("Customer exists: " + returnedCustomer.getCustomerName());
	        } else {
	            // Customer does not exist, insert a new one
	            String insertCustomerSQL = "INSERT INTO Customer (Name, PhoneNumber, Email, PhysicalAddress) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement pstmtInsertCustomer = connection.prepareStatement(insertCustomerSQL, Statement.RETURN_GENERATED_KEYS)) {
	                pstmtInsertCustomer.setString(1, customer.getCustomerName());
	                pstmtInsertCustomer.setString(2, customer.getCustomerPhoneNumber());
	                pstmtInsertCustomer.setString(3, customer.getCustomerEmail());
	                pstmtInsertCustomer.setString(4, customer.getCustomerPhysicalAddress());
	                pstmtInsertCustomer.executeUpdate();
	                System.out.println("Customer added: " + customer.getCustomerName());
	
	                // Retrieve the generated CustomerID
	                try (ResultSet generatedKeys = pstmtInsertCustomer.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        returnedCustomer = new Customer(
	                            generatedKeys.getInt(1),  // Get the auto-generated CustomerID
	                            customer.getCustomerName(),
	                            customer.getCustomerPhoneNumber(),
	                            customer.getCustomerEmail(),
	                            customer.getCustomerPhysicalAddress()
	                        );
	                    } else {
	                        throw new SQLException("Failed to retrieve generated CustomerID.");
	                    }
	                }
	            } catch (SQLException e) {
	                connection.rollback();  // Rollback the transaction if customer insertion fails
	                System.err.println("Error adding customer: " + e.getMessage());
	                throw new SQLException("Failed to insert customer. Rolling back transaction.");
	            }
	        }
	    } catch (SQLException e) {
	        connection.rollback();  // Rollback transaction if checking customer fails
	        System.err.println("Error checking customer: " + e.getMessage());
	        throw new SQLException("Error checking customer. Rolling back transaction.");
	    }
	
	    // Check if Architect exists by ID or Name
	    String checkArchitectSQL = "SELECT * FROM Architect WHERE Name = ? OR ArchitectID = ?";
	    try (PreparedStatement pstmtCheckArchitect = connection.prepareStatement(checkArchitectSQL)) {
	        pstmtCheckArchitect.setString(1, architect.getArchitectName());
	        pstmtCheckArchitect.setInt(2, architect.getArchitectId());
	        ResultSet rsArchitect = pstmtCheckArchitect.executeQuery();
	
	        if (rsArchitect.next()) {
	            // Architect exists, use the existing one
	            returnedArchitect = new Architect(
	                rsArchitect.getInt("ArchitectID"),
	                rsArchitect.getString("Name"),
	                rsArchitect.getString("PhoneNumber"),
	                rsArchitect.getString("Email"),
	                rsArchitect.getString("PhysicalAddress")
	            );
	            System.out.println("Architect exists: " + returnedArchitect.getArchitectName());
	        } else {
	            // Architect does not exist, insert a new one
	            String insertArchitectSQL = "INSERT INTO Architect (Name, PhoneNumber, Email, PhysicalAddress) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement pstmtInsertArchitect = connection.prepareStatement(insertArchitectSQL, Statement.RETURN_GENERATED_KEYS)) {
	                pstmtInsertArchitect.setString(1, architect.getArchitectName());
	                pstmtInsertArchitect.setString(2, architect.getArchitectPhoneNumber());
	                pstmtInsertArchitect.setString(3, architect.getArchitectEmail());
	                pstmtInsertArchitect.setString(4, architect.getArchitectPhysicalAddress());
	                pstmtInsertArchitect.executeUpdate();
	                System.out.println("Architect added: " + architect.getArchitectName());
	
	                // Retrieve the generated ArchitectID
	                try (ResultSet generatedKeys = pstmtInsertArchitect.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        returnedArchitect = new Architect(
	                            generatedKeys.getInt(1),  // Get the auto-generated ArchitectID
	                            architect.getArchitectName(),
	                            architect.getArchitectPhoneNumber(),
	                            architect.getArchitectEmail(),
	                            architect.getArchitectPhysicalAddress()
	                        );
	                    } else {
	                        throw new SQLException("Failed to retrieve generated ArchitectID.");
	                    }
	                }
	            } catch (SQLException e) {
	                connection.rollback();  // Rollback if architect insertion fails
	                System.err.println("Error adding architect: " + e.getMessage());
	                throw new SQLException("Failed to insert architect. Rolling back transaction.");
	            }
	        }
	    } catch (SQLException e) {
	        connection.rollback();  // Rollback if checking architect fails
	        System.err.println("Error checking architect: " + e.getMessage());
	        throw new SQLException("Error checking architect. Rolling back transaction.");
	    }
	
	    // Check if Engineer exists by ID or Name
	    String checkEngineerSQL = "SELECT * FROM Engineer WHERE Name = ? OR EngineerID = ?";
	    try (PreparedStatement pstmtCheckEngineer = connection.prepareStatement(checkEngineerSQL)) {
	        pstmtCheckEngineer.setString(1, engineer.getEngineerName());
	        pstmtCheckEngineer.setInt(2, engineer.getEngineerId());
	        ResultSet rsEngineer = pstmtCheckEngineer.executeQuery();
	
	        if (rsEngineer.next()) {
	            // Engineer exists, use the existing one
	            returnedEngineer = new Engineer(
	                rsEngineer.getInt("EngineerID"),
	                rsEngineer.getString("Name"),
	                rsEngineer.getString("PhoneNumber"),
	                rsEngineer.getString("Email"),
	                rsEngineer.getString("PhysicalAddress")
	            );
	            System.out.println("Engineer exists: " + returnedEngineer.getEngineerName());
	        } else {
	            // Engineer does not exist, insert a new one
	            String insertEngineerSQL = "INSERT INTO Engineer (Name, PhoneNumber, Email, PhysicalAddress) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement pstmtInsertEngineer = connection.prepareStatement(insertEngineerSQL, Statement.RETURN_GENERATED_KEYS)) {
	                pstmtInsertEngineer.setString(1, engineer.getEngineerName());
	                pstmtInsertEngineer.setString(2, engineer.getEngineerPhoneNumber());
	                pstmtInsertEngineer.setString(3, engineer.getEngineerEmail());
	                pstmtInsertEngineer.setString(4, engineer.getEngineerPhysicalAddress());
	                pstmtInsertEngineer.executeUpdate();
	                System.out.println("Engineer added: " + engineer.getEngineerName());
	
	                // Retrieve the generated EngineerID
	                try (ResultSet generatedKeys = pstmtInsertEngineer.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        returnedEngineer = new Engineer(
	                            generatedKeys.getInt(1),  // Get the auto-generated EngineerID
	                            engineer.getEngineerName(),
	                            engineer.getEngineerPhoneNumber(),
	                            engineer.getEngineerEmail(),
	                            engineer.getEngineerPhysicalAddress()
	                        );
	                    } else {
	                        throw new SQLException("Failed to retrieve generated EngineerID.");
	                    }
	                }
	            } catch (SQLException e) {
	                connection.rollback();  // Rollback if engineer insertion fails
	                System.err.println("Error adding engineer: " + e.getMessage());
	                throw new SQLException("Failed to insert engineer. Rolling back transaction.");
	            }
	        }
	    } catch (SQLException e) {
	        connection.rollback();  // Rollback if checking engineer fails
	        System.err.println("Error checking engineer: " + e.getMessage());
	        throw new SQLException("Error checking engineer. Rolling back transaction.");
	    }
	
	    // Insert Project with the generated IDs for Customer, Architect, and Engineer
	    String insertProjectSQL = "INSERT INTO Project (ProjectName, CustomerID, ArchitectID, EngineerID, BuildingType, ERFNumber, TotalFee, TotalPaid, Deadline, PhysicalAddress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement pstmtInsertProject = connection.prepareStatement(insertProjectSQL)) {
	        pstmtInsertProject.setString(1, project.getProjectName());
	        pstmtInsertProject.setInt(2, returnedCustomer.getCustomerId());
	        pstmtInsertProject.setInt(3, returnedArchitect.getArchitectId());
	        pstmtInsertProject.setInt(4, returnedEngineer.getEngineerId());
	        pstmtInsertProject.setString(5, project.getBuildingType());
	        pstmtInsertProject.setString(6, project.getErfNumber());
	        pstmtInsertProject.setDouble(7, project.getTotalFee());
	        pstmtInsertProject.setDouble(8, project.getTotalPaid());
	        pstmtInsertProject.setDate(9, project.getDeadline() != null ? java.sql.Date.valueOf(project.getDeadline().toString()) : null);
	        pstmtInsertProject.setString(10, project.getPhysicalAddress());
	        pstmtInsertProject.executeUpdate();
	        System.out.println("Project added: " + project.getProjectName());
	    } catch (SQLException e) {
	        // Rollback if project insertion fails
	        connection.rollback();  
	        System.err.println("Error adding project: " + e.getMessage());
	        throw new SQLException("Failed to insert project. Rolling back transaction.");
	    }
	
	    // Commit the transaction if all operations succeed
	    connection.commit();
	} catch (SQLException e) {
	    // Rollback the transaction if any part fails
	    try {
	        connection.rollback();
	    } catch (SQLException rollbackEx) {
	        System.err.println("Error during rollback: " + rollbackEx.getMessage());
	    }
	    System.err.println("Transaction failed: " + e.getMessage());
	} finally {
	    try {
	        // Restore auto-commit mode
	        connection.setAutoCommit(true);
	    } catch (SQLException e) {
	        System.err.println("Error restoring auto-commit: " + e.getMessage());
	        }
	    }
	}

 /**
   * Handles the updating of a customer, architect, engineer, and project in the database.
   *
   * @param customer the customer to be updated.
   * @param architect the architect to be updated.
   * @param engineer the engineer to be updated.
   * @param project the project to be updated.
   */
  private void handleUpdate(Customer customer, Architect architect, Engineer engineer, Project project) {
    // Update Customer
    String updateCustomerSQL = "UPDATE Customer SET Name = ?, PhoneNumber = ?, Email = ?, " +
                                "PhysicalAddress = ? WHERE CustomerID = ?";
    try (PreparedStatement pstmtUpdateCustomer = connection.prepareStatement(updateCustomerSQL)) {
      pstmtUpdateCustomer.setString(1, customer.getCustomerName());
      pstmtUpdateCustomer.setString(2, customer.getCustomerPhoneNumber());
      pstmtUpdateCustomer.setString(3, customer.getCustomerEmail());
      pstmtUpdateCustomer.setString(4, customer.getCustomerPhysicalAddress());
      pstmtUpdateCustomer.setInt(5, customer.getCustomerId());
      pstmtUpdateCustomer.executeUpdate();
      System.out.println("Customer updated: " + customer.getCustomerName());
    } catch (SQLException e) {
      System.err.println("Error updating customer: " + e.getMessage());
    }

    // Update Architect
    String updateArchitectSQL = "UPDATE Architect SET Name = ?, PhoneNumber = ?, Email = ?, " +
                                 "PhysicalAddress = ? WHERE ArchitectID = ?";
    try (PreparedStatement pstmtUpdateArchitect = connection.prepareStatement(updateArchitectSQL)) {
      pstmtUpdateArchitect.setString(1, architect.getArchitectName());
      pstmtUpdateArchitect.setString(2, architect.getArchitectPhoneNumber());
      pstmtUpdateArchitect.setString(3, architect.getArchitectEmail());
      pstmtUpdateArchitect.setString(4, architect.getArchitectPhysicalAddress());
      pstmtUpdateArchitect.setInt(5, architect.getArchitectId());
      pstmtUpdateArchitect.executeUpdate();
      System.out.println("Architect updated: " + architect.getArchitectName());
    } catch (SQLException e) {
      System.err.println("Error updating architect: " + e.getMessage());
    }

    // Update Engineer
    String updateEngineerSQL = "UPDATE Engineer SET Name = ?, PhoneNumber = ?, Email = ?, " +
                                "PhysicalAddress = ? WHERE EngineerID = ?";
    try (PreparedStatement pstmtUpdateEngineer = connection.prepareStatement(updateEngineerSQL)) {
      pstmtUpdateEngineer.setString(1, engineer.getEngineerName());
      pstmtUpdateEngineer.setString(2, engineer.getEngineerPhoneNumber());
      pstmtUpdateEngineer.setString(3, engineer.getEngineerEmail());
      pstmtUpdateEngineer.setString(4, engineer.getEngineerPhysicalAddress());
      pstmtUpdateEngineer.setInt(5, engineer.getEngineerId());
      pstmtUpdateEngineer.executeUpdate();
      System.out.println("Engineer updated: " + engineer.getEngineerName());
    } catch (SQLException e) {
      System.err.println("Error updating engineer: " + e.getMessage());
    }

    // Update Project
    String updateProjectSQL = "UPDATE Project SET ProjectName = ?, CustomerID = ?, " +
                              "ArchitectID = ?, EngineerID = ?, BuildingType = ?, " +
                              "ERFNumber = ?, TotalFee = ?, TotalPaid = ?, " +
                              "Deadline = ?, PhysicalAddress = ? WHERE ProjectID = ?";
    try (PreparedStatement pstmtUpdateProject = connection.prepareStatement(updateProjectSQL)) {
      pstmtUpdateProject.setString(1, project.getProjectName());
      pstmtUpdateProject.setInt(2, customer.getCustomerId());
      pstmtUpdateProject.setInt(3, architect.getArchitectId());
      pstmtUpdateProject.setInt(4, engineer.getEngineerId());
      pstmtUpdateProject.setString(5, project.getBuildingType());
      pstmtUpdateProject.setString(6, project.getErfNumber());
      pstmtUpdateProject.setDouble(7, project.getTotalFee());
      pstmtUpdateProject.setDouble(8, project.getTotalPaid());
      // Only set Deadline if provided
      if (project.getDeadline() != null) {
        pstmtUpdateProject.setDate(9, java.sql.Date.valueOf(project.getDeadline().toString()));
      } else {
        // Handle as needed.
        pstmtUpdateProject.setDate(9, null); 
      }
      pstmtUpdateProject.setString(10, project.getPhysicalAddress());
      pstmtUpdateProject.setInt(11, project.getProjectId());
      pstmtUpdateProject.executeUpdate();
      System.out.println("Project updated: " + project.getProjectName());
    } catch (SQLException e) {
      System.err.println("Error updating project: " + e.getMessage());
    }
  }

  /**
   * Deletes a project and its associated records from the database.
   *
   * @param projectId the ID of the project to delete.
   * @param engineerId the ID of the associated engineer.
   * @param customerId the ID of the associated customer.
   * @param architectId the ID of the associated architect.
   * @throws SQLException if a database access error occurs.
   */
  public void deleteProject(int projectId, int engineerId, int customerId, int architectId) 
    throws SQLException {
    // First, delete the project itself
    String sqlDeleteProject = "DELETE FROM Project WHERE ProjectID = ?;";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteProject)) {
      preparedStatement.setInt(1, projectId);
      preparedStatement.executeUpdate();
    }

    // Now, check if we can safely delete associated records
    String sqlCheckEngineer = "SELECT COUNT(*) FROM Project WHERE EngineerID = ?;";
    String sqlCheckCustomer = "SELECT COUNT(*) FROM Project WHERE CustomerID = ?;";
    String sqlCheckArchitect = "SELECT COUNT(*) FROM Project WHERE ArchitectID = ?;";

    // Check if the Engineer is still associated with other projects
    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckEngineer)) {
      preparedStatement.setInt(1, engineerId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next() && resultSet.getInt(1) == 0) {
        String sqlDeleteEngineer = "DELETE FROM Engineer WHERE EngineerID = ?;";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDeleteEngineer)) {
          deleteStatement.setInt(1, engineerId);
          deleteStatement.executeUpdate();
        }
      }
    }

    // Check if the Customer is still associated with other projects
    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckCustomer)) {
      preparedStatement.setInt(1, customerId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next() && resultSet.getInt(1) == 0) {
        String sqlDeleteCustomer = "DELETE FROM Customer WHERE CustomerID = ?;";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDeleteCustomer)) {
          deleteStatement.setInt(1, customerId);
          deleteStatement.executeUpdate();
        }
      }
    }

    // Check if the Architect is still associated with other projects
    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckArchitect)) {
      preparedStatement.setInt(1, architectId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next() && resultSet.getInt(1) == 0) {
        String sqlDeleteArchitect = "DELETE FROM Architect WHERE ArchitectID = ?;";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDeleteArchitect)) {
          deleteStatement.setInt(1, architectId);
          deleteStatement.executeUpdate();
        }
      }
    }
  }

  /**
   * Deletes an existing project and displays the updated project list.
   *
   * @param project the project to delete.
   */
  public void deleteExistingProject(Project project) {
    // Display current projects
    readPeopleAndProjects();

    try {
      // Call deleteProject with project details
      deleteProject(project.getProjectId(), project.getEngineerId(), 
                     project.getCustomerId(), project.getArchitectId());

      // Read the projects again to show updated data
      System.out.println("Project deleted successfully. Updated list of projects:\n");
      readPeopleAndProjects();
    } catch (SQLException e) {
      System.err.println("SQL error: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }

  /**
   * Reads project details and handles insertion, update, and deletion.
   *
   * @param scanner the Scanner object for user input.
   */
  public void readAndWrite(Scanner scanner) {
    try {
        // Set auto-commit to false at the beginning of the transaction
        connection.setAutoCommit(false);

        // Display current projects and people
        readPeopleAndProjects();

        // Collect data for Customer
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter Customer Phone Number: ");
        String customerPhone = scanner.nextLine();
        System.out.print("Enter Customer Email: ");
        String customerEmail = scanner.nextLine();
        System.out.print("Enter Customer Physical Address: ");
        String customerPhysicalAddress = scanner.nextLine();
        // Create Customer object (ID will be auto-generated)
        Customer customer = new Customer(0, customerName, customerPhone, customerEmail, customerPhysicalAddress); // ID is initially 0

        // Collect data for Architect
        System.out.print("Enter Architect Name: ");
        String architectName = scanner.nextLine();
        System.out.print("Enter Architect Phone Number: ");
        String architectPhone = scanner.nextLine();
        System.out.print("Enter Architect Email: ");
        String architectEmail = scanner.nextLine();
        System.out.print("Enter Architect Physical Address: ");
        String architectPhysicalAddress = scanner.nextLine();
        // Create Architect object (ID will be auto-generated)
        Architect architect = new Architect(0, architectName, architectPhone, architectEmail, architectPhysicalAddress); // ID is initially 0

        // Collect data for Engineer
        System.out.print("Enter Engineer Name: ");
        String engineerName = scanner.nextLine();
        System.out.print("Enter Engineer Phone Number: ");
        String engineerPhone = scanner.nextLine();
        System.out.print("Enter Engineer Email: ");
        String engineerEmail = scanner.nextLine();
        System.out.print("Enter Engineer Physical Address: ");
        String engineerPhysicalAddress = scanner.nextLine();
        // Create Engineer object (ID will be auto-generated)
        Engineer engineer = new Engineer(0, engineerName, engineerPhone, engineerEmail, engineerPhysicalAddress); // ID is initially 0

        // Collect data for Project
        System.out.print("Enter Project ID: ");
        int projectId = getValidInt(scanner);
        System.out.print("Enter Project Name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter Building Type: ");
        String buildingType = scanner.nextLine();
        System.out.print("Enter ERF Number: ");
        String erfNumber = scanner.nextLine();
        System.out.print("Enter Total Fee: ");
        double totalFee = getValidDouble(scanner);
        System.out.print("Enter Total Paid: ");
        double totalPaid = getValidDouble(scanner);
        System.out.print("Enter Deadline (YYYY-MM-DD): ");
        LocalDate deadline = getValidDate(scanner);
        System.out.print("Enter Project Physical Address: ");
        String projectPhysicalAddress = scanner.nextLine();
        // Create Project object (ID is provided by user, others are auto-generated later)
        Project project = new Project(projectId, projectName, buildingType, projectPhysicalAddress, erfNumber, totalFee, totalPaid, java.sql.Date.valueOf(deadline), 0, 0, 0); // IDs are initially 0

        // Insert the data into the database and get auto-generated IDs
        handleInsert(customer, architect, engineer, project);

        // Call the update method (if needed)
        handleUpdate(customer, architect, engineer, project);

        // If the user wants to delete, call delete method (Delete method will handle it)
        deleteExistingProject(project);

        // Commit the transaction
        connection.commit();
        System.out.println("Operation successful!");
	
	    } catch (SQLException e) {
	        System.err.println("Transaction error: " + e.getMessage());
	        try {
	            // Rollback the transaction if any error occurs
	            connection.rollback();
	            System.out.println("Transaction rolled back.");
	        } catch (SQLException rollbackEx) {
	            System.err.println("Rollback error: " + rollbackEx.getMessage());
	        }
	    } finally {
	        try {
	            // Reset auto-commit to true
	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            System.err.println("Failed to reset auto-commit: " + e.getMessage());
	        }
	    }
	}

  /**
   * Validates integer input from the user.
   *
   * @param scanner the Scanner object for user input.
   * @return the validated integer input.
   */
  public int getValidInt(Scanner scanner) {
    while (true) {
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        // Or another sentinel value to indicate skipping
        return -1;
      }
      try {
        // Parse integer
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.print("Invalid input. Please enter an integer: ");
      }
    }
  }

  /**
   * Validates double input from the user.
   *
   * @param scanner the Scanner object for user input.
   * @return the validated double input.
   */
  public Double getValidDouble(Scanner scanner) {
    while (true) {
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        // Return null or another sentinel value to indicate skipping
        return (double) -1; 
      }
      try {
        // Parse double
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.print("Invalid input. Please enter a decimal number: ");
      }
    }
  }

  /**
   * Validates date input from the user.
   *
   * @param scanner the Scanner object for user input.
   * @return the validated LocalDate input.
   */
  public LocalDate getValidDate(Scanner scanner) {
    while (true) {
      String dateInput = scanner.nextLine().trim();
      if (dateInput.isEmpty()) {
        // Return null to indicate skipping
        return null;
      }
      try {
        return LocalDate.parse(dateInput);
      } catch (DateTimeParseException e) {
        System.out.print("Invalid date format. Please enter in YYYY-MM-DD format: ");
      }
    }
  }

  /**
   * Generates a project name based on user input.
   *
   * @param projectName the name of the project.
   * @param buildingType the type of building.
   * @param customerName the name of the customer.
   * @return the generated project name.
   */
  public static String generateProjectName(String projectName, String buildingType, 
                                            String customerName) {
    if (projectName == null || projectName.trim().isEmpty()) {
      customerName = customerName.trim();
      String surname;

      if (customerName.contains(" ")) {
        // Get the last surname
        surname = customerName.split(" ")[customerName.split(" ").length - 1];
      } else if (!customerName.isEmpty()) {
        // Use full name if no spaces
        surname = customerName;
      } else {
        // Default value
        surname = "Customer";
      }

      // Generate name
      projectName = surname + "'s " + buildingType;
    }
    return projectName;
  }

  /**
   * Captures customer, engineer, architect, and project information
   * and inserts it into the database.
   *
   * @param scanner the Scanner object for user input.
   */
  public void captureInfoAndAdd(Scanner scanner) {
    try {

        // Handle Customer (create or select)
        System.out.print("[1] Create new Customer, or [2] Select existing Customer. Please select 1/2: ");
        int customerChoice = getValidInt(scanner);
        Customer customer = null;

        if (customerChoice == 1) {
            // Collect full information for creating a new Customer
            customer = createCustomer(scanner);
        } else if (customerChoice == 2) {
            // Select existing Customer
            customer = selectCustomer(scanner);
        }

        // Handle Engineer (create or select)
        System.out.print("[1] Create new Engineer, or [2] Select existing Engineer. Please select 1/2: ");
        int engineerChoice = getValidInt(scanner);
        Engineer engineer = null;

        if (engineerChoice == 1) {
            // Collect full information for creating a new Engineer
            engineer = createEngineer(scanner);
        } else if (engineerChoice == 2) {
            // Select existing Engineer
            engineer = selectEngineer(scanner);
        }

        // Handle Architect (create or select)
        System.out.print("[1] Create new Architect, or [2] Select existing Architect. Please select 1/2: ");
        int architectChoice = getValidInt(scanner);
        Architect architect = null;

        if (architectChoice == 1) {
            // Collect full information for creating a new Architect
            architect = createArchitect(scanner);
        } else if (architectChoice == 2) {
            // Select existing Architect
            architect = selectArchitect(scanner);
        }

        // Collect Project information
        System.out.print("Enter Project Name: ");
        String projectName = scanner.nextLine().trim();
        
        // Declare the variable outside to use later
        String buildingType = "";  

        // If the project name is empty, generate a default name based on the customer's surname and building type
        if (projectName == null || projectName.isEmpty()) {
            System.out.print("Enter Building Type: ");
            buildingType = scanner.nextLine().trim();

            // Generate the project name using the customer's surname and the building type
            projectName = generateProjectName(projectName, buildingType, customer.getCustomerName());
        } else {
            // Collect Building Type if name was manually entered
            System.out.print("Enter Building Type: ");
            buildingType = scanner.nextLine().trim();
        }

        // Collect remaining project details
        System.out.print("Enter ERF Number: ");
        String erfNumber = scanner.nextLine().trim();

        System.out.print("Enter Total Fee: ");
        double totalFee = getValidDouble(scanner);

        System.out.print("Enter Total Paid: ");
        double totalPaid = getValidDouble(scanner);

        System.out.print("Enter Deadline (YYYY-MM-DD): ");
        LocalDate deadline = getValidDate(scanner);

        System.out.print("Enter Project Physical Address: ");
        String projectPhysicalAddress = scanner.nextLine().trim();

        // Create Project object (IDs set to 0 initially)
        Project project = new Project(0, projectName, buildingType, projectPhysicalAddress, erfNumber, totalFee, totalPaid, java.sql.Date.valueOf(deadline), architect.getArchitectId(), engineer.getEngineerId(), customer.getCustomerId());

        // Insert data into the database
        handleInsert(customer, architect, engineer, project);

    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}


  /**
   * Method to create a new Customer.
   *
   * @param scanner the Scanner object for user input.
   * @return the created Customer object.
   */
  private Customer createCustomer(Scanner scanner) {
	// Prompt for customer details.
    System.out.print("Enter Customer Name: ");
    String customerName = scanner.nextLine().trim();
    System.out.print("Enter Customer Phone Number: ");
    String customerPhone = scanner.nextLine().trim();
    System.out.print("Enter Customer Email: ");
    String customerEmail = scanner.nextLine().trim();
    System.out.print("Enter Customer Physical Address: ");
    String customerPhysicalAddress = scanner.nextLine().trim();

    // Return a new Customer object
    return new Customer(0, customerName, customerPhone, customerEmail, customerPhysicalAddress);
  }

  /**
   * Method to select an existing Customer from the database.
   *
   * @param scanner the Scanner object for user input.
   * @return the selected Customer object, or null if no valid customer is found.
   */
  private Customer selectCustomer(Scanner scanner) {
    System.out.println("Select Customer from the following list:");
    // List all customers
    displayCustomerList();  
    System.out.print("Enter Customer ID to select: ");
    int customerId = getValidInt(scanner);

    // Fetch and return the selected customer
    Customer selectedCustomer = getCustomerById(customerId);
    if (selectedCustomer == null) {
      System.out.println("Invalid Customer ID selected.");
      return null;
    }
    return selectedCustomer;
  }

  /**
   * Method to create a new Engineer.
   *
   * @param scanner the Scanner object for user input.
   * @return the created Engineer object.
   */
  private Engineer createEngineer(Scanner scanner) {
	// Prompt for engineer details.
    System.out.print("Enter Engineer Name: ");
    String engineerName = scanner.nextLine().trim();
    System.out.print("Enter Engineer Phone Number: ");
    String engineerPhone = scanner.nextLine().trim();
    System.out.print("Enter Engineer Email: ");
    String engineerEmail = scanner.nextLine().trim();
    System.out.print("Enter Engineer Physical Address: ");
    String engineerPhysicalAddress = scanner.nextLine().trim();

    // Return a new Engineer object
    return new Engineer(0, engineerName, engineerPhone, engineerEmail, engineerPhysicalAddress);
  }

  /**
   * Method to select an existing Engineer from the database.
   *
   * @param scanner the Scanner object for user input.
   * @return the selected Engineer object, or null if no valid engineer is found.
   */
  private Engineer selectEngineer(Scanner scanner) {
    System.out.println("Select Engineer from the following list:");
    // List all engineers
    displayEngineerList();  
    System.out.print("Enter Engineer ID to select: ");
    int engineerId = getValidInt(scanner);

    // Fetch and return the selected engineer
    Engineer selectedEngineer = getEngineerById(engineerId);
    if (selectedEngineer == null) {
      System.out.println("Invalid Engineer ID selected.");
      return null;
    }
    return selectedEngineer;
  }

  /**
   * Method to create a new Architect.
   *
   * @param scanner the Scanner object for user input.
   * @return the created Architect object.
   */
  private Architect createArchitect(Scanner scanner) {
	//Prompt for architect details
    System.out.print("Enter Architect Name: ");
    String architectName = scanner.nextLine().trim();
    System.out.print("Enter Architect Phone Number: ");
    String architectPhone = scanner.nextLine().trim();
    System.out.print("Enter Architect Email: ");
    String architectEmail = scanner.nextLine().trim();
    System.out.print("Enter Architect Physical Address: ");
    String architectPhysicalAddress = scanner.nextLine().trim();

    // Return a new Architect object
    return new Architect(0, architectName, architectPhone, architectEmail, architectPhysicalAddress);
  }

  /**
   * Method to select an existing Architect from the database.
   *
   * @param scanner the Scanner object for user input.
   * @return the selected Architect object, or null if no valid architect is found.
   */
  private Architect selectArchitect(Scanner scanner) {
    System.out.println("Select Architect from the following list:");
    // List all architects
    displayArchitectList();  
    System.out.print("Enter Architect ID to select: ");
    int architectId = getValidInt(scanner);

    // Fetch and return the selected architect
    Architect selectedArchitect = getArchitectById(architectId);
    if (selectedArchitect == null) {
      System.out.println("Invalid Architect ID selected.");
      return null;
    }
    return selectedArchitect;
  }

  /**
   * Display the list of customers from the database.
   */
  private void displayCustomerList() {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT CustomerID, Name FROM Customer");
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int customerId = rs.getInt("CustomerID");
        String name = rs.getString("Name");
        System.out.println("[ID: " + customerId + "] " + name);
      }
    } catch (SQLException e) {
      System.err.println("Error displaying customer list: " + e.getMessage());
    }
  }

  /**
   * Display the list of engineers from the database.
   */
  private void displayEngineerList() {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT EngineerID, Name FROM Engineer");
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int engineerId = rs.getInt("EngineerID");
        String name = rs.getString("Name");
        System.out.println("[ID: " + engineerId + "] " + name);
      }
    } catch (SQLException e) {
      System.err.println("Error displaying engineer list: " + e.getMessage());
    }
  }

  /**
   * Display the list of architects from the database.
   */
  private void displayArchitectList() {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT ArchitectID, Name FROM Architect");
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int architectId = rs.getInt("ArchitectID");
        String name = rs.getString("Name");
        System.out.println("[ID: " + architectId + "] " + name);
      }
    } catch (SQLException e) {
      System.err.println("Error displaying architect list: " + e.getMessage());
    }
  }

  /**
   * Method to fetch a customer by ID from the database.
   *
   * @param customerId the ID of the customer to fetch.
   * @return the Customer object, or null if no customer is found.
   */
  private Customer getCustomerById(int customerId) {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Customer WHERE CustomerID = ?")) {
      stmt.setInt(1, customerId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Customer(
            rs.getInt("CustomerID"),
            rs.getString("Name"),
            rs.getString("PhoneNumber"),
            rs.getString("Email"),
            rs.getString("PhysicalAddress")
          );
        }
      }
    } catch (SQLException e) {
      System.err.println("Error fetching customer: " + e.getMessage());
    }
    // Return null if no customer is found
    return null;  
  }

  /**
   * Method to fetch an engineer by ID from the database.
   *
   * @param engineerId the ID of the engineer to fetch.
   * @return the Engineer object, or null if no engineer is found.
   */
  private Engineer getEngineerById(int engineerId) {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Engineer WHERE EngineerID = ?")) {
      stmt.setInt(1, engineerId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Engineer(
            rs.getInt("EngineerID"),
            rs.getString("Name"),
            rs.getString("PhoneNumber"),
            rs.getString("Email"),
            rs.getString("PhysicalAddress")
          );
        }
      }
    } catch (SQLException e) {
      System.err.println("Error fetching engineer: " + e.getMessage());
    }
    // Return null if no engineer is found
    return null;  
  }

  /**
   * Method to fetch an architect by ID from the database.
   *
   * @param architectId the ID of the architect to fetch.
   * @return the Architect object, or null if no architect is found.
   */
  private Architect getArchitectById(int architectId) {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Architect WHERE ArchitectID = ?")) {
      stmt.setInt(1, architectId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Architect(
            rs.getInt("ArchitectID"),
            rs.getString("Name"),
            rs.getString("PhoneNumber"),
            rs.getString("Email"),
            rs.getString("PhysicalAddress")
          );
        }
      }
    } catch (SQLException e) {
      System.err.println("Error fetching architect: " + e.getMessage());
    }
    // Return null if no architect is found
    return null;  
  }

  /**
   * Fetches project details based on the provided project ID.
   *
   * @param projectId the ID of the project to fetch.
   * @return a map containing project details, or null if not found.
   */
  public Map<String, Object> fetchProjectDetails(int projectId) {
    // SQL query to select details along with associated customer, architect, and engineer information.
    String sqlQuery = """
        SELECT
            Project.ProjectID,
            Project.ProjectName,
            Project.BuildingType,
            Project.ERFNumber,
            Project.TotalFee,
            Project.TotalPaid,
            Project.PhysicalAddress,
            Project.Deadline,
            Customer.CustomerID,
            Customer.Name AS CustomerName,
            Customer.PhoneNumber AS CustomerPhone,
            Customer.Email AS CustomerEmail,
            Customer.PhysicalAddress AS CustomerAddress,
            Architect.ArchitectID,
            Architect.Name AS ArchitectName,
            Architect.PhoneNumber AS ArchitectPhone,
            Architect.Email AS ArchitectEmail,
            Architect.PhysicalAddress AS ArchitectAddress,
            Engineer.EngineerID,
            Engineer.Name AS EngineerName,
            Engineer.PhoneNumber AS EngineerPhone,
            Engineer.Email AS EngineerEmail,
            Engineer.PhysicalAddress AS EngineerAddress
        FROM Project
        INNER JOIN Customer ON Project.CustomerID = Customer.CustomerID
        INNER JOIN Architect ON Project.ArchitectID = Architect.ArchitectID
        INNER JOIN Engineer ON Project.EngineerID = Engineer.EngineerID
        WHERE Project.ProjectID = ?;
    """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
      // Set the project ID in the prepared statement
      preparedStatement.setInt(1, projectId);
      try (ResultSet results = preparedStatement.executeQuery()) {
        if (results.next()) {
          Map<String, Object> projectDetails = new HashMap<>();
          // Populate the map with project details from the result set
          projectDetails.put("ProjectID", results.getInt("ProjectID"));
          projectDetails.put("ProjectName", results.getString("ProjectName"));
          projectDetails.put("BuildingType", results.getString("BuildingType"));
          projectDetails.put("ERFNumber", results.getString("ERFNumber"));
          projectDetails.put("TotalFee", results.getDouble("TotalFee"));
          projectDetails.put("TotalPaid", results.getDouble("TotalPaid"));
          projectDetails.put("PhysicalAddress", results.getString("PhysicalAddress"));


          // Handle potential null for Deadline
          java.sql.Date deadline = results.getDate("Deadline");
          projectDetails.put("Deadline", (deadline != null) ? deadline.toLocalDate() : null);

          // Populate customer details
          projectDetails.put("CustomerID", results.getInt("CustomerID"));
          projectDetails.put("CustomerName", results.getString("CustomerName"));
          projectDetails.put("CustomerPhone", results.getString("CustomerPhone"));
          projectDetails.put("CustomerEmail", results.getString("CustomerEmail"));
          projectDetails.put("CustomerAddress", results.getString("CustomerAddress"));

          // Populate architect details
          projectDetails.put("ArchitectID", results.getInt("ArchitectID"));
          projectDetails.put("ArchitectName", results.getString("ArchitectName"));
          projectDetails.put("ArchitectPhone", results.getString("ArchitectPhone"));
          projectDetails.put("ArchitectEmail", results.getString("ArchitectEmail"));
          projectDetails.put("ArchitectAddress", results.getString("ArchitectAddress"));

          // Populate engineer details 
          projectDetails.put("EngineerID", results.getInt("EngineerID"));
          projectDetails.put("EngineerName", results.getString("EngineerName"));
          projectDetails.put("EngineerPhone", results.getString("EngineerPhone"));
          projectDetails.put("EngineerEmail", results.getString("EngineerEmail"));
          projectDetails.put("EngineerAddress", results.getString("EngineerAddress"));

          return projectDetails;
        }
      }
    } catch (SQLException e) {
      System.err.println("SQL error: " + e.getMessage());
    }
    // Return null if no project is found or if an exception occurs
    return null; 
  }
  
  /**
   * Fetches and displays the Project ID and Project Name from the database.
   * This method is intended to simplify the process of displaying only essential project details
   * to help the user easily identify and select a project for updating.
   */
  public void fetchProjectIdAndName() {
      // SQL query to fetch Project ID and Project Name from the Project table
      String query = "SELECT ProjectID, ProjectName FROM Project"; // Adjust table name as needed
      
      try (Statement stmt = connection.createStatement(); 
           ResultSet rs = stmt.executeQuery(query)) {
          
          // Display the list of available projects
          System.out.println("Available Projects:");
          
          // Loop through each project and display only the Project ID and Project Name
          while (rs.next()) {
              int projectId = rs.getInt("ProjectID");      // Fetch Project ID
              String projectName = rs.getString("ProjectName"); // Fetch Project Name
              System.out.println("ID: " + projectId + " | Name: " + projectName); // Print details
          }
      } catch (SQLException e) {
          // Handle any SQL exceptions that occur during the process
          System.err.println("Error fetching project details: " + e.getMessage());
      }
  }


  /**
   * Captures and updates the project details based on user input.
   *
   * @param scanner the Scanner object for user input.
   */
  public void captureAndUpdate(Scanner scanner) {
    try {
        // Set auto-commit to false at the beginning of the transaction
        connection.setAutoCommit(false);

        // Display current projects and people
        fetchProjectIdAndName();

        // Collect Project ID to update
        System.out.print("Enter Project ID you wish to update: ");
        int projectId = getValidInt(scanner);

        // Fetch existing project details from the database
        Map<String, Object> projectDetails = fetchProjectDetails(projectId);
        if (projectDetails == null) {
            System.out.println("No project found with that ID.");
            return;
        }

        // Create existing Customer object with existing details
        Customer customer = new Customer(
            (Integer) projectDetails.get("CustomerID"),
            (String) projectDetails.get("CustomerName"),
            (String) projectDetails.get("CustomerPhone"),
            (String) projectDetails.get("CustomerEmail"),
            (String) projectDetails.get("CustomerAddress")
        );

        // Create existing Architect object with existing details
        Architect architect = new Architect(
            (Integer) projectDetails.get("ArchitectID"),
            (String) projectDetails.get("ArchitectName"),
            (String) projectDetails.get("ArchitectPhone"),
            (String) projectDetails.get("ArchitectEmail"),
            (String) projectDetails.get("ArchitectAddress")
        );

        // Create existing Engineer object with existing details
        Engineer engineer = new Engineer(
            (Integer) projectDetails.get("EngineerID"),
            (String) projectDetails.get("EngineerName"),
            (String) projectDetails.get("EngineerPhone"),
            (String) projectDetails.get("EngineerEmail"),
            (String) projectDetails.get("EngineerAddress")
        );

        // Create existing Project object with existing details
        Project project = new Project(
            projectId,
            (String) projectDetails.get("ProjectName"),
            (String) projectDetails.get("BuildingType"),
            (String) projectDetails.get("PhysicalAddress"),
            (String) projectDetails.get("ERFNumber"),
            (Double) projectDetails.get("TotalFee"),
            (Double) projectDetails.get("TotalPaid"),
            java.sql.Date.valueOf((LocalDate) projectDetails.get("Deadline")),
            architect.getArchitectId(),
            engineer.getEngineerId(),
            customer.getCustomerId()
        );

        // Allow user to update fields one by one, if they choose
        System.out.print("Enter Project Name (Current: " + project.getProjectName() + "): (Press enter to skip) ");
        String projectName = scanner.nextLine().trim();
        if (!projectName.isEmpty()) {
            project.setProjectName(projectName);
        }

        System.out.print("Enter Building Type (Current: " + project.getBuildingType() + "): (Press enter to skip) ");
        String buildingType = scanner.nextLine().trim();
        if (!buildingType.isEmpty()) {
            project.setBuildingType(buildingType);
        }

        System.out.print("Enter ERF Number (Current: " + project.getErfNumber() + "): (Press enter to skip) ");
        String erfNumber = scanner.nextLine().trim();
        if (!erfNumber.isEmpty()) {
            project.setErfNumber(erfNumber);
        }

        System.out.print("Enter Total Fee (Current: " + project.getTotalFee() + "): (Press enter to skip) ");
        double totalFee = getValidDouble(scanner);
        if (totalFee != -1) {
            project.setTotalFee(totalFee);
        }

        System.out.print("Enter Total Paid (Current: " + project.getTotalPaid() + "): (Press enter to skip) ");
        double totalPaid = getValidDouble(scanner);
        if (totalPaid != -1) {
            project.setTotalPaid(totalPaid);
        }

        System.out.print("Enter Deadline (Current: " + project.getDeadline() + ") (YYYY-MM-DD): (Press enter to skip) ");
        LocalDate deadline = getValidDate(scanner);
        if (deadline != null) {
            project.setDeadline(java.sql.Date.valueOf(deadline));
        }

        System.out.print("Enter Project Physical Address (Current: " + project.getPhysicalAddress() + "): (Press enter to skip) ");
        String projectPhysicalAddress = scanner.nextLine().trim();
        if (!projectPhysicalAddress.isEmpty()) {
            project.setPhysicalAddress(projectPhysicalAddress);
        }

        // Update Customer Details
        System.out.print("Enter Customer Name (Current: " + customer.getCustomerName() + "): (Press enter to skip) ");
        String customerName = scanner.nextLine().trim();
        if (!customerName.isEmpty()) {
            customer.setCustomerName(customerName);
        }

        System.out.print("Enter Customer Phone Number (Current: " + customer.getCustomerPhoneNumber() + "): (Press enter to skip) ");
        String customerPhone = scanner.nextLine().trim();
        if (!customerPhone.isEmpty()) {
            customer.setCustomerPhoneNumber(customerPhone);
        }

        System.out.print("Enter Customer Email (Current: " + customer.getCustomerEmail() + "): (Press enter to skip) ");
        String customerEmail = scanner.nextLine().trim();
        if (!customerEmail.isEmpty()) {
            customer.setCustomerEmail(customerEmail);
        }

        System.out.print("Enter Customer Physical Address (Current: " + customer.getCustomerPhysicalAddress() + "): (Press enter to skip) ");
        String customerPhysicalAddress = scanner.nextLine().trim();
        if (!customerPhysicalAddress.isEmpty()) {
            customer.setCustomerPhysicalAddress(customerPhysicalAddress);
        }

        // Update Architect Details
        System.out.print("Enter Architect Name (Current: " + architect.getArchitectName() + "): (Press enter to skip) ");
        String architectName = scanner.nextLine().trim();
        if (!architectName.isEmpty()) {
            architect.setArchitectName(architectName);
        }

        System.out.print("Enter Architect Phone Number (Current: " + architect.getArchitectPhoneNumber() + "): (Press enter to skip) ");
        String architectPhone = scanner.nextLine().trim();
        if (!architectPhone.isEmpty()) {
            architect.setArchitectPhoneNumber(architectPhone);
        }

        System.out.print("Enter Architect Email (Current: " + architect.getArchitectEmail() + "): (Press enter to skip) ");
        String architectEmail = scanner.nextLine().trim();
        if (!architectEmail.isEmpty()) {
            architect.setArchitectEmail(architectEmail);
        }

        System.out.print("Enter Architect Physical Address (Current: " + architect.getArchitectPhysicalAddress() + "): (Press enter to skip) ");
        String architectPhysicalAddress = scanner.nextLine().trim();
        if (!architectPhysicalAddress.isEmpty()) {
            architect.setArchitectPhysicalAddress(architectPhysicalAddress);
        }

        // Update Engineer Details
        System.out.print("Enter Engineer Name (Current: " + engineer.getEngineerName() + "): (Press enter to skip) ");
        String engineerName = scanner.nextLine().trim();
        if (!engineerName.isEmpty()) {
            engineer.setEngineerName(engineerName);
        }

        System.out.print("Enter Engineer Phone Number (Current: " + engineer.getEngineerPhoneNumber() + "): (Press enter to skip) ");
        String engineerPhone = scanner.nextLine().trim();
        if (!engineerPhone.isEmpty()) {
            engineer.setEngineerPhoneNumber(engineerPhone);
        }

        System.out.print("Enter Engineer Email (Current: " + engineer.getEngineerEmail() + "): (Press enter to skip) ");
        String engineerEmail = scanner.nextLine().trim();
        if (!engineerEmail.isEmpty()) {
            engineer.setEngineerEmail(engineerEmail);
        }

        System.out.print("Enter Engineer Physical Address (Current: " + engineer.getEngineerPhysicalAddress() + "): (Press enter to skip) ");
        String engineerPhysicalAddress = scanner.nextLine().trim();
        if (!engineerPhysicalAddress.isEmpty()) {
            engineer.setEngineerPhysicalAddress(engineerPhysicalAddress);
        }

        // Call the update method with the collected data
        handleUpdate(customer, architect, engineer, project);

        // Commit the transaction
        connection.commit();
        System.out.println("Update successful!");

	    } catch (SQLException e) {
	        System.err.println("Transaction error: " + e.getMessage());
	        try {
	            // Rollback the transaction if any error occurs
	            connection.rollback();
	            System.out.println("Transaction rolled back.");
	        } catch (SQLException rollbackEx) {
	            System.err.println("Rollback error: " + rollbackEx.getMessage());
	        }
	    } finally {
	        try {
	            // Reset auto-commit to true
	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            System.err.println("Failed to reset auto-commit: " + e.getMessage());
	        }
	    }
	}


  /**
   * Captures and deletes a project based on user input.
   *
   * @param scanner the Scanner object for user input.
   */
  public void captureAndDelete(Scanner scanner) {
    // Display projects.
    readPeopleAndProjects();

    System.out.print("Enter Project ID you wish to delete: ");
    int projectId = getValidInt(scanner);

    // Check if the project ID exists
    String checkProjectSQL = "SELECT COUNT(*) FROM Project WHERE ProjectID = ?";

    try (PreparedStatement checkStatement = connection.prepareStatement(checkProjectSQL)) {
      checkStatement.setInt(1, projectId);
      ResultSet resultSet = checkStatement.executeQuery();

      // Check if the project exists
      if (resultSet.next() && resultSet.getInt(1) == 0) {
        System.out.println("Project ID " + projectId + " does not exist.");
        return; 
      }
    } catch (SQLException e) {
      System.err.println("An error occurred while checking project existence: " + e.getMessage());
      return;
    }

    // Fetch the project details using the projectId
    Map<String, Object> projectDetails = fetchProjectDetails(projectId);
    if (projectDetails == null) {
      System.out.println("Failed to retrieve project details.");
      return;
    }

    // Extract necessary IDs for deletion
    int engineerId = (int) projectDetails.get("EngineerID");
    int customerId = (int) projectDetails.get("CustomerID");
    int architectId = (int) projectDetails.get("ArchitectID");

    // Proceed to delete the project and associated records
    try {
      deleteProject(projectId, engineerId, customerId, architectId);
      System.out.println("Project deleted successfully.");
    } catch (SQLException e) {
      System.err.println("An error occurred while deleting the project: " + e.getMessage());
    }
  }


  /**
   * Method to finalize a project based on user input.
   *
   * @param scanner the Scanner object for user input.
   */
  public void finaliseProject(Scanner scanner) {
    // Display projects.
    readPeopleAndProjects(); 

    System.out.print("Enter the Project ID of the completed project: ");
    int projectId = getValidInt(scanner);

    // Check if the project ID exists
    String checkProjectSQL = "SELECT COUNT(*) FROM Project WHERE ProjectID = ?";

    try (PreparedStatement checkStatement = connection.prepareStatement(checkProjectSQL)) {
      checkStatement.setInt(1, projectId);
      ResultSet resultSet = checkStatement.executeQuery();

      // Check if the project exists
      if (resultSet.next() && resultSet.getInt(1) == 0) {
        System.out.println("Project ID " + projectId + " does not exist.");
        return; 
      }
    } catch (SQLException e) {
      System.err.println("An error occurred while checking project existence: " + e.getMessage());
      return;
    }

    // Prompt user for completion date.
    System.out.print("Enter the date of completion (YYYY-MM-DD): ");
    LocalDate completionDate = getValidDate(scanner); 

    // SQL query to update the project as finalized
    String finalisedProjectSQL = """
      UPDATE Project SET
      Finalized = 1,
      CompletionDate = ?
      WHERE ProjectID = ?;
    """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(finalisedProjectSQL)) {
      preparedStatement.setDate(1, java.sql.Date.valueOf(completionDate));
      preparedStatement.setInt(2, projectId);

      int rowsUpdated = preparedStatement.executeUpdate();
      // Print if it was successful.
      if (rowsUpdated > 0) {
        System.out.println("Project finalized successfully.");
      } else {
        System.out.println("No project was updated. Please check the Project ID.");
      }
    } catch (SQLException e) {
      System.err.println("An error occurred while finalizing the project: " + e.getMessage());
    }
  }

  /**
   * Method to display incomplete projects.
   */
  public void incompleteProjects() {
    // SQL query to select incomplete projects
    String incompleteSQL = """
      SELECT * FROM Project
      WHERE Finalized = 0 OR CompletionDate IS NULL ;
      """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(incompleteSQL)) {
      ResultSet resultSet = preparedStatement.executeQuery();

      // Process the results
      while (resultSet.next()) {
        // Retrieve project details.
        int id = resultSet.getInt("ProjectID");
        String name = resultSet.getString("ProjectName");
        String buildingType = resultSet.getString("BuildingType");
        String projectAddress = resultSet.getString("PhysicalAddress");
        String erfNumber = resultSet.getString("ERFNumber");
        double totalFee = resultSet.getDouble("TotalFee");
        double totalPaid = resultSet.getDouble("TotalPaid");
        Date deadline = resultSet.getDate("Deadline");
        boolean finalized = resultSet.getBoolean("Finalized");
        Date completionDate = resultSet.getDate("CompletionDate");

        // Print the project details
        System.out.println("ProjectID: " + id);
        System.out.println("ProjectName: " + name);
        System.out.println("BuildingType: " + buildingType);
        System.out.println("PhysicalAddress: " + projectAddress);
        System.out.println("ERFNumber: " + erfNumber);
        System.out.println("TotalFee: " + totalFee);
        System.out.println("TotalPaid: " + totalPaid);
        System.out.println("Deadline: " + deadline);
        System.out.println("Finalized: " + finalized);
        System.out.println("CompletionDate: " + completionDate);
        
        System.out.println("-------------------------------------------------");
      }
    } catch (SQLException e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }

  /**
   * Method to find projects past their due date.
   *
   * @param scanner the Scanner object for user input.
   */
  public void pastDueDate(Scanner scanner) {
	  // Prompt user for current date.
	  System.out.print("Please enter the current date (YYYY-MM-DD): ");
	  LocalDate currentDate = getValidDate(scanner);
	
	  // SQL query to select projects with completion dates earlier than the current date or NULL
	  // and a deadline before the current date
	  String pastDueDateSQL = """
	    SELECT ProjectName, Deadline, CompletionDate, Finalized 
	    FROM Project 
	    WHERE (CompletionDate < ? OR CompletionDate IS NULL) 
	      AND (Finalized = false OR Finalized IS NULL)
	      AND Deadline < ?; 
	  """;
	
	  try (PreparedStatement preparedStatement = connection.prepareStatement(pastDueDateSQL)) {
	      // Set the current date for both conditions: CompletionDate and Deadline
	      preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
	      preparedStatement.setDate(2, java.sql.Date.valueOf(currentDate));
	
	      // Execute the query
	      ResultSet resultSet = preparedStatement.executeQuery();
	
	      // Process the results
	      while (resultSet.next()) {
	          String projectName = resultSet.getString("ProjectName");
	          java.sql.Date deadline = resultSet.getDate("Deadline");
	          System.out.println("Project Name: " + projectName + ", Deadline: " + deadline);
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	  }
 

  /**
   * Method to find projects by number or name.
   *
   * @param scanner the Scanner object for user input.
   */
  public void findByNumberOrName(Scanner scanner) {
	// Prompt for project name or ID or both.
	System.out.print("Enter Project ID: (Press enter to skip) ");
	int projectId = getValidInt(scanner);  // Get valid project ID

	System.out.print("Enter Project Name: ");
	String projectName = scanner.nextLine();  

    // SQL query to find a project by ID or name
    String numberOrNameSQL = "SELECT * FROM Project WHERE ProjectID = ? OR ProjectName = ?";

    // Initialize a flag to check if any results were found
    boolean found = false;

    try (PreparedStatement preparedStatement = connection.prepareStatement(numberOrNameSQL)) {
      preparedStatement.setInt(1, projectId);
      preparedStatement.setString(2, projectName);

      // Use executeQuery to retrieve results
      ResultSet resultSet = preparedStatement.executeQuery();

      // Process the results
      while (resultSet.next()) {
        // Set the flag to true if at least one project is found
        found = true; 
        // Extract data from the result set
        int id = resultSet.getInt("ProjectID");
        String name = resultSet.getString("ProjectName");
        String buildingType = resultSet.getString("BuildingType");
        String projectAddress = resultSet.getString("PhysicalAddress");
        String erfNumber = resultSet.getString("ERFNumber");
        double totalFee = resultSet.getDouble("TotalFee");
        double totalPaid = resultSet.getDouble("TotalPaid");
        Date deadline = resultSet.getDate("Deadline");
        boolean finalized = resultSet.getBoolean("Finalized");
        Date completionDate = resultSet.getDate("CompletionDate");

        // Print the project details
        System.out.println("ProjectID: " + id);
        System.out.println("ProjectName: " + name);
        System.out.println("BuildingType: " + buildingType);
        System.out.println("PhysicalAddress: " + projectAddress);
        System.out.println("ERFNumber: " + erfNumber);
        System.out.println("TotalFee: " + totalFee);
        System.out.println("TotalPaid: " + totalPaid);
        System.out.println("Deadline: " + deadline);
        System.out.println("Finalized: " + finalized);
        System.out.println("CompletionDate: " + completionDate);
      }

      // If no project was found, notify the user
      if (!found) {
        System.out.println("No projects found with the given ID or name.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}