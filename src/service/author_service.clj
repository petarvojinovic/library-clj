(ns service.author_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn get-all-authors []
  (jdbc/query db ["SELECT * FROM authors"]))

(defn get-author-by-id [id]
  (jdbc/query db ["SELECT * FROM authors WHERE id = ?" id]))

(defn create-author [name birth_year]
  (jdbc/execute! db
                 ["INSERT INTO authors (name, birth_year)
                  VALUES (?, ?)"
                  name birth_year]))

(defn update-author [id name birth_year]
  (jdbc/execute! db
                 ["UPDATE authors
                  SET name = ?, birth_year = ?
                  WHERE id = ?"
                  name birth_year id]))

(defn delete-author [id]
  (jdbc/execute! db ["DELETE FROM authors WHERE id = ?" id]))


(defn get-books-by-author-id [author_id]
  (jdbc/query db ["SELECT * FROM books WHERE author_id = ?" author_id]))