(ns library-clj.core
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.edn :as edn]
            [controller.author_controller :as author-controller]
            [controller.book_controller :as book-controller]
            [controller.user_controller :as user-controller]
            [controller.loan_controller :as loan-controller]
            [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as middleware]))

(def db (edn/read-string (slurp "configuration/db.edn")))

(defn create-db-connection []
  (jdbc/db-do-commands
    {:connection-uri (format "jdbc:%s://%s/%s?user=%s&password=%s"
                             (db :dbtype) (db :host)
                             (db :dbname) (db :user)
                             (db :password))}
    (read-string (slurp (format "src/scripts/%s"
                                ((edn/read-string (slurp "configuration/init-db.edn")) :init-file-name))))))

(defn init []
  (jdbc/db-do-commands
    {:connection-uri (format "jdbc:%s://%s?user=%s&password=%s"
                             (db :dbtype) (db :host)
                             (db :user) (db :password))}
    false
    (format "CREATE DATABASE IF NOT EXISTS %s" (db :dbname)))
  (create-db-connection))

(defroutes app-routes
           author-controller/author-routes
           book-controller/book-routes
           user-controller/user-routes
           loan-controller/loan-routes)

(init)


(defn -main []
  (init)
  (jetty/run-jetty (-> app-routes
                       (middleware/wrap-json-body {:keywords? true :bigdecimals? true})
                       (middleware/wrap-json-response)
                       (wrap-defaults api-defaults))
                   {:port 8085}))

(-main)




