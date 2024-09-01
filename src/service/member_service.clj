(ns service.member_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn get-all-members []
  (jdbc/query db ["SELECT * FROM members"]))

(defn get-member-by-id [id]
  (jdbc/query db ["SELECT * FROM members WHERE id = ?" id]))

(defn create-member [name email phone membership_start membership_end]
  (jdbc/execute! db
                 ["INSERT INTO members (name, email, phone, membership_start, membership_end)
                  VALUES (?, ?, ?, ?, ?)"
                  name email phone membership_start membership_end]))

(defn update-member [id name email phone membership_start membership_end]
  (jdbc/execute! db
                 ["UPDATE members
                  SET name = ?, email = ?, phone = ?, membership_start = ?, membership_end = ?
                  WHERE id = ?"
                  name email phone membership_start membership_end id]))

(defn delete-member [id]
  (jdbc/execute! db ["DELETE FROM members WHERE id = ?" id]))
