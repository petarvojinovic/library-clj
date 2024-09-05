(ns controller.book_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE PATCH]]
            [service.book_service :as book-service]
            [cheshire.core :as json]
            [validation.book_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes book-routes
           (GET "/book" []
             (let [books (book-service/get-all-books)
                   json-string (serialize-to-pretty-json books)]
               (response/response json-string)))

           (GET "/book/:id" [id]
             (let [book (book-service/get-book-by-id id)]
               (if (= :ok (:status book))
                 (response/response (serialize-to-pretty-json (:data book)))
                 (response/status (response/response (:message book)) 404))))

           (GET "/book/author/:author_id" [author_id]
             (let [books (book-service/get-books-by-author-id author_id)]
               (if (= :ok (:status books))
                 (response/response (serialize-to-pretty-json (:data books)))
                 (response/status (response/response (:message books)) 404))))

           (POST "/book" request
             (let [json-parsed (:body request)]
               (let [{:keys [title genre year_published book_status author_id]} json-parsed
                     validation-result (validation/validate-book json-parsed)]
                 (if (= :ok (:status validation-result))
                   (do
                     (book-service/create-book title genre year_published book_status author_id)
                     (response/response (serialize-to-pretty-json {:message "Book created successfully"})))
                   (response/status
                     (response/response (serialize-to-pretty-json validation-result)) 400)))))

           (PUT "/book/:id" request
             (let [json-parsed (:body request)
                   book-id (-> request :params :id)
                   {:keys [title genre year_published book_status author_id]} json-parsed
                   update-result (book-service/update-book book-id title genre year_published book_status author_id)]
               (if (= :ok (:status update-result))
                 (response/response (serialize-to-pretty-json update-result))
                 (response/status (response/response (serialize-to-pretty-json update-result)) 404))))

           (DELETE "/book/:id" [id]
             (let [delete-result (book-service/delete-book id)]
               (if (= :ok (:status delete-result))
                 (response/response (serialize-to-pretty-json delete-result))
                 (response/status (response/response (serialize-to-pretty-json delete-result)) 404))))

           (PATCH "/book/:id" [id]
                  (let [update-result (book-service/set-book-status-available id)]
                    (if (= :ok (:status update-result))
                      (response/response (serialize-to-pretty-json update-result))
                      (response/status (response/response (serialize-to-pretty-json update-result)) 404)))))
