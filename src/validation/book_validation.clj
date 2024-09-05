(ns validation.book_validation
  (:require [clojure.spec.alpha :as s]
            [service.author_service :as author-service]))

(s/def ::title (s/and string? (complement empty?)))
(s/def ::genre (s/and string? (complement empty?)))

(defn valid-year? [year]
  (re-matches #"\d{4}" (str year)))

(s/def ::year_published (s/and #(valid-year? %)))

(defn author-exists? [author_id]
  (not (empty? (author-service/get-author-by-id author_id))))

(s/def ::book_status #(contains? #{"available" "loaned"} %))

(s/def ::author_id author-exists?)

(s/def ::book (s/keys :req-un [::title ::genre ::year_published ::book_status ::author_id ]))

(defn validate-book [book-data]
  (let [errors (cond-> {}
                       (not (s/valid? ::title (get book-data :title)))
                       (assoc :title "Title must be a non-empty string")

                       (not (s/valid? ::genre (get book-data :genre)))
                       (assoc :genre "Genre must be a non-empty string")

                       (not (s/valid? ::year_published (get book-data :year_published)))
                       (assoc :year_published "Year published must be a valid four digit number")

                       (not (s/valid? ::book_status (get book-data :book_status)))
                       (assoc :book_status "Book status must be either 'available' or 'loaned'")

                       (not (s/valid? ::author_id (get book-data :author_id)))
                       (assoc :author_id "Author with given ID does not exist"))]

    (if (empty? errors)
      {:status :ok}
      {:status :error :message errors})))
