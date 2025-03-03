/**
 * Represents a customer with personal and contact information.
 */
public class Customer {
  
  // Attributes
  private int customerId;
  private String customerName;
  private String customerPhoneNumber;
  private String customerEmail;
  private String customerPhysicalAddress;

  /**
   * Constructs a Customer with the specified details.
   *
   * @param customerId              the unique identifier for the customer
   * @param customerName            the name of the customer
   * @param customerPhoneNumber     the phone number of the customer
   * @param customerEmail           the email address of the customer
   * @param customerPhysicalAddress  the physical address of the customer
   */
  public Customer(int customerId, String customerName, String customerPhoneNumber,
                  String customerEmail, String customerPhysicalAddress) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.customerPhoneNumber = customerPhoneNumber;
    this.customerEmail = customerEmail;
    this.customerPhysicalAddress = customerPhysicalAddress;
  }

  /**
   * Returns the customer's unique identifier.
   *
   * @return the customer's unique identifier
   */
  public int getCustomerId() {
    return customerId;
  }

  /**
   * Sets the customer's unique identifier.
   *
   * @param customerId the unique identifier for the customer
   */
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  /**
   * Returns the customer's name.
   *
   * @return the customer's name
   */
  public String getCustomerName() {
    return customerName;
  }

  /**
   * Sets the customer's name.
   *
   * @param customerName the name of the customer
   */
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  /**
   * Returns the customer's phone number.
   *
   * @return the customer's phone number
   */
  public String getCustomerPhoneNumber() {
    return customerPhoneNumber;
  }

  /**
   * Sets the customer's phone number.
   *
   * @param customerPhoneNumber the phone number of the customer
   */
  public void setCustomerPhoneNumber(String customerPhoneNumber) {
    this.customerPhoneNumber = customerPhoneNumber;
  }

  /**
   * Returns the customer's email address.
   *
   * @return the customer's email address
   */
  public String getCustomerEmail() {
    return customerEmail;
  }

  /**
   * Sets the customer's email address.
   *
   * @param customerEmail the email address of the customer
   */
  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  /**
   * Returns the customer's physical address.
   *
   * @return the customer's physical address
   */
  public String getCustomerPhysicalAddress() {
    return customerPhysicalAddress;
  }

  /**
   * Sets the customer's physical address.
   *
   * @param customerPhysicalAddress the physical address of the customer
   */
  public void setCustomerPhysicalAddress(String customerPhysicalAddress) {
    this.customerPhysicalAddress = customerPhysicalAddress;
  }
}


