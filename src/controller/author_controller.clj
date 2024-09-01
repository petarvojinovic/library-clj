(ns controller.author_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.author_service :as authors-service]
            [cheshire.core :as json]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes author-routes
           (GET "/authors" []
             (let [authors (authors-service/get-all-authors)
                   json-string (serialize-to-pretty-json authors)]
               (response/response json-string)))

           (GET "/authors/:id" [id]
             (let [author (authors-service/get-author-by-id id)
                   json-string (serialize-to-pretty-json author)]
               (response/response json-string)))

           (POST "/authors" request
             (let [json-parsed (:body request)]
               (let [{:keys [name birth_year]} json-parsed]
                 (authors-service/create-author name birth_year)
                 (response/response "Author created successfully"))))

           (PUT "/authors/:id" request
             (let [json-parsed (:body request)
                   author-id (-> request :params :id)]
               (let [{:keys [name birth_year]} json-parsed]
                 (authors-service/update-author author-id name birth_year)
                 (response/response "Author updated successfully"))))

           (DELETE "/authors/:id" [id]
             (authors-service/delete-author id)
             (response/response "Author deleted successfully"))

           (GET "/authors/:id/books" [id]
             (let [books (authors-service/get-books-by-author-id id)
                   json-string (serialize-to-pretty-json books)]
               (response/response json-string))))

