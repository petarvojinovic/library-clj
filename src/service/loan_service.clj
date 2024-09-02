(ns service.loan_service
  (:refer-clojure :exclude [get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(def date-formatter (f/formatter "yyyy-MM-dd"))

(defn format-date [date]
  (f/unparse date-formatter (c/from-sql-date date)))

(defn get-all-loans []
  (let [loans (jdbc/query db ["SELECT * FROM loans"])]
    (map #(assoc % :loan_date (format-date (:loan_date %))
                   :return_date (format-date (:return_date %)))
         loans)))

(defn loan-exists? [member_id book_id]
  (not (empty? (jdbc/query db ["SELECT * FROM loans WHERE member_id = ? AND book_id = ?" member_id book_id]))))

(defn get-loan [member_id book_id]
  (jdbc/query db ["SELECT * FROM loans WHERE member_id = ? AND book_id = ?" member_id book_id]))

(defn create-loan [member_id book_id loan_date return_date]
  (jdbc/execute! db
                 ["INSERT INTO loans (member_id, book_id, loan_date, return_date)
                  VALUES (?, ?, ?, ?)"
                  member_id book_id loan_date return_date]))

(defn update-loan [member_id book_id loan_date return_date]
  (jdbc/execute! db
                 ["UPDATE loans
                  SET loan_date = ?, return_date = ?
                  WHERE member_id = ? AND book_id = ?"
                  loan_date return_date member_id book_id]))

(defn delete-loan [member_id book_id]
  (jdbc/execute! db ["DELETE FROM loans WHERE member_id = ? AND book_id = ?" member_id book_id]))
