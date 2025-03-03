import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a project with details about its attributes and associated personnel.
 */
public class Project {
  
  // Attributes
  private int projectId;
  private String projectName;
  private String buildingType;
  private String physicalAddress;
  private String erfNumber;
  private double totalFee;
  private double totalPaid;
  private Date deadline;
  private int architectId;
  private int engineerId;
  private int customerId;

  /**
   * Constructs a Project with the specified details.
   *
   * @param projectId              the unique identifier for the project
   * @param projectName            the name of the project
   * @param buildingType           the type of building for the project
   * @param physicalAddress        the physical address of the project
   * @param erfNumber              the erf number of the project
   * @param totalFee               the total fee for the project
   * @param totalPaid              the total amount paid for the project
   * @param deadline               the deadline for project completion
   * @param architectId            the ID of the associated architect
   * @param engineerId             the ID of the associated engineer
   * @param customerId             the ID of the associated customer
   */
  public Project(int projectId, String projectName, String buildingType, String physicalAddress,
      String erfNumber, double totalFee, double totalPaid, Date deadline, int architectId,
      int engineerId, int customerId) {
    this.projectId = projectId;
    this.projectName = projectName;
    this.buildingType = buildingType;
    this.physicalAddress = physicalAddress;
    this.erfNumber = erfNumber;
    this.totalFee = totalFee;
    this.totalPaid = totalPaid;
    this.deadline = deadline;
    this.architectId = architectId;
    this.engineerId = engineerId;
    this.customerId = customerId;
  }

  /**
   * Returns the project's unique identifier.
   *
   * @return the project's unique identifier
   */
  public int getProjectId() {
    return projectId;
  }

  /**
   * Sets the project's unique identifier.
   *
   * @param projectId the unique identifier for the project
   */
  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  /**
   * Returns the project's name.
   *
   * @return the project's name
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * Sets the project's name.
   *
   * @param projectName the name of the project
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * Returns the type of building for the project.
   *
   * @return the type of building for the project
   */
  public String getBuildingType() {
    return buildingType;
  }

  /**
   * Sets the type of building for the project.
   *
   * @param buildingType the type of building for the project
   */
  public void setBuildingType(String buildingType) {
    this.buildingType = buildingType;
  }

  /**
   * Returns the physical address of the project.
   *
   * @return the physical address of the project
   */
  public String getPhysicalAddress() {
    return physicalAddress;
  }

  /**
   * Sets the physical address of the project.
   *
   * @param physicalAddress the physical address of the project
   */
  public void setPhysicalAddress(String physicalAddress) {
    this.physicalAddress = physicalAddress;
  }

  /**
   * Returns the erf number of the project.
   *
   * @return the erf number of the project
   */
  public String getErfNumber() {
    return erfNumber;
  }

  /**
   * Sets the erf number of the project.
   *
   * @param erfNumber the erf number of the project
   */
  public void setErfNumber(String erfNumber) {
    this.erfNumber = erfNumber;
  }

  /**
   * Returns the total fee for the project.
   *
   * @return the total fee for the project
   */
  public double getTotalFee() {
    return totalFee;
  }

  /**
   * Sets the total fee for the project.
   *
   * @param totalFee the total fee for the project
   */
  public void setTotalFee(double totalFee) {
    this.totalFee = totalFee;
  }

  /**
   * Returns the total amount paid for the project.
   *
   * @return the total amount paid for the project
   */
  public double getTotalPaid() {
    return totalPaid;
  }

  /**
   * Sets the total amount paid for the project.
   *
   * @param totalPaid the total amount paid for the project
   */
  public void setTotalPaid(double totalPaid) {
    this.totalPaid = totalPaid;
  }

  /**
   * Returns the deadline for project completion.
   *
   * @return the deadline for project completion
   */
  public Date getDeadline() {
    return deadline;
  }

  /**
   * Sets the deadline for project completion.
   *
   * @param deadline the deadline for project completion
   */
  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  /**
   * Returns the architect's unique identifier associated with the project.
   *
   * @return the architect's unique identifier associated with the project
   */
  public int getArchitectId() {
    return architectId;
  }

  /**
   * Sets the architect's unique identifier associated with the project.
   *
   * @param architectId the architect's unique identifier associated with the project
   */
  public void setArchitectId(int architectId) {
    this.architectId = architectId;
  }

  /**
   * Returns the engineer's unique identifier associated with the project.
   *
   * @return the engineer's unique identifier associated with the project
   */
  public int getEngineerId() {
    return engineerId;
  }

  /**
   * Sets the engineer's unique identifier associated with the project.
   *
   * @param engineerId the engineer's unique identifier associated with the project
   */
  public void setEngineerId(int engineerId) {
    this.engineerId = engineerId;
  }

  /**
   * Returns the customer's unique identifier associated with the project.
   *
   * @return the customer's unique identifier associated with the project
   */
  public int getCustomerId() {
    return customerId;
  }

  /**
   * Sets the customer's unique identifier associated with the project.
   *
   * @param customerId the customer's unique identifier associated with the project
   */
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  /**
   * Establishes a connection to the database.
   *
   * @param url      the database URL
   * @param user     the database username
   * @param password the database password
   * @return a Connection object to the database
   * @throws SQLException if a database access error occurs
   */
  public static Connection getConnection(String url, String user, String password) throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }
}

