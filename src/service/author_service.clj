(ns service.author_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]
            [validation.author_validation :as validation]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn author-exists? [id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM authors WHERE id = ?" id]))))

(defn get-all-authors []
  (jdbc/query db ["SELECT * FROM authors"]))

(defn get-author-by-id [id]
  (jdbc/query db ["SELECT * FROM authors WHERE id = ?" id]))

(defn create-author [author-data]
  (let [validation-result (validation/validate-author author-data)
        {:keys [name birth_year]} author-data]
    (if (= :ok (:status validation-result))
      (do
        (jdbc/execute! db
                       ["INSERT INTO authors (name, birth_year) VALUES (?, ?)"
                        name birth_year])
        {:status :ok :message "Author created successfully"})
      validation-result)))

(defn update-author [id author-data]
  (let [validation-result (validation/validate-author author-data)
        {:keys [name birth_year]} author-data]
    (if (= :ok (:status validation-result))
      (if (author-exists? id)
        (do
          (jdbc/execute! db
                         ["UPDATE authors SET name = ?, birth_year = ? WHERE id = ?"
                          name birth_year id])
          {:status :ok :message "Author updated successfully"})
        {:status :error :message "Author not found"})
      validation-result)))

(defn delete-author [id]
  (if (author-exists? id)
    (do
      (jdbc/execute! db ["DELETE FROM authors WHERE id = ?" id])
      {:status :ok :message "Author deleted successfully"})
    {:status :error :message "Author not found"}))

(defn get-books-by-author-id [author_id]
  (jdbc/query db ["SELECT * FROM books WHERE author_id = ?" author_id]))
