(ns service.author_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn author-exists? [id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM author WHERE id = ?" id]))))

(defn get-all-authors []
  (jdbc/query db ["SELECT * FROM author"]))

(defn get-author-by-id2 [id]
  (jdbc/query db ["SELECT * FROM author WHERE id = ?" id]))

(defn get-author-by-id [id]
  (let [author (first (jdbc/query db ["SELECT * FROM author WHERE id = ?" id]))]
    (if author
      {:status :ok :data author}
      {:status :error :message "Author not found"})))

(defn create-author [name birth_year]
  (jdbc/execute! db
                 ["INSERT INTO author (name, birth_year)
                  VALUES (?, ?)"
                  name birth_year]))

(defn update-author [id name birth_year]
  (if (author-exists? id)
    (do
      (jdbc/execute! db
                     ["UPDATE author
                      SET name = ?, birth_year = ?
                      WHERE id = ?"
                      name birth_year id])
      {:status :ok :message "Author updated successfully"})
    {:status :error :message "Author not found"}))

(defn delete-author [id]
  (if (author-exists? id)
    (do
      (jdbc/execute! db ["DELETE FROM author WHERE id = ?" id])
      {:status :ok :message "Author deleted successfully"})
    {:status :error :message "Author not found"}))

(defn get-books-by-author-id [author_id]
  (if (author-exists? author_id)
    (let [books (jdbc/query db ["SELECT * FROM book WHERE book.author_id = ?" author_id])]
      (if (empty? books)
        {:status :error :message "No books found for this author"}
        {:status :ok :data books}))
    {:status :error :message "Author not found"}))
