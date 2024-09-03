(ns service.books_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn get-all-books []
  (jdbc/query db ["SELECT books.*, authors.name AS author_name, authors.birth_year AS author_birth_year
                   FROM books
                   JOIN authors ON books.author_id = authors.id"]))


(defn get-book-by-id [id]
  (jdbc/query db ["SELECT books.*, authors.name AS author_name, authors.birth_year AS author_birth_year
                   FROM books
                   JOIN authors ON books.author_id = authors.id
                   WHERE books.id = ?" id]))

(defn create-book [title genre year_published author_id]
  (jdbc/execute! db
                 ["INSERT INTO books (title, genre, year_published, author_id)
                  VALUES (?, ?, ?, ?)"
                  title genre year_published author_id]))

(defn update-book [id title genre year_published author_id]
  (jdbc/execute! db
                 ["UPDATE books
                  SET title = ?, genre = ?, year_published = ?, author_id = ?
                  WHERE id = ?"
                  title genre year_published author_id id]))

(defn delete-book [id]
  (jdbc/execute! db ["DELETE FROM books WHERE id = ?" id]))


;; dobijan svih knjiga odredjenog autora
(defn get-books-by-author-id [author_id]
  (jdbc/query db ["SELECT books.*, authors.name AS author_name, authors.birth_year AS author_birth_year
                   FROM books
                   JOIN authors ON books.author_id = authors.id
                   WHERE books.author_id = ?" author_id]))

(defn author-exists? [author_id]
  (let [author (first (jdbc/query db ["SELECT * FROM authors WHERE id = ?" author_id]))]
    (not (nil? author))))
