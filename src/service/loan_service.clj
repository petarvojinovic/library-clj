(ns service.loan_service
  (:refer-clojure :exclude [get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(def date-formatter (f/formatter "yyyy-MM-dd"))

(defn parse-date [date-str]
  (c/to-sql-date (f/parse date-formatter date-str)))

(defn loan-exists? [user_id book_id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM loan WHERE user_id = ? AND book_id = ?" user_id book_id]))))

(defn user-exists? [user_id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM user WHERE id = ?" user_id]))))

(defn book-exists? [book_id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM book WHERE id = ?" book_id]))))


(defn book-loaned? [book_id]
  (let [book (first (jdbc/query db ["SELECT book_status FROM book WHERE id = ?" book_id]))]
    (= "loaned" (:book_status book))))

(defn get-all-loans []
  (jdbc/query db ["SELECT * FROM loan"]))

(defn get-loan [user_id book_id]
  (let [loan (first (jdbc/query db ["SELECT * FROM loan WHERE user_id = ? AND book_id = ?" user_id book_id]))]
    (if loan
      {:status :ok :data loan}
      {:status :error :message "Loan not found"})))

(defn create-loan [user_id book_id loan_date return_date]
  (if (and (user-exists? user_id)
           (book-exists? book_id))
    (if (book-loaned? book_id)
      {:status :error :message "Book is already loaned"}
      (do
        (jdbc/execute! db
                       ["INSERT INTO loan (user_id, book_id, loan_date, return_date)
                        VALUES (?, ?, ?, ?)
                        ON DUPLICATE KEY UPDATE loan_date = VALUES(loan_date), return_date = VALUES(return_date)"
                        user_id book_id (parse-date loan_date) (parse-date return_date)])

        (jdbc/execute! db ["UPDATE book SET book_status = 'loaned' WHERE id = ?" book_id])
        {:status :ok :message "Loan created successfully"}))
    {:status :error :message "Invalid user or book ID"}))


(defn update-loan [user_id book_id loan_date return_date]
  (if (loan-exists? user_id book_id)
    (do
      (jdbc/execute! db
                     ["UPDATE loan
                      SET loan_date = ?, return_date = ?
                      WHERE user_id = ? AND book_id = ?"
                      loan_date return_date user_id book_id])
      {:status :ok :message "Loan updated successfully"})
    {:status :error :message "Loan not found"}))

(defn delete-loan [user_id book_id]
  (if (loan-exists? user_id book_id)
    (do
      (jdbc/execute! db ["DELETE FROM loan WHERE user_id = ? AND book_id = ?" user_id book_id])
      (jdbc/execute! db ["UPDATE book SET book_status = 'available' WHERE id = ?" book_id])
      {:status :ok :message "Loan deleted successfully"})
    {:status :error :message "Loan not found"}))

(defn change-loan-return-date [user_id book_id return_date]
  (if (loan-exists? user_id book_id)
    (do
      (jdbc/execute! db
                     ["UPDATE loan SET return_date = ? WHERE user_id = ? AND book_id = ?"
                      return_date user_id book_id])
      {:status :ok :message "Return date updated successfully"})
    {:status :error :message "Loan not found"}))

