(ns service.author_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn author-exists? [id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM authors WHERE id = ?" id]))))

(defn get-all-authors []
  (jdbc/query db ["SELECT * FROM authors"]))

(defn get-author-by-id2 [id]
  (jdbc/query db ["SELECT * FROM authors WHERE id = ?" id]))

(defn get-author-by-id [id]
  (let [author (first (jdbc/query db ["SELECT * FROM authors WHERE id = ?" id]))]
    (if author
      {:status :ok :data author}
      {:status :error :message "Author not found"})))

(defn create-author [author-data]
  (let [{:keys [name birth_year]} author-data]
    (jdbc/execute! db
                   ["INSERT INTO authors (name, birth_year) VALUES (?, ?)"
                    name birth_year])
    {:status :ok :message "Author created successfully"}))

(defn update-author [id author-data]
  (let [{:keys [name birth_year]} author-data]
    (if (author-exists? id)
      (do
        (jdbc/execute! db
                       ["UPDATE authors SET name = ?, birth_year = ? WHERE id = ?"
                        name birth_year id])
        {:status :ok :message "Author updated successfully"})
      {:status :error :message "Author not found"})))

(defn delete-author [id]
  (if (author-exists? id)
    (do
      (jdbc/execute! db ["DELETE FROM authors WHERE id = ?" id])
      {:status :ok :message "Author deleted successfully"})
    {:status :error :message "Author not found"}))

(defn get-books-by-author-id [author_id]
  (if (author-exists? author_id)
    (let [books (jdbc/query db ["SELECT * FROM book WHERE book.author_id = ?" author_id])]
      (if (empty? books)
        {:status :error :message "No books found for this author"}
        {:status :ok :data books}))
    {:status :error :message "Author not found"}))
