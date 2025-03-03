/**
 * Represents an engineer with personal and contact information.
 */
public class Engineer {

  // Attributes
  private int engineerId;
  private String engineerName;
  private String engineerPhoneNumber;
  private String engineerEmail;
  private String engineerPhysicalAddress;

  /**
   * Constructs an Engineer with the specified details.
   *
   * @param engineerId              the unique identifier for the engineer
   * @param engineerName            the name of the engineer
   * @param engineerPhoneNumber     the phone number of the engineer
   * @param engineerEmail           the email address of the engineer
   * @param engineerPhysicalAddress  the physical address of the engineer
   */
  public Engineer(int engineerId, String engineerName, String engineerPhoneNumber,
                  String engineerEmail, String engineerPhysicalAddress) {
    this.engineerId = engineerId;
    this.engineerName = engineerName;
    this.engineerPhoneNumber = engineerPhoneNumber;
    this.engineerEmail = engineerEmail;
    this.engineerPhysicalAddress = engineerPhysicalAddress;
  }

  /**
   * Returns the engineer's unique identifier.
   *
   * @return the engineer's unique identifier
   */
  public int getEngineerId() {
    return engineerId;
  }

  /**
   * Sets the engineer's unique identifier.
   *
   * @param engineerId the unique identifier for the engineer
   */
  public void setEngineerId(int engineerId) {
    this.engineerId = engineerId;
  }

  /**
   * Returns the engineer's name.
   *
   * @return the engineer's name
   */
  public String getEngineerName() {
    return engineerName;
  }

  /**
   * Sets the engineer's name.
   *
   * @param engineerName the name of the engineer
   */
  public void setEngineerName(String engineerName) {
    this.engineerName = engineerName;
  }

  /**
   * Returns the engineer's phone number.
   *
   * @return the engineer's phone number
   */
  public String getEngineerPhoneNumber() {
    return engineerPhoneNumber;
  }

  /**
   * Sets the engineer's phone number.
   *
   * @param engineerPhoneNumber the phone number of the engineer
   */
  public void setEngineerPhoneNumber(String engineerPhoneNumber) {
    this.engineerPhoneNumber = engineerPhoneNumber;
  }

  /**
   * Returns the engineer's email address.
   *
   * @return the engineer's email address
   */
  public String getEngineerEmail() {
    return engineerEmail;
  }

  /**
   * Sets the engineer's email address.
   *
   * @param engineerEmail the email address of the engineer
   */
  public void setEngineerEmail(String engineerEmail) {
    this.engineerEmail = engineerEmail;
  }

  /**
   * Returns the engineer's physical address.
   *
   * @return the engineer's physical address
   */
  public String getEngineerPhysicalAddress() {
    return engineerPhysicalAddress;
  }

  /**
   * Sets the engineer's physical address.
   *
   * @param engineerPhysicalAddress the physical address of the engineer
   */
  public void setEngineerPhysicalAddress(String engineerPhysicalAddress) {
    this.engineerPhysicalAddress = engineerPhysicalAddress;
  }
}


