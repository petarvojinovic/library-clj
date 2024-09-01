# Library Management System

## Overview

This application is a CRUD system designed to manage a library. The system allows you to manage authors, books, members, and book loans. Each book is associated with an author, and each member can have one or more books on loan. The application is built using Clojure and utilizes a MySQL database for data storage.

### Key Features:
- Create, update, view, and delete authors.
- Create, update, view, and delete books.
- Create, update, view, and delete library members.
- Manage book loans, including tracking loan dates and return dates.

## Relational Model

### Tables and Attributes

#### Authors
| Column Name   | Data Type    | Description                        |
|---------------|--------------|------------------------------------|
| `id`          | INT          | Primary Key, Auto-Increment        |
| `name`        | VARCHAR(100) | Name of the author                 |
| `birth_year`  | INT          | Birth year of the author           |

#### Books
| Column Name     | Data Type    | Description                            |
|-----------------|--------------|----------------------------------------|
| `id`            | INT          | Primary Key, Auto-Increment            |
| `title`         | VARCHAR(200) | Title of the book                      |
| `genre`         | VARCHAR(100) | Genre of the book                      |
| `year_published`| INT          | Year the book was published            |
| `author_id`     | INT          | Foreign Key referencing `authors(id)`  |

#### Members
| Column Name        | Data Type    | Description                        |
|--------------------|--------------|------------------------------------|
| `id`               | INT          | Primary Key, Auto-Increment        |
| `name`             | VARCHAR(100) | Name of the member                 |
| `email`            | VARCHAR(100) | Email of the member                |
| `phone`            | VARCHAR(15)  | Phone number of the member         |
| `membership_start` | DATE         | Start date of the membership       |
| `membership_end`   | DATE         | End date of the membership         |

#### Loans
| Column Name  | Data Type | Description                               |
|--------------|-----------|-------------------------------------------|
| `member_id`  | INT       | Foreign Key referencing `members(id)`     |
| `book_id`    | INT       | Foreign Key referencing `books(id)`       |
| `loan_date`  | DATE      | Date the book was loaned                  |
| `return_date`| DATE      | Date the book was returned                |
| Primary Key  | (member_id, book_id) | Composite key combining `member_id` and `book_id` |

### Relationships

- **Authors to Books**: One-to-Many relationship (An author can write multiple books).
- **Books to Loans**: One-to-Many relationship (A book can be loaned multiple times).
- **Members to Loans**: One-to-Many relationship (A member can loan multiple books).

## API Endpoints

This project includes a Postman collection, `Library.postman_collection.json`, 
that can be used to test all API endpoints. Below is a description of the available endpoints:

### Authors
- **GET /authors**
    - Retrieves a list of all authors.

- **GET /authors/:id**
    - Retrieves information about an author by their ID.

- **POST /authors**
    - Creates a new author with the provided data (name and birth year).

- **PUT /authors/:id**
    - Updates the data of an author by their ID.

- **DELETE /authors/:id**
    - Deletes an author and all their books by their ID.

### Books
- **GET /books**
    - Retrieves a list of all books.

- **GET /books/:id**
    - Retrieves information about a book by its ID.

- **GET /authors/:id/books**
    - Retrieves all books by a specific author by their ID.

- **POST /books**
    - Creates a new book with the provided data (title, genre, year published, author ID).

- **PUT /books/:id**
    - Updates the data of a book by its ID.

- **DELETE /books/:id**
    - Deletes a book by its ID.

### Members
- **GET /members**
    - Retrieves a list of all members.

- **GET /members/:id**
    - Retrieves information about a member by their ID.

- **POST /members**
    - Creates a new member with the provided data (name, email, phone, membership start date, membership end date).

- **PUT /members/:id**
    - Updates the data of a member by their ID.

- **DELETE /members/:id**
    - Deletes a member by their ID.

### Loans
- **GET /loans**
    - Retrieves a list of all loans.

- **GET /loans/:member_id/:book_id**
    - Retrieves information about a loan by the member ID and book ID.

- **POST /loans**
    - Creates a new loan with the provided data (member ID, book ID, loan date).

- **PUT /loans/:member_id/:book_id**
    - Updates the return date of a loan by the member ID and book ID.

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