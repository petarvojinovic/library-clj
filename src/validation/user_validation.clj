(ns validation.user_validation
  (:require [clojure.spec.alpha :as s]))

;; Define specs for each field
(s/def ::username (s/and string? (complement empty?)))
(s/def ::password (s/and string? (complement empty?)))
(s/def ::name (s/and string? (complement empty?)))
(s/def ::email #(re-matches #".+@.+\..+" %))
(s/def ::phone #(re-matches #"\d{3}-\d{3}-\d{4}" %))
(s/def ::role #{"administrator" "member"})
(s/def ::membership_type #{"monthly" "yearly"})

;; Define spec for user
(s/def ::user (s/keys :req-un [::username ::password ::name ::email ::phone ::role ::membership_type]))

(defn validate-user [user-data]
  (let [errors (cond-> {}

                       ;; Username validation
                       (not (s/valid? ::username (get user-data :username)))
                       (assoc :username "Invalid username: must be a non-empty string")

                       ;; Password validation
                       (not (s/valid? ::password (get user-data :password)))
                       (assoc :password "Invalid password: must be a non-empty string")

                       ;; Name validation
                       (not (s/valid? ::name (get user-data :name)))
                       (assoc :name "Invalid name: must be a non-empty string")

                       ;; Email validation
                       (not (s/valid? ::email (get user-data :email)))
                       (assoc :email "Email must be in a valid format")

                       ;; Phone validation
                       (not (s/valid? ::phone (get user-data :phone)))
                       (assoc :phone "Phone number must be in the format 123-456-7890")

                       ;; Role validation
                       (not (s/valid? ::role (get user-data :role)))
                       (assoc :role "Role must be 'administrator' or 'member'")

                       ;; Membership type validation
                       (not (s/valid? ::membership_type (get user-data :membership_type)))
                       (assoc :membership_type "Membership type must be 'monthly' or 'yearly'"))]

    ;; Return validation result
    (if (empty? errors)
      {:status :ok}
      {:status :error :message errors})))
