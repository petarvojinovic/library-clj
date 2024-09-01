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
  (if (s/valid? ::member member-data)
    {:status :ok}
    {:status :error :message (s/explain-str ::member member-data)}))