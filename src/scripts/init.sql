[
"DROP DATABASE IF EXISTS `library`;",

"CREATE DATABASE IF NOT EXISTS `library`;",

"USE `library`;",

"DROP TABLE IF EXISTS `loans`;",
"DROP TABLE IF EXISTS `books`;",
"DROP TABLE IF EXISTS `user`;",
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

"CREATE TABLE `user` (
  `id` INT AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `role` VARCHAR(100) NOT NULL,
  `membership_type` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"CREATE TABLE `loans` (
  `user_id` INT,
  `book_id` INT,
  `loan_date` DATE NOT NULL,
  `return_date` DATE,
  PRIMARY KEY (`user_id`, `book_id`),
  FOREIGN KEY (`book_id`) REFERENCES `books`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
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

"INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `role`, `membership_type`) VALUES ('pera', 'pera123', 'Petar Vojinovic', 'petar.vojinovic@gmail.com', '060-111-1111', 'administrator', 'NULL');",
"INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `role`, `membership_type`) VALUES ('igi', 'igi123', 'Igor Vukasovic', 'igor.vukasovic@gmail.com', '064-222-2222', 'member', 'yearly');",
"INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `role`, `membership_type`) VALUES ('velicko', 'velicko123', 'Stefan Velickovic', 'stefan.velickovic@yahoo.com', '063-333-3333', 'member', 'monthly');",
"INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `role`, `membership_type`) VALUES ('andja', 'andja123', 'Andjela Belosevac', 'andjela.belosevac@yahoo.com', '069-444-4444', 'member', 'yearly');",

"INSERT INTO `loans` (`user_id`, `book_id`, `loan_date`, `return_date`) VALUES (1, 1, '2024-08-01', NULL);",
"INSERT INTO `loans` (`user_id`, `book_id`, `loan_date`, `return_date`) VALUES (2, 2, '2024-08-05', '2024-08-15');",
"INSERT INTO `loans` (`user_id`, `book_id`, `loan_date`, `return_date`) VALUES (3, 3, '2024-08-10', '2024-08-20');",
"INSERT INTO `loans` (`user_id`, `book_id`, `loan_date`, `return_date`) VALUES (4, 5, '2024-08-12', NULL);",
"INSERT INTO `loans` (`user_id`, `book_id`, `loan_date`, `return_date`) VALUES (1, 4, '2024-08-15', NULL);"

]
