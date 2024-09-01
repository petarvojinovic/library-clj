[
"DROP DATABASE IF EXISTS `library`;",

"CREATE DATABASE IF NOT EXISTS `library`;",

"USE `library`;",

"DROP TABLE IF EXISTS `loans`;",
"DROP TABLE IF EXISTS `books`;",
"DROP TABLE IF EXISTS `members`;",
"DROP TABLE IF EXISTS `authors`;",

"CREATE TABLE `authors` (
  `id` INT AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `birth_year` INT,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"CREATE TABLE `books` (
  `id` INT AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `genre` VARCHAR(100),
  `year_published` INT,
  `author_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`author_id`) REFERENCES `authors`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"CREATE TABLE `members` (
  `id` INT AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(15),
  `membership_start` DATE NOT NULL,
  `membership_end` DATE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"CREATE TABLE `loans` (
  `member_id` INT,
  `book_id` INT,
  `loan_date` DATE NOT NULL,
  `return_date` DATE,
  PRIMARY KEY (`member_id`, `book_id`),
  FOREIGN KEY (`book_id`) REFERENCES `books`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`member_id`) REFERENCES `members`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"INSERT INTO `authors` (`name`, `birth_year`) VALUES ('J.K. Rowling', 1965);",
"INSERT INTO `authors` (`name`, `birth_year`) VALUES ('George Orwell', 1903);",
"INSERT INTO `authors` (`name`, `birth_year`) VALUES ('Agatha Christie', 1890);",
"INSERT INTO `authors` (`name`, `birth_year`) VALUES ('J.R.R. Tolkien', 1892);",
"INSERT INTO `authors` (`name`, `birth_year`) VALUES ('F. Scott Fitzgerald', 1896);",

"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('Harry Potter and the Philosopher''s Stone', 'Fantasy', 1997, 1);",
"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('1984', 'Dystopian', 1949, 2);",
"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('Murder on the Orient Express', 'Mystery', 1934, 3);",
"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('The Hobbit', 'Fantasy', 1937, 4);",
"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('The Great Gatsby', 'Classic', 1925, 5);",
"INSERT INTO `books` (`title`, `genre`, `year_published`, `author_id`) VALUES ('Harry Potter and the Chamber of Secrets', 'Classic', 1998, 1);",

"INSERT INTO `members` (`name`, `email`, `phone`, `membership_start`, `membership_end`) VALUES ('Petar Vojinovic', 'petar.vojinovic@gmail.com', '060-111-1111', '2024-01-01', '2025-01-01');",
"INSERT INTO `members` (`name`, `email`, `phone`, `membership_start`, `membership_end`) VALUES ('Igor Vukasovic', 'igor.vukasovic@gmail.com', '064-222-2222', '2024-02-01', '2025-02-01');",
"INSERT INTO `members` (`name`, `email`, `phone`, `membership_start`, `membership_end`) VALUES ('Stefan Velickovic', 'stefan.velickovic@yahoo.com', '063-333-3333', '2024-03-01', '2025-03-01');",
"INSERT INTO `members` (`name`, `email`, `phone`, `membership_start`, `membership_end`) VALUES ('Andjela Belosevac', 'andjela.belosevac@yahoo.com', '069-444-4444', '2024-04-01', '2025-04-01');",

"INSERT INTO `loans` (`member_id`, `book_id`, `loan_date`, `return_date`) VALUES (1, 1, '2024-08-01', NULL);",
"INSERT INTO `loans` (`member_id`, `book_id`, `loan_date`, `return_date`) VALUES (2, 2, '2024-08-05', '2024-08-15');",
"INSERT INTO `loans` (`member_id`, `book_id`, `loan_date`, `return_date`) VALUES (3, 3, '2024-08-10', '2024-08-20');",
"INSERT INTO `loans` (`member_id`, `book_id`, `loan_date`, `return_date`) VALUES (4, 5, '2024-08-12', NULL);",
"INSERT INTO `loans` (`member_id`, `book_id`, `loan_date`, `return_date`) VALUES (1, 4, '2024-08-15', NULL);"

]
