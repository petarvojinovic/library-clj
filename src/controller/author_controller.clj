(ns controller.author_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.author_service :as author-service]
            [cheshire.core :as json]
            [validation.author_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes author-routes
           (GET "/author" []
             (let [authors (author-service/get-all-authors)
                   json-string (serialize-to-pretty-json authors)]
               (response/response json-string)))

           (GET "/author/:id" [id]
             (let [author (author-service/get-author-by-id id)]
               (if (= :ok (:status author))
                 (response/response (serialize-to-pretty-json (:data author)))
                 (response/status (response/response (:message author)) 404))))

           (GET "/author/:id/books" [id]
             (let [books (author-service/get-books-by-author-id id)]
               (if (= :ok (:status books))
                 (response/response (serialize-to-pretty-json (:data books)))
                 (response/status (response/response (:message books)) 404))))

           (POST "/author" request
             (let [json-parsed (:body request)]
               (let [{:keys [name birth_year]} json-parsed
                     validation-result (validation/validate-author json-parsed)]
                 (if (= :ok (:status validation-result))
                   (do
                     (author-service/create-author name birth_year)
                     (response/response (serialize-to-pretty-json {:message "Author created successfully"})))
                   (response/status
                     (response/response (serialize-to-pretty-json validation-result)) 400)))))

           (PUT "/author/:id" request
             (let [json-parsed (:body request)
                   author-id (-> request :params :id)
                   {:keys [name birth_year]} json-parsed
                   update-result (author-service/update-author author-id name birth_year)]
               (if (= :ok (:status update-result))
                 (response/response (serialize-to-pretty-json update-result))
                 (response/status (response/response (serialize-to-pretty-json update-result)) 404))))

           (DELETE "/author/:id" [id]
             (let [delete-result (author-service/delete-author id)]
               (if (= :ok (:status delete-result))
                 (response/response (serialize-to-pretty-json delete-result))
                 (response/status (response/response (serialize-to-pretty-json delete-result)) 404)))))
