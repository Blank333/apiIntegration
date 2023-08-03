# Spring Boot Application

This is a Spring Boot application that provides REST API endpoints for managing customer data. The application allows you to create, read, update, and delete customer information. Runs on a test API server (may or may not work).

## Prerequisites

Before running the application, make sure you have the following installed:

- Java
- Maven

## Getting Started

1. Clone the repository to your local machine.

2. Build the application using Maven:

   ```
   mvn clean package
   ```

3. Run the application:

   The application will start on `http://localhost:63342/apiIntegration/client/login.html?`.
   
   The api endpoints are on `http://localhost:8080/`

## API Endpoints

### User authentication

- URL: `/api/authenticate`
- Method: POST
- Request Body: JSON object containing customer details (first_name, last_name, street, address, city, state, email, phone)
- Login ID: test@sunbasedata.com Password: Test@123

### Create Customer

- URL: `/api/customer-form`
- Method: POST
- Request Body: JSON object containing customer details (first_name, last_name, street, address, city, state, email, phone)

### Get All Customers

- URL: `/api/customers`
- Method: GET
- Response: JSON array of all customers

### Update Customer

- URL: `/api/customer-form/{uuid}`
- Method: POST
- Request Body: JSON object containing updated customer details (first_name, last_name, street, address, city, state, email, phone)

### Delete Customer

- URL: `/api/delete-customer/{uuid}`
- Method: POST

## Error Handling

The application handles errors and returns appropriate HTTP status codes in case of invalid requests or other errors.

## Dependencies

The application uses the following dependencies:

- Spring Boot: for creating the Spring application and REST endpoints
- Lombok: for reducing boilerplate code with annotations
- Jackson: Parsing JSON data

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
