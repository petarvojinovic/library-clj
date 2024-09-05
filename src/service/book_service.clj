(ns service.book_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn author-exists? [author_id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM author WHERE id = ?" author_id]))))

(defn book-exists? [id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM book WHERE id = ?" id]))))

(defn get-all-books []
  (jdbc/query db ["SELECT book.id, book.title, book.genre, book.year_published, book.book_status, book.author_id
                   FROM book"]))

(defn get-book-by-id [id]
  (let [book (first (jdbc/query db ["SELECT * FROM book WHERE id = ?" id]))]
    (if book
      {:status :ok :data book}
      {:status :error :message "Book not found"})))

;; dobijan svih knjiga odredjenog autora
(defn get-books-by-author-id [author_id]
  (if (author-exists? author_id)
    (let [books (jdbc/query db ["SELECT * FROM book WHERE book.author_id = ?" author_id])]
      (if (empty? books)
        {:status :error :message "No books found for this author"}
        {:status :ok :data books}))
    {:status :error :message "Author not found"}))

(defn create-book [title genre year_published book_status author_id]
  (jdbc/execute! db
                 ["INSERT INTO book (title, genre, year_published, book_status, author_id)
                  VALUES (?, ?, ?, ?, ?)"
                  title genre year_published book_status author_id]))

(defn update-book [id title genre year_published book_status author_id]
  (if (book-exists? id)
    (do
      (jdbc/execute! db
                     ["UPDATE book
                      SET title = ?, genre = ?, year_published = ?, book_status = ?, author_id = ?
                      WHERE id = ?"
                      title genre year_published book_status author_id id])
      {:status :ok :message "Book updated successfully"})
    {:status :error :message "Book not found"}))

(defn delete-book [id]
  (if (book-exists? id)
    (do
      (jdbc/execute! db ["DELETE FROM book WHERE id = ?" id])
      {:status :ok :message "Book deleted successfully"})
    {:status :error :message "Book not found"}))

(defn set-book-status-available [id]
  (if (book-exists? id)
    (do
      (jdbc/execute! db
                     ["UPDATE book SET book_status = 'available' WHERE id = ?" id])
      {:status :ok :message "Book returned and available to loan again"})
    {:status :error :message "Book not found"}))


