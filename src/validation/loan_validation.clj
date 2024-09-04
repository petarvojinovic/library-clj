(ns validation.loan_validation
  (:require [clojure.spec.alpha :as s]
            [service.loan_service :as loan-service]
            [service.user_service :as user-service]
            [service.books_service :as book-service]))

(defn valid-year? [date]
  (re-matches #"\d{4}-\d{2}-\d{2}" date))

(defn loan-exists? [user_id book_id]
  (not (empty? (loan-service/get-loan user_id book_id))))

(defn user-exists? [user_id]
  (not (empty? (user-service/get-user-by-id user_id))))

(defn book-exists? [book_id]
  (not (empty? (book-service/get-book-by-id book_id))))

(s/def ::loan_date valid-year?)
(s/def ::return_date (s/nilable valid-year?))
(s/def ::user_id (s/and int? user-exists?))
(s/def ::book_id (s/and int? book-exists?))

(s/def ::loan (s/keys :req-un [::user_id ::book_id ::loan_date]))

(defn validate-loan [loan-data]
  (let [errors (cond-> {}
                       (loan-exists? (get loan-data :user_id) (get loan-data :book_id))
                       (assoc :loan "A loan with the same user and book already exists")

                       (not (s/valid? ::user_id (get loan-data :user_id)))
                       (assoc :user_id "User with the given ID does not exist")

                       (not (s/valid? ::book_id (get loan-data :book_id)))
                       (assoc :book_id "Book with the given ID does not exist")

                       (not (s/valid? ::loan_date (get loan-data :loan_date)))
                       (assoc :loan_date "Loan date must be in the format YYYY-MM-DD")

                       (and (get loan-data :return_date)
                            (not (s/valid? ::return_date (get loan-data :return_date))))
                       (assoc :return_date "Return date must be in the format YYYY-MM-DD if provided"))]

    (if (empty? errors)
      {:status :ok}
      {:status :error :message errors})))
