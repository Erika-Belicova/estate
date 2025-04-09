# Estate Application

Estate is a platform designed to connect tenants and landlords, enabling users to browse available properties, manage their own rental listings, and send messages to property owners. The back-end application handles these interactions through a REST API, connecting with an existing front-end application.

This back-end solution is built with **Java** and **Spring Boot**, providing authentication and security through **Spring Security**, and integrates with a **MySQL** database to store property and user data. It communicates with the provided **Angular front-end** to deliver a full-stack application for property rental management.

## Table of Contents

1.  Prerequisites  

2.  Clone the Project from GitHub

3.  Database Setup

4.  Back-End Setup

5.  Front-End Setup

6.  Swagger Documentation
  
7.  Testing the Application  

8.  Technologies and Libraries Used  

10.  Troubleshooting  

## Prerequisites

Before running the application, you need to have the following tools installed on your machine:

-   **Node.js** (version 14 or higher)  
   https://nodejs.org/  
    To check if Node.js is already installed, run:  
    `node -v`
    
-   **Angular CLI** (version 14 or higher)  
    To install Angular CLI globally, run the following command:  
    `npm install -g @angular/cli`  
      
-   **MySQL** (for database setup)  	 	
	https://dev.mysql.com/downloads/installer/  
    To check if MySQL is installed, open the MySQL application on your computer (for example, MySQL Workbench or the MySQL Command Line Client). 
    
-   **Java 11 or 17** (required for running the backend)  
	https://www.oracle.com/java/technologies/javase-jdk11-downloads.html 
    To check if Java is installed, run:  
    `java -version`
    
-   **Maven** (for managing Java dependencies)  
	https://maven.apache.org/download.cgi  
    To check if Maven is installed, run:   
   ` mvn -v`

## Clone the Project from GitHub

Open your terminal or command prompt. Clone the GitHub repository of the project using the following command:

`git clone https://github.com/Erika-Belicova/estate`

Replace the username and repository-name with your GitHub username and the project name, respectively.

Navigate into the cloned project directory:

`cd repository-name`

You will see two main folders:

-   **estate-front-end** for the Angular front-end application  
-   **estate-back-end** for the Spring Boot back-end application

## Database Setup

Ensure that **MySQL** is installed (as mentioned in the **Prerequisites** section).

### Start MySQL Server
Before running the back-end, ensure that MySQL is running on your machine. 

Open the MySQL application on your computer (for example, MySQL Workbench or the MySQL Command Line Client). Upon opening it, you'll be prompted to enter the root password you set during installation. In MySQL Command Line Client, you’ll then see the MySQL prompt (e.g., mysql>); in MySQL Workbench, a graphical interface will open where you can browse databases and run SQL queries.
  
### Create the Database  
In MySQL, create a new database for your application. You can run the following commands to create and use the database:  
  
```
CREATE DATABASE estate_application_db;
USE estate_application_db;
```

-   The `CREATE DATABASE` command creates a new database called `estate_application_db`.
-   The `USE` command tells MySQL to switch to the newly created database, so any future operations (like creating tables or inserting data) will be done in this database.


### Run the SQL Script to Initialize the Database
Once the database is set up, you need to run the SQL script to initialize the required tables. The SQL script is located at:
 
`estate-back-end/src/main/resources/sql/script.sql`

Open your MySQL application (such as MySQL Workbench or the MySQL Command Line Client) and do one of the following:

-   In **MySQL Workbench**, go to **File > Open**, navigate to the `script.sql` file, and open it in a new SQL tab. Then click **Execute** (the lightning bolt icon) to run the script.

-   In the **MySQL Command Line Client**, use the `source` command to run the script:

     `source path\to\script.sql`;

      Replace `path\to\script.sql` with the full path to the file on your system.

This will create the necessary tables for the back-end to function correctly.  
  
### Create and Configure the .env File  
In order to connect the Spring Boot back-end with your MySQL database, you will need to create a .env file to store your MySQL credentials.

-   Navigate to the **estate-back-end** directory (where your **pom.xml** file is located).
-   Create a file named **.env** in this directory.  
      
In the **.env** file, add the following variables:  
  
```
DATABASE_USERNAME=your_mysql_username
DATABASE_PASSWORD=your_mysql_password
```

-   Replace **your_mysql_username** with your MySQL username (usually **root** if using default settings).
-   Replace **your_mysql_password** with your MySQL password.  

### Configure the Application to Connect to MySQL
You need to ensure that Spring Boot is connected to the database. This is done through the application properties file.
  
In your **estate-back-end/src/main/resources/application.properties**, make sure you have the correct database configuration. The Spring Boot application will read the database username and password from the **.env** file.  
  
The configuration should look like this:  
  
```
spring.datasource.url=jdbc:mysql://localhost:3306/estate_application_db?serverTimeZone=UTC
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

-   Make sure the name `estate_application_db` matches the database you created earlier.
-   The **\${DATABASE_USERNAME}** and **\${DATABASE_PASSWORD}** values will be loaded from the **.env** file.  

_____

### Important Notes:
-   **MySQL Running:** MySQL must be running for the back-end to connect to the database. Otherwise the database will fail to connect.  
      
-   **Database Configuration:** The back-end will look for the database configuration in the .env file, so make sure that your MySQL credentials and database name are correctly set there.  
      
-   **.env File:** The **.env** file should be placed in the root directory of the **estate-back-end** folder, alongside the **pom.xml** file. Make sure the values in this file are correct before running the application.  

_____

### Empty the Database for Testing
If you want to empty the database during testing (for example, when you want to reset the data), you can delete all records from the tables by running the following SQL commands in this order:

```
DELETE FROM MESSAGES;
DELETE FROM RENTALS;
DELETE FROM USERS;
```

This will remove all data from the tables while keeping the table structure intact, which is useful for resetting data during tests. The related records in other tables will also be deleted automatically.

### Reinitialize the Database
If you want to completely reinitialize the database (drop all tables and recreate them), you can run the following SQL commands:

```
DROP DATABASE IF EXISTS estate_application_db;
CREATE DATABASE estate_application_db;
USE estate_application_db;
```

Then re-run the SQL script to reinitialize the tables.

## Back-End Setup

### Generate and Set the JWT Secret Key

The application uses JSON Web Tokens (JWT) for authentication and security. To enable this, you need to define a secret key in the **.env** file located in the **estate-back-end** directory. This key is used to sign and verify tokens, ensuring secure user authentication.
 
Add the following variable in the **.env** file:  
  
`JWT_SECRET_KEY=your_jwt_secret_key`

You can generate a 256-bit secret key for **JWT_SECRET_KEY** from a key generator like https://generate-random.org/encryption-key-generator.
_____

### Install and Run the Back-End

Ensure that you have **Java** and **Maven** installed (as mentioned in the **Prerequisites** section).

Navigate to the **estate-back-end** folder:

`cd estate-back-end`

#### Install Back-End Dependencies using Maven:

`mvn clean install`
  
#### Running the Back-End

Once the dependencies are installed, run the back-end Spring Boot application with:

`mvn spring-boot:run`

The back-end API will be available at http://localhost:3001.

## Front-end Setup

The front-end part of this project has been pre-configured, and you can run it alongside the back-end for a fully functional application.

Ensure that you have **Node.js** and **Angular CLI** installed (as mentioned in the **Prerequisites** section).

#### Navigate into the front-end project directory:  
  
`cd estate-front-end`

#### Install front-end dependencies:  

`npm install`

#### Start the front-end application:  
  
`npm run start`

The Angular application will be available at http://localhost:4200.  
  
## Swagger Documentation

Once the back-end is running, you can access the API documentation at:

`http://localhost:3001/swagger-ui.html`

This Swagger UI will provide details on all the available API endpoints.

## Testing the Application

To test the application:

**Open your browser and go to:**  
  
http://localhost:4200

**You should see the front-end UI, which will interact with the back-end API running at** http://localhost:3001.

## Technologies, Frameworks, and Libraries Used

#### Back-End:

-   **Java (11 or 17):** Programming language for back-end development.  

-   **Spring Boot:** Framework for building the back-end application.  

-   **Spring Security:** Handles authentication and authorization using JWT.  

-   **MySQL:** Relational database used to store application data.  

-   **Maven:** Dependency management and build automation tool for Java.  

-   **Swagger:** Provides API documentation for easy testing and reference.  
      
#### Front-End:

-   **Angular (14 or higher):** JavaScript framework for building the user interface.  
      
-   **Node.js (14 or higher):** Required for running Angular applications.  
      
-   **Angular CLI (14 or higher):** Command-line interface for managing the Angular project.

## Troubleshooting

1.  **Issue:** The back-end or front-end doesn’t start.
  
	**Solution:** Ensure that all dependencies are installed. Run the following:

  -   For back-end: `mvn clean install and mvn spring-boot:run`
    
  -   For front-end: `npm install and npm run start`

2.  **Issue:** API endpoints are not responding.
    
	**Solution:** Check the browser console for any errors. Make sure that the back-end is running at **http://localhost:3001** and that the front-end is correctly pointing to the back-end API.
