(ns validation.loan_validation
  (:require [clojure.spec.alpha :as s]
            [service.loan_service :as loan-service]
            [service.member_service :as member-service]
            [service.books_service :as book-service]))

(defn valid-year? [date]
  (re-matches #"\d{4}-\d{2}-\d{2}" date))

(defn loan-exists? [member_id book_id]
  (not (empty? (loan-service/get-loan member_id book_id))))

(defn member-exists? [member_id]
  (not (empty? (member-service/get-member-by-id member_id))))

(defn book-exists? [book_id]
  (not (empty? (book-service/get-book-by-id book_id))))

(s/def ::loan_date valid-year?)
(s/def ::return_date (s/nilable valid-year?))
(s/def ::member_id (s/and int? member-exists?))
(s/def ::book_id (s/and int? book-exists?))

(s/def ::loan (s/keys :req-un [::member_id ::book_id ::loan_date]))

(defn validate-loan [loan-data]
  (let [errors (cond-> {}
                       (loan-exists? (get loan-data :member_id) (get loan-data :book_id))
                       (assoc :loan "A loan with the same member and book already exists")

                       (not (s/valid? ::member_id (get loan-data :member_id)))
                       (assoc :member_id "Member with the given ID does not exist")

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
