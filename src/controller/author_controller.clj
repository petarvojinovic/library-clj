(ns controller.author_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.author_service :as authors-service]
            [cheshire.core :as json]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes author-routes
           (GET "/author" []
             (let [authors (authors-service/get-all-authors)
                   json-string (serialize-to-pretty-json authors)]
               (response/response json-string)))

           (GET "/author/:id" [id]
             (let [author (authors-service/get-author-by-id id)
                   json-string (serialize-to-pretty-json author)]
               (response/response json-string)))

           (POST "/author" request
             (let [json-parsed (:body request)
                   creation-result (authors-service/create-author json-parsed)]
               (if (= :ok (:status creation-result))
                 (response/response (serialize-to-pretty-json creation-result))
                 (response/status (response/response (serialize-to-pretty-json creation-result)) 400))))

           (PUT "/author/:id" request
             (let [json-parsed (:body request)
                   author-id (-> request :params :id)
                   update-result (authors-service/update-author author-id json-parsed)]
               (if (= :ok (:status update-result))
                 (response/response (serialize-to-pretty-json update-result))
                 (response/status (response/response (serialize-to-pretty-json update-result)) 400))))

           (DELETE "/author/:id" [id]
             (let [delete-result (authors-service/delete-author id)]
               (if (= :ok (:status delete-result))
                 (response/response (serialize-to-pretty-json delete-result))
                 (response/status (response/response (serialize-to-pretty-json delete-result)) 404))))

           (GET "/author/:id/books" [id]
             (let [books (authors-service/get-books-by-author-id id)
                   json-string (serialize-to-pretty-json books)]
               (response/response json-string))))
