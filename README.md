# Library Management System

## Overview

This application is a CRUD system designed to manage a library. The system allows you to manage authors, books, users, and book loans. Each book is associated with an author, and each user can have one or more books on loan. The application is built using Clojure and utilizes a MySQL database for data storage.

## Relational Model

### Tables and Attributes

#### Author
| Column Name   | Data Type    | Description                        |
|---------------|--------------|------------------------------------|
| `id`          | INT          | Primary Key, Auto-Increment        |
| `name`        | VARCHAR(100) | Name of the author                 |
| `birth_year`  | INT          | Birth year of the author           |

#### Book
| Column Name     | Data Type    | Description                            |
|-----------------|--------------|----------------------------------------|
| `id`            | INT          | Primary Key, Auto-Increment            |
| `title`         | VARCHAR(200) | Title of the book                      |
| `genre`         | VARCHAR(100) | Genre of the book                      |
| `year_published`| INT          | Year the book was published            |
| `book_status`   | VARCHAR(100) | Status of the book (`available`, `loaned`) |
| `author_id`     | INT          | Foreign Key referencing `authors(id)`  |

#### Member
| Column Name        | Data Type    | Description                                  |
|--------------------|--------------|----------------------------------------------|
| `id`               | INT          | Primary Key, Auto-Increment                  |
| `username`         | VARCHAR(100) | Username of the user                         |
| `password`         | VARCHAR(100) | Password for the user's account              |
| `name`             | VARCHAR(100) | Name of the user                             |
| `email`            | VARCHAR(100) | Email of the user                            |
| `phone`            | VARCHAR(15)  | Phone number of the user                     |
| `role`             | VARCHAR(50)  | Role of the user (`member`, `administrator`) |
| `membership_type`  | VARCHAR(50)  | Type of membership (`monthly`, `yearly`)     |

#### Loans
| Column Name  | Data Type | Description                                               |
|--------------|-----------|-----------------------------------------------------------|
| `user_id`    | INT       | Foreign Key referencing `members(id)`                     |
| `book_id`    | INT       | Foreign Key referencing `books(id)`                       |
| `loan_date`  | DATE      | Date the book was loaned                                  |
| `return_date`| DATE      | Date the book was returned or null if the date is not set |
| Primary Key  | (user_id, book_id) | Composite key combining `user_id` and `book_id`           |

 
## API Endpoints

This project includes a Postman collection, `Library.postman_collection.json`, that can be used to test all API endpoints. Below is a description of the available endpoints:

### Authors
- **GET /author**
  - Retrieves a list of all authors.

- **GET /author/:id**
  - Retrieves information about an author by their ID.

- **POST /author**
  - Creates a new author with the provided data (name and birth year).

- **PUT /author/:id**
  - Updates the data of an author by their ID.

- **DELETE /author/:id**
  - Deletes an author and all their books by their ID.

### Books
- **GET /book**
  - Retrieves a list of all books.

- **GET /book/:id**
  - Retrieves information about a book by its ID.

- **GET /book/author/:author_id**
  - Retrieves all books by a specific author by their ID.

- **POST /book**
  - Creates a new book with the provided data (title, genre, year published, book status, author ID).

- **PUT /book/:id**
  - Updates the data of a book by its ID.

- **PATCH /book/:id**
  - Updates the book's status to `available`.

- **DELETE /book/:id**
  - Deletes a book by its ID.

### Members
- **GET /member**
  - Retrieves a list of all members.

- **GET /member/:id**
  - Retrieves information about a member by their ID.

- **POST /member**
  - Creates a new member with the provided data (name, email, phone, membership start date, membership end date).

- **PUT /member/:id**
  - Updates the data of a member by their ID.

- **DELETE /member/:id**
  - Deletes a member by their ID.

### Loans
- **GET /loan**
  - Retrieves a list of all loans.

- **GET /loan/:member_id/:book_id**
  - Retrieves information about a loan by the member ID and book ID.

- **POST /loan**
  - Creates a new loan with the provided data (member ID, book ID, loan date). If the book is already loaned, the loan cannot be created.

- **PATCH /loan/:member_id/:book_id**
  - Updates the return date of a loan by the member ID and book ID.

- **DELETE /loan/:member_id/:book_id**
  - Deletes a loan by the member ID and book ID.

## Setup Instructions

### Prerequisites

Ensure you have the following installed:

- [Clojure](https://clojure.org/guides/getting_started)
- [Leiningen](https://leiningen.org/)

### Configuration

You need to configure the database parameters in two files:

- `configuration/init-db.edn`
- `configuration/db.edn`

Set the `username` and `password` for your database connection.

### Running the Application

To run the application, use the following command:

```bash
lein ring server
