(ns service.recommendation_service
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn get-books-loaned-by-member [member_id]
  (jdbc/query db ["SELECT books.id, books.title, books.genre, authors.name AS author_name, authors.birth_year AS author_birth_year
                   FROM loans
                   JOIN books ON loans.book_id = books.id
                   JOIN authors ON books.author_id = authors.id
                   WHERE loans.member_id = ?" member_id]))

(defn get-all-books []
  (jdbc/query db ["SELECT books.*, authors.name AS author_name, authors.birth_year AS author_birth_year
                   FROM books
                   JOIN authors ON books.author_id = authors.id"]))

(defn filter-books-by-genre [books genre]
  (filter #(= (:genre %) genre) books))

(defn recommend-books [member_id]
  (let [loaned-books (get-books-loaned-by-member member_id)
        all-books (get-all-books)
        genres (set (map :genre loaned-books))
        unread-books (remove #(some #{(:id %)} (map :id loaned-books)) all-books)
        recommendations (mapcat #(filter-books-by-genre unread-books %) genres)]
    (map (fn [book]
           (assoc book :author {:author_id (:author_id book)
                                :name (:author_name book)
                                :birth_year (:author_birth_year book)}))
         recommendations)))

