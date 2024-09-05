(ns service.user_service
  (:refer-clojure :exclude [seqable? get update])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn get-all-users []
  (jdbc/query db ["SELECT * FROM user"]))

(defn get-user-by-id [id]
  (let [user (first (jdbc/query db ["SELECT * FROM user WHERE id = ?" id]))]
    (if user
      {:status :ok :data user}
      {:status :error :message "User not found"})))

(defn get-members []
  (jdbc/query db ["SELECT * FROM user WHERE role = ?" "member"]))

(defn user-exists? [id]
  (not (empty? (jdbc/query db ["SELECT 1 FROM user WHERE id = ?" id]))))

(defn username-exists? [username]
  (not (empty? (jdbc/query db ["SELECT 1 FROM user WHERE username = ?" username]))))

(defn create-user [username password name email phone role membership_type]
  (if (username-exists? username)
    {:status :error :message "Username already taken"}
    (do
      (jdbc/execute! db
                     ["INSERT INTO user (username, password, name, email, phone, role, membership_type)
                      VALUES (?, ?, ?, ?, ?, ?, ?)"
                      username password name email phone role membership_type])
      {:status :ok :message "User created successfully"})))

(defn update-user [id username password name email phone role membership_type]
  (if (user-exists? id)
    (do
      (jdbc/execute! db
                     ["UPDATE user
                      SET username = ?, password = ?, name = ?, email = ?, phone = ?, role = ?, membership_type = ?
                      WHERE id = ?"
                      username password name email phone role membership_type id])
      {:status :ok :message "User updated successfully"})
    {:status :error :message "User not found"}))

(defn delete-user [id]
  (if (user-exists? id)
    (do
      (jdbc/execute! db ["DELETE FROM user WHERE id = ?" id])
      {:status :ok :message "User deleted successfully"})
    {:status :error :message "User not found"}))

