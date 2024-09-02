(ns validation.member_validation
  (:require [clojure.spec.alpha :as s]
            ))

(s/def ::name (s/and string? (complement empty?)))
(s/def ::email #(re-matches #".+@.+\..+" %))
(s/def ::phone #(re-matches #"\d{3}-\d{3}-\d{4}" %))
(s/def ::date #(re-matches #"\d{4}-\d{2}-\d{2}" %))

(s/def ::membership_start ::date)
(s/def ::membership_end ::date)

(s/def ::member (s/keys :req-un [::name ::email ::phone ::membership_start ::membership_end]))

(defn validate-member [member-data]
  (let [errors (cond-> {}
                       (not (s/valid? ::name (get member-data :name)))
                       (assoc :name "Username must be a non-empty string")

                       (not (s/valid? ::email (get member-data :email)))
                       (assoc :email "Email must be in a valid format")

                       (not (s/valid? ::phone (get member-data :phone)))
                       (assoc :phone "Phone number must be in the format 123-456-7890")

                       (not (s/valid? ::date (get member-data :membership_start)))
                       (assoc :membership_start "Membership start date must be in the format YYYY-MM-DD")

                       (not (s/valid? ::date (get member-data :membership_end)))
                       (assoc :membership_end "Membership end date must be in the format YYYY-MM-DD"))]

    (if (empty? errors)
      {:status :ok}
      {:status :error :message errors})))