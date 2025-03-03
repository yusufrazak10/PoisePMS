/**
 * Represents an architect with personal and contact information.
 */
public class Architect {

  // Attributes
  private int architectId;
  private String architectName;
  private String architectPhoneNumber;
  private String architectEmail;
  private String architectPhysicalAddress;

  /**
   * Constructs an Architect with the specified details.
   *
   * @param architectId              the unique identifier for the architect
   * @param architectName            the name of the architect
   * @param architectPhoneNumber     the phone number of the architect
   * @param architectEmail           the email address of the architect
   * @param architectPhysicalAddress  the physical address of the architect
   */
  public Architect(int architectId, String architectName, String architectPhoneNumber,
                   String architectEmail, String architectPhysicalAddress) {
    this.architectId = architectId;
    this.architectName = architectName;
    this.architectPhoneNumber = architectPhoneNumber;
    this.architectEmail = architectEmail;
    this.architectPhysicalAddress = architectPhysicalAddress;
  }

  /**
   * Returns the architect's unique identifier.
   *
   * @return the architect's unique identifier
   */
  public int getArchitectId() {
    return architectId;
  }

  /**
   * Sets the architect's unique identifier.
   *
   * @param architectId the unique identifier for the architect
   */
  public void setArchitectId(int architectId) {
    this.architectId = architectId;
  }

  /**
   * Returns the architect's name.
   *
   * @return the architect's name
   */
  public String getArchitectName() {
    return architectName;
  }

  /**
   * Sets the architect's name.
   *
   * @param architectName the name of the architect
   */
  public void setArchitectName(String architectName) {
    this.architectName = architectName;
  }

  /**
   * Returns the architect's phone number.
   *
   * @return the architect's phone number
   */
  public String getArchitectPhoneNumber() {
    return architectPhoneNumber;
  }

  /**
   * Sets the architect's phone number.
   *
   * @param architectPhoneNumber the phone number of the architect
   */
  public void setArchitectPhoneNumber(String architectPhoneNumber) {
    this.architectPhoneNumber = architectPhoneNumber;
  }

  /**
   * Returns the architect's email address.
   *
   * @return the architect's email address
   */
  public String getArchitectEmail() {
    return architectEmail;
  }

  /**
   * Sets the architect's email address.
   *
   * @param architectEmail the email address of the architect
   */
  public void setArchitectEmail(String architectEmail) {
    this.architectEmail = architectEmail;
  }

  /**
   * Returns the architect's physical address.
   *
   * @return the architect's physical address
   */
  public String getArchitectPhysicalAddress() {
    return architectPhysicalAddress;
  }

  /**
   * Sets the architect's physical address.
   *
   * @param architectPhysicalAddress the physical address of the architect
   */
  public void setArchitectPhysicalAddress(String architectPhysicalAddress) {
    this.architectPhysicalAddress = architectPhysicalAddress;
  }
}
