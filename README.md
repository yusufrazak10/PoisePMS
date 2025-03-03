# Project Management System




## Overview
The Project Management System (PMS) is a Java-based application designed to manage various aspects of project operations, including adding, updating, deleting, and finalizing projects. It interacts with a MySQL database to store and retrieve project-related information. JavaDoc provides detailed information about the code structure and functionality.




## Table of Contents




1. [Getting Started](#getting-started)
2. [Screenshot](#screenshot)
3. [Author](#author)
4. [License](#license)




### Prerequisites
Before running the application, make sure you have the following installed:


- **Java Development Kit (JDK)**: [Download Link](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **MySQL Database**: [Download Link](https://dev.mysql.com/downloads/installer/)
- **JDBC Driver for MySQL**: [Download Link](https://dev.mysql.com/downloads/connector/j/)


## Getting Started


Setup


1. Clone the Repository
Clone the repository to your local machine:
git clone https://github.com/yusufrazak/PoisePMS.git


2. Set up MySQL Database
Log into your MySQL server:
mysql -u root -p
Enter the MySQL root password when prompted.


Create a new database for the application:
CREATE DATABASE PoisePMS;


3. Import the Database Schema
Navigate to the directory where dump_file.sql is located (inside your PoisePMS project folder):
cd /path/to/your/PoisePMS


Import the database schema and initial data into PoisePMS:
mysql -u root -p PoisePMS < dump_file.sql
This will create the necessary tables and populate them with sample data for the Architect, Customer, Engineer, and Project tables.


4. Configure Database Connection
Update the database connection details in your Java application. This is usually done in a configuration file (db.properties or similar) or directly in the Java code.


Example configuration (often found in db.properties):
db.url=jdbc:mysql://localhost:3306/PoisePMS
db.username=root
db.password=your_password
Replace your_password with your actual MySQL root password.
Ensure the database URL (jdbc:mysql://localhost:3306/PoisePMS) points to the correct database (PoisePMS in this case).


5. Compile the Java Application
If you haven’t already compiled the Java application, you can do so using your IDE (like Eclipse) or manually using the command line:
javac -cp ".:path/to/mysql-connector-java.jar" src/*.java
Replace path/to/mysql-connector-java.jar with the actual path where the MySQL JDBC driver is located.
If you're using an IDE like Eclipse, this will be done automatically when you run the project.


6. Run the Application
Once the Java code is compiled, you can run the application from the command line:
java -cp ".:path/to/mysql-connector-java.jar" com.projectmanagement.Main
Replace path/to/mysql-connector-java.jar with the correct path to the MySQL JDBC driver.
If everything is configured correctly, the application should launch, and you can begin interacting with the project management system.




## Screenshot
![Menu Screenshot](Screenshot 2024-11-14 at 02.23.29.png)




## Author
- **Yusuf Razak**  
  Email: [razakyusufjoe@gmail.com](mailto:razakyusufjoe@gmail.com)  
  GitHub: [@yusufrazak](https://github.com/yusufrazak)
