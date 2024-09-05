[
"DROP DATABASE IF EXISTS `library`;",

"CREATE DATABASE IF NOT EXISTS `library`;",

"USE `library`;",

"DROP TABLE IF EXISTS `loans`;",
"DROP TABLE IF EXISTS `book`;",
"DROP TABLE IF EXISTS `user`;",
"DROP TABLE IF EXISTS `author`;",

"CREATE TABLE `author` (
  `id` INT AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `birth_year` INT,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"CREATE TABLE `book` (
  `id` INT AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `genre` VARCHAR(100),
  `year_published` INT,
  `book_status` VARCHAR(100),
  `author_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`author_id`) REFERENCES `author`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
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
  FOREIGN KEY (`book_id`) REFERENCES `book`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;",

"INSERT INTO `author` (`name`, `birth_year`) VALUES ('J.K. Rowling', 1965);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('George Orwell', 1903);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('Agatha Christie', 1890);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('J.R.R. Tolkien', 1892);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('F. Scott Fitzgerald', 1896);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('Isaac Asimov', 1920);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('Arthur Conan Doyle', 1859);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('H.G. Wells', 1866);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('Stephen King', 1947);",
"INSERT INTO `author` (`name`, `birth_year`) VALUES ('Jane Austen', 1775);",

"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Harry Potter and the Philosopher''s Stone', 'Fantasy', 1997, 'loaned', 1);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Harry Potter and the Chamber of Secrets', 'Fantasy', 1998, 'available', 1);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Harry Potter and the Prisoner of Azkaban', 'Fantasy', 1999, 'available', 1);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('1984', 'Dystopian', 1949, 'loaned', 2);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Murder on the Orient Express', 'Mystery', 1934, 'loaned', 3);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The ABC Murders', 'Mystery', 1936, 'available', 3);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Hobbit', 'Fantasy', 1937, 'loaned', 4);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Lord of the Rings: The Fellowship of the Ring', 'Fantasy', 1954, 'available', 4);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Lord of the Rings: The Two Towers', 'Fantasy', 1954, 'available', 4);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Great Gatsby', 'Classic', 1925, 'loaned', 5);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Foundation', 'Science Fiction', 1951, 'available', 6);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Foundation and Empire', 'Science Fiction', 1952, 'available', 6);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Hound of the Baskervilles', 'Mystery', 1902, 'available', 7);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Time Machine', 'Science Fiction', 1895, 'available', 8);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('The Shining', 'Horror', 1977, 'loaned', 9);",
"INSERT INTO `book` (`title`, `genre`, `year_published`, `book_status`, `author_id`) VALUES ('Pride and Prejudice', 'Classic', 1813, 'available', 10);",

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
