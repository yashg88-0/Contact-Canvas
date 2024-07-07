# Contact Canvas

Contact Canvas is a web-based application designed for managing user contacts efficiently. It allows users to save, view, and manage their contacts with an intuitive interface.

## Features

- **User Authentication**: Secure login and registration for users.
- **Contact Management**: Add, edit, and delete contacts.
- **Image Upload**: Upload and associate images with contacts.
- **Password Validation**: Ensure strong and matching passwords during registration.
- **Custom Queries**: Efficient data retrieval using custom queries.

## Technologies Used

- **Java 17**: Backend language.
- **Spring Boot**: Framework for building the application.
- **Spring Security**: Provides authentication and authorization features.
- **Thymeleaf**: Templating engine for rendering web pages.
- **MySQL**: Database for storing user and contact information.
- **HTML/CSS/JavaScript**: Frontend technologies for building the user interface.

## Getting Started

### Prerequisites

- JDK 17
- MySQL
- Maven

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yashg88-0/Contact-Canvas.git
    cd Contact-Canvas
    ```

2. **Configure the database**:
    - Update the `application.properties` file with your MySQL database credentials.

3. **Build the project**:
    ```bash
    mvn clean install
    ```

4. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

### Usage

- Access the application at `http://localhost:8448`.
- Register a new user account or log in with existing credentials.
- Start managing your contacts by adding new ones, editing existing contacts, and deleting contacts no longer needed.

## Project Structure

- **src/main/java**: Contains the Java source files.
- **src/main/resources**: Contains the application properties and static resources.
- **src/main/webapp**: Contains the Thymeleaf templates.
- **pom.xml**: Maven configuration file.

## Contribution

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.
