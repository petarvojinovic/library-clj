(ns validation.author_validation
  (:require [clojure.spec.alpha :as s]))

(s/def ::name (s/and string? (complement empty?)))
(s/def ::birth_year #(re-matches #"\d{4}" (str %)))

(s/def ::author (s/keys :req-un [::name ::birth_year]))

(defn validate-author [author-data]
  (let [errors (cond-> {}
                       (not (s/valid? ::name (get author-data :name)))
                       (assoc :name "Author name must be a non-empty string")

                       (not (s/valid? ::birth_year (get author-data :birth_year)))
                       (assoc :birth_year "Invalid birth year"))]

    (if (empty? errors)
      {:status :ok}
      {:status :error :message errors})))
